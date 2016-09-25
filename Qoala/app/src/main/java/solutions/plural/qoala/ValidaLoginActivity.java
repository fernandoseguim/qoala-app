package solutions.plural.qoala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.JsonTask;
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
        if (!sr.getToken(this).isEmpty()) {
            try {
                JSONStringer json = new JSONStringer()
                        .object()
                        .key("token").value(sr.getToken())
                        .endObject();
                new ValidateLoginTask().execute(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            startLoginActivity();
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

    private class ValidateLoginTask extends JsonTask {

        @Override
        protected void setConfig() {
            this.context = getContext();
            this.action = "Accounts/ValidateToken";
            this.httpMethod = HttpMethod.POST;
        }

        @Override
        protected void onPostExecuted(int respondeCode, String respondeMessage, JSONObject jsonObject) {

            switch (respondeCode) {
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
}
