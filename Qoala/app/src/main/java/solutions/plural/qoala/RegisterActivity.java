package solutions.plural.qoala;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.utils.JSONAPI2;
import solutions.plural.qoala.utils.Util;

public class RegisterActivity extends Activity {

    public Context getContext() {
        return RegisterActivity.this;
    }

    private EditText edtEmail = null;
    private EditText edtPassword = null;
    private EditText edtUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtUsername = (EditText) findViewById(R.id.edtUserName);


        Bundle bundle = getIntent().getExtras();
        String email = null;
        String pwd = null;

        if (bundle != null) {
            bundle.getString("email");

            if (bundle.containsKey("email")) {
                email = bundle.getString("email");
            }
            if (bundle.containsKey("pwd")) {
                pwd = bundle.getString("pwd");
            }
        }
        edtEmail.setText(email);
        edtPassword.setText(pwd);

    }

    public void registerClick(View v) {

        if (edtUsername.getText().length() == 0) {
            Toast.makeText(getContext(), R.string.error_field_required, Toast.LENGTH_SHORT).show();
            if (edtUsername.isFocusable() && !edtUsername.isFocused())
                edtUsername.requestFocus();
            return;
        }
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

        new RegisterTask().execute(
                edtUsername.getText().toString(),
                edtEmail.getText().toString(),
                edtPassword.getText().toString());
    }

    /**
     * Task para registro de usuario
     */
    private class RegisterTask extends AsyncTask<String, Integer, JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), getContext().getString(R.string.progress_title), getString(R.string.progress_registering));
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONStringer json = new JSONStringer();
                json.object();
                json.key("name").value(params[0]);
                json.key("email").value(params[1]);
                json.key("password").value(params[2]);
                json.endObject();

                return JSONAPI2.PostJSON("http://ws.qoala.com.br/accounts/register", json);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject retorno) {
            super.onPostExecute(retorno);
            progressDialog.dismiss();
            if (retorno == null || retorno.has("Error")) {
                Toast.makeText(getContext(), R.string.error_connection_failure, Toast.LENGTH_LONG).show();

            } else {
                try {
                    if (retorno.has(JSONAPI2.json_respondeCode)) {
                        StringBuilder mensagem = new StringBuilder();
                        int code = retorno.getInt(JSONAPI2.json_respondeCode);


                        switch (code) {
                            case 400://Bad Request
                                if (retorno.has(JSONAPI2.json_Message))
                                    mensagem.append(retorno.getString(JSONAPI2.json_Message));
                                new AlertDialog.Builder(getContext())
                                        .setTitle(R.string.title_activity_register)
                                        .setPositiveButton(android.R.string.ok, null)
                                        .setMessage(mensagem)
                                        .create()
                                        .show();
                                break;

                            case 201:
                                //todo: receber ok do login e registrar token
                                if (retorno.has(JSONAPI2.json_token)) {
                                    String token = retorno.getString(JSONAPI2.json_token);
                                    //SessionResources.getInstance(true).setToken(token);
                                    Intent i = getIntent();
                                    i.putExtra(JSONAPI2.json_token, token);
                                    setResult(RESULT_OK, i);
                                    finishActivity(101);
                                }
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
