package solutions.plural.qoala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;

import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JsonTask;
import solutions.plural.qoala.utils.SessionResources;

public class ValidaLoginActivity extends Activity {

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO montar layout do loading... (logo no centro e bg no fundo)
    }

    @Override
    protected void onStart() {
        super.onStart();
        SessionResources sr = SessionResources.getInstance();
        if (!sr.getToken(this).isEmpty()) {
            new ValidateLoginTask().execute();
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
            this.action = "Accounts/Me";
            this.httpMethod = HttpMethod.GET;
        }

        @Override
        protected void onPostExecuted(int respondeCode, String respondeMessage, JSONObject jsonObject) {
            switch (respondeCode) {
                case HttpStatusCode.Unauthorized:
                case HttpStatusCode.NotFound:
                    SessionResources.getInstance(true).setToken("", getContext());
                    startLoginActivity();
                    break;
                case HttpStatusCode.OK:
                    startDeviceListActivity();
                    break;
            }
        }
    }
}
