package solutions.plural.qoala;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.Models.UserDTO;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.JSONAPI;
import solutions.plural.qoala.utils.JsonTask;
import solutions.plural.qoala.utils.SessionResources;
import solutions.plural.qoala.utils.Util;

public class LoginActivity extends AppCompatActivity {

    // Request code for register activity callback
    final static int RC_Register = 101;
    private EditText edtEmail = null;
    private EditText edtPassword = null;

    public Context getContext() {
        return LoginActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionResources sr = SessionResources.getInstance();
        setContentView(R.layout.activity_login_scroll);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    public void loginClick(View v) {
        login();
    }

    protected void login() {

        if (!Util.isValidEmail(edtEmail.getText().toString())) {
            Toast.makeText(getContext(), R.string.error_invalid_email, Toast.LENGTH_LONG).show();
            if (edtEmail.isFocusable())
                edtEmail.requestFocus();
            return;
        }
        if (edtPassword.getText().length() == 0) {
            Toast.makeText(getContext(), R.string.error_field_required, Toast.LENGTH_SHORT).show();
            if (edtPassword.isFocusable())
                edtPassword.requestFocus();
            return;
        }

        try {
            JSONStringer json = new JSONStringer()
                    .object()
                    .key("password").value(edtPassword.getText().toString())
                    .key("email").value(edtEmail.getText().toString())
                    .endObject();

            new RequestLoginTask().execute(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void registerClick(View v) {
        startRegisterActivity();
    }

    private void startDeviceListActivity() {
        Intent intent = new Intent(getContext(), MainLogadoActivity.class);
        startActivity(intent);
        finish();
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        intent.putExtra("email", edtEmail.getText().toString());
        intent.putExtra("pwd", edtPassword.getText().toString());
        startActivityForResult(intent, RC_Register);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_Register) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                if (b.containsKey(JSONAPI.json_token)) {
                    String token = b.getString(JSONAPI.json_token);
                    SessionResources.getInstance().setToken(token, getContext());
                    startDeviceListActivity();
                }
            }
        }
    }

    /**
     * Task para requisitar login
     */
    private class RequestLoginTask extends JsonTask {

        @Override
        protected void setConfig() {
            this.context = getContext();
            this.action = "Accounts/Login";
            this.httpMethod = HttpMethod.POST;
        }

        @Override
        protected void onPostExecuted(int responseCode, String responseMessage, JSONObject jsonObject) {

            switch (responseCode) {
                case 400://Bad Request
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.title_activity_login)
                            .setNegativeButton(R.string.action_registergo, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startRegisterActivity();
                                }
                            })
                            .setPositiveButton(android.R.string.ok, null)
                            .setMessage(responseMessage)
                            .create()
                            .show();
                    break;

                case 200://OK
                case 201://Created
                    //todo: receber ok do login e registrar token
                    if (jsonObject.has(JSONAPI.json_token)) {
                        String token = jsonObject.optString(JSONAPI.json_token);
                        SessionResources sr = SessionResources.getInstance(true);
                        sr.setToken(token, getContext());
                        sr.setUser(UserDTO.fromJson(jsonObject.toString()));
                        startDeviceListActivity();
                    }
                    break;
            }
        }
    }
}