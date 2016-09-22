package solutions.plural.qoala;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.utils.JSONAPI;
import solutions.plural.qoala.utils.SessionResources;

public class ValidaLoginActivity extends Activity {

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionResources sr = SessionResources.getInstance();
        if (sr.getToken().isEmpty())
            sr.loadToken(this);

        if (!sr.getToken().isEmpty()) {
            new ValidateLoginTask().execute(sr.getToken());
        }
    }

    private void startDeviceListActivity() {
        Intent intent = new Intent(getContext(), MainLogadoActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private class ValidateLoginTask extends AsyncTask<String, Integer, JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), getContext().getString(R.string.progress_title), getContext().getString(R.string.progress_waiting));
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONStringer json = new JSONStringer();
                json.object();
                json.key(JSONAPI.json_token).value(params[0]);
                json.endObject();

                JSONObject jso = JSONAPI.Post("Accounts/ValidadeToken", json);

                return jso;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject retorno) {
            progressDialog.dismiss();
            Log.d("LOGIN", "retorno da validação: " + retorno);
            try {
                if (retorno == null || retorno.has("Error")) {
                    Log.e("LOGIN", "ERRO: " + retorno.getString("Error"));
                } else {
                    if (retorno.has(JSONAPI.json_responseCode)) {
                        int code = retorno.getInt(JSONAPI.json_responseCode);
                        switch (code) {
                            case 410:// Gone
                                SessionResources.getInstance(true).setToken("", getContext());
                                startLoginActivity();
                                break;
                            case 202:// Accepted
                                startDeviceListActivity();
                                break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
