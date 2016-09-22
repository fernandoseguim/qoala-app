package solutions.plural.qoala;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.utils.JSONAPI;
import solutions.plural.qoala.utils.SessionResources;
import solutions.plural.qoala.utils.Util;

public class LoginActivity extends AppCompatActivity {

    public Context getContext() {
        return LoginActivity.this;
    }

    private EditText edtEmail = null;
    private EditText edtPassword = null;

    // Request code for register activity callback
    final static int RC_Register = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionResources sr = SessionResources.getInstance();
            setContentView(R.layout.activity_login);
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

        String email = edtEmail.getText().toString();
        String pwd = edtPassword.getText().toString();
        new RequestLoginTask().execute(pwd, email);
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
    private class RequestLoginTask extends AsyncTask<String, Integer, JSONObject> {

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
                json.key("password").value(params[0]);
                json.key("email").value(params[1]);
                json.endObject();

                return JSONAPI.Post("accounts/login", json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject retorno) {
            progressDialog.dismiss();
            Log.d("LOGIN", "retorno do login: " + retorno);
            try {
                if (retorno == null || retorno.has("Error")) {
                    Log.e("LOGIN", "ERRO: " + retorno.getString("Error"));
                    Toast.makeText(getContext(), R.string.error_connection_failure, Toast.LENGTH_LONG).show();
                } else {
                    if (retorno.has(JSONAPI.json_responseCode)) {
                        StringBuilder mensagem = new StringBuilder();
                        int code = retorno.getInt(JSONAPI.json_responseCode);

                        switch (code) {
                            case 400://Bad Request
                                if (retorno.has(JSONAPI.json_message))
                                    mensagem.append(retorno.getString(JSONAPI.json_message));
                                new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.title_activity_login)
                                        .setNegativeButton(R.string.action_registergo, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                startRegisterActivity();
                                            }
                                        })
                                        .setPositiveButton(android.R.string.ok, null)
                                        .setMessage(mensagem)
                                        .create()
                                        .show();
                                break;

                            case 200:
                            case 201://Created
                                //todo: receber ok do login e registrar token
                                if (retorno.has(JSONAPI.json_token)) {
                                    String token = retorno.getString(JSONAPI.json_token);
                                    SessionResources.getInstance(true).setToken(token, getContext());
                                    startDeviceListActivity();
                                }
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
