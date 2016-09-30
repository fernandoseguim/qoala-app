package solutions.plural.qoala.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

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
    private boolean _silent = false;

    protected
    @HttpMethod
    String httpMethod;

    public JsonTask setSilent(boolean silent) {
        this._silent = silent;
        return this;
    }

    public JsonTask() {
        super();
        setConfig();
        TAG = getClass().getName() + "/" + action + ":";
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
        if (params.length >= 1)
            json = params[0];
        JSONObject jso = null;
        switch (httpMethod) {
            case HttpMethod.GET:
                jso = JSONAPI.Get(action);
                break;
            case HttpMethod.POST:
                jso = JSONAPI.Post(action, json);
                break;
        }

        return jso;

    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        if (!_silent)
            progressDialog.dismiss();
        Log.d(TAG, "retorno da validação: " + jsonObject);
        try {
            if (jsonObject == null || jsonObject.has("Error")) {
                Log.e(TAG, "ERRO: " + jsonObject.getString("Error"));
            } else {
                if (jsonObject == null || jsonObject.has("Message") && jsonObject.has(JSONAPI.json_responseMessage)) {
                    Log.e(TAG, "ERRO: " + jsonObject.getString(JSONAPI.json_responseCode) +
                            " - " + jsonObject.getString(JSONAPI.json_responseMessage) +
                            "\nMessage:" + jsonObject.optString("Message"));
                } else {
                    if (jsonObject.has(JSONAPI.json_responseCode)) {
                        @HttpStatusCode int code = jsonObject.optInt(JSONAPI.json_responseCode);
                        String msg = jsonObject.optString(JSONAPI.json_responseMessage) + " " + jsonObject.optString(JSONAPI.json_message);
                        jsonObject.remove(JSONAPI.json_responseCode);
                        jsonObject.remove(JSONAPI.json_responseMessage);
                        if (code == HttpStatusCode.Unauthorized) {
                            Log.d(TAG, code + ": " + msg);
                            Intent intent = new Intent(context, LoginActivity.class);
                            if (context instanceof Activity) {
                                ((Activity) context).startActivityForResult(intent, 1);
                                ((Activity) context).finish();
                            }
                        } else {
                            onPostExecuted(code, msg, jsonObject);
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
     * </p>
     *
     * @param responseCode
     * @param responseMessage
     * @param jsonObject
     */
    protected abstract void onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject);

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
