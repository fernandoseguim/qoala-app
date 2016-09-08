package solutions.plural.qoala;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.utils.JSONAPI;

public class RegisterActivity extends Activity {


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

    }

    private boolean isValidEmail(CharSequence email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void registerClick(View v) {

        if (edtUsername.getText().length() == 0) {
            Toast.makeText(getContext(), R.string.error_field_required, Toast.LENGTH_SHORT).show();
            if (edtUsername.isFocusable() && !edtUsername.isFocused())
                edtUsername.requestFocus();
            return;
        }
        if (!isValidEmail(edtEmail.getText().toString())) {
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

        RegisterTask task = new RegisterTask();
        task.execute(edtUsername.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString());
    }

    public Context getContext() {
        return RegisterActivity.this;
    }


    private class RegisterTask extends AsyncTask<String, Integer, JSONObject> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getContext(), getContext().getString(R.string.progress_title), getString(R.string.progress_registering));
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                // TODO: 05/09/2016 Acessar serviço de registro

                JSONStringer json = new JSONStringer();
                json.object();
                json.key("username").value(params[0]);
                json.key("email").value(params[1]);
                json.key("pwd").value(params[2]);
                json.endObject();

                return JSONAPI.PostJSON("https://echo.getpostman.com/post", json);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject retorno) {
            super.onPostExecute(retorno);
            progressDialog.dismiss();
            // TODO: 05/09/2016 De acordo com o retorno, armazenar o tokienID ou retornar a mensagem apropriada.
            if (retorno == null) {
                Toast.makeText(getContext(), R.string.error_register_failure, Toast.LENGTH_LONG).show();
            } else {
                try {
                    if (retorno.getJSONObject("form") != null) {
                        String nome = retorno.getJSONObject("form").getJSONObject("").getString("username");
                        if (nome.equalsIgnoreCase("Gabriel")) {
                            Toast.makeText(getContext(), R.string.info_register_success, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            finishActivity(101);
        }
    }
}
