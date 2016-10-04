package solutions.plural.qoala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.models.UserDTO;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JsonTask;
import solutions.plural.qoala.utils.SessionResources;

public class SplashActivity extends Activity {

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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
            setSilent(true);
            this.context = getContext();
            this.action = "Accounts/Me";
            this.httpMethod = HttpMethod.GET;
        }

        @Override
        protected JSONObject doInBackground(JSONStringer... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return super.doInBackground(params);
        }

        @Override
        protected boolean onPostExecuted(int respondeCode, String respondeMessage, JSONObject jsonObject) {
            switch (respondeCode) {
                case HttpStatusCode.Unauthorized:
                case HttpStatusCode.NotFound:
                    SessionResources.getInstance(true).setToken("", getContext());
                    startLoginActivity();
                    return true;
                case HttpStatusCode.OK:
                    SessionResources sr = SessionResources.getInstance(true);
                    sr.setUser(UserDTO.fromJson(jsonObject.toString()));
                    startDeviceListActivity();
                    return true;
            }
            return false;

        }
    }
}
