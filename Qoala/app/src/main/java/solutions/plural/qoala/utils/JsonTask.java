package solutions.plural.qoala.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import junit.framework.Assert;

import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.LoginActivity;
import solutions.plural.qoala.R;

/**
 * <p>Executa uma tarefa em background para JSON</p>
 * <pre class="prettyprint">
 * private class GetUserTask extends JsonTask {
 * protected void setConfig() {
 * this.context=getContext();
 * this.action = "Users/Get";
 * this.httpMethod = HttpMethod.GET;
 * }
 * <p>
 * protected void onPostExecuted(int respondeCode, String respondeMessage, JSONObject jsonObject) {
 * switch (respondeCode) {
 * case 410:// Gone
 * SessionResources.getInstance(true).setToken("", getContext());
 * startLoginActivity();
 * break;
 * case 202:// Accepted
 * startDeviceListActivity();
 * break;
 * }
 * }
 * <p>
 * }
 * </pre>
 * <p>
 * <p>Once created, a task is executed very simply:</p>
 * <pre class="prettyprint">
 * new GetUserTask().execute(jsonObject);
 * </pre>
 */
public abstract class JsonTask extends AsyncTask<JSONStringer, Integer, JSONObject> {

    protected final String TAG;

    protected ProgressDialog progressDialog;
    protected Context context = null;
    protected String action = "";
    protected
    @HttpMethod
    String httpMethod;
    private boolean _silent = false;

    public JsonTask() {
        super();
        setConfig();
        TAG = getClass().getName() + "::/" + action + ":";
    }

    public JsonTask setSilent(boolean silent) {
        this._silent = silent;
        return this;
    }

    @Override
    protected void onPreExecute() {
        Assert.assertNotNull(TAG + " is needed to be set CONTEXT on setup()", context);
        Log.d(TAG, "preExecuting, Silent = " + _silent);
        if (!_silent)
            progressDialog = ProgressDialog.show(context, context.getString(R.string.progress_title), context.getString(R.string.progress_waiting));
    }

    @Override
    protected JSONObject doInBackground(JSONStringer... params) {
        JSONStringer json = null;
        if (params != null && params.length >= 1)
            json = params[0];
        JSONObject jso = null;
        switch (httpMethod) {
            case HttpMethod.GET:
                jso = JSONAPI.Get(action);
                break;
            case HttpMethod.DELETE:
            case HttpMethod.PUT:
            case HttpMethod.POST:
                jso = JSONAPI.CallURL(httpMethod, action, json);
                break;
        }

        return jso;

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (!_silent)
            progressDialog.dismiss();

        Log.d(TAG, "retorno da validação ["+jsonObject.toString().length()/1024+"k]: " + jsonObject.toString());
        try {
            if (jsonObject == null || jsonObject.has("Error")) {
                Log.e(TAG, "ERRO: " + jsonObject.getString("Error"));
                new AlertDialog.Builder(context)
                        .setTitle(R.string.app_name)
                        .setPositiveButton(android.R.string.ok, null)
                        .setMessage(R.string.error_connection_failure)
                        .create()
                        .show();

            } else {
                if (jsonObject.has(JSONAPI.json_responseCode)) {
                    @HttpStatusCode int code = jsonObject.optInt(JSONAPI.json_responseCode);
                    String message = jsonObject.optString(JSONAPI.json_message);
                    if(message.isEmpty())
                        message = jsonObject.optString(JSONAPI.json_responseMessage);
                    jsonObject.remove(JSONAPI.json_responseCode);
                    jsonObject.remove(JSONAPI.json_responseMessage);
                    if (code == HttpStatusCode.Unauthorized) {
                        Log.i(TAG, code + ": " + message);
                        if (context instanceof Activity) {
                            Toast.makeText(context, context.getResources().getText(R.string.error_unauthorized) + "\n" + message, Toast.LENGTH_LONG).show();
                            context.startActivity(new Intent(context, LoginActivity.class));
                            ((Activity) context).finish();
                        }
                    } else {

                        boolean postExecutedok = onPostExecuted(code, message, jsonObject);

                        if (!postExecutedok) {
                            Log.e(TAG, String.format("Code: {0}; Message: {1}.", code, message));
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.app_name)
                                    .setPositiveButton(android.R.string.ok, null)
                                    .setMessage(message)
                                    .create()
                                    .show();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>Este metodo é Executado depois da chamada de <code>onPostExecute</code>.
     * <p>Deve retornar <code>true</code> se o retorno foi como experado, <br/>
     * ou <code>false</code> se deve apresentrar erro.</p>
     * <pre class="prettyprint">
     * protected boolean onPostExecuted(int responseCode, String responseMessage, JSONObject jsonObject) {
     * switch (responseCode) {
     * case HttpStatusCode.OK:
     * reloadPosts();
     * return true;
     * }
     * return false;
     * }
     * </pre>     *
     *
     * @param responseCode
     * @param responseMessage
     * @param jsonObject
     * @return <p>É importante que o retorno seja <code>true</code>, se foi feito o tratamento do retorno
     * para não apresentar uma mensagem de alerta com a mensagem de retorno. </p><p>Quando
     * <code>false</code>, um alerta irá apresentar a mensagem .</p>
     */
    @NonNull
    protected abstract boolean onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject);

    /**
     * Este metodo deve ser usado para configurar as seguintes variaveis.
     * <p>
     * <p>
     * <code>this.context=getContext();
     * this.action = "Accounts/ValidateToken";
     * this.httpMethod = HttpMethod.POST;</code>
     */
    protected abstract void setConfig();

}
