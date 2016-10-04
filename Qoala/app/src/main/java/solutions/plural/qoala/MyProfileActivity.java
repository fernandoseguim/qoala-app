package solutions.plural.qoala;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.Modelos.UserDTO;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JsonTask;
import solutions.plural.qoala.utils.SessionResources;

public class MyProfileActivity extends AppCompatActivity {

    UserDTO user = null;
    private GetUserTask getUser;
    private PutUserTask putUser;
    private String id_user = String.valueOf(SessionResources.getInstance().getUser().id_user);

    private EditText edtEmail = null;
    private EditText edtPassword = null;
    private EditText edtPassword2 = null;
    private EditText edtUsername = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_post);
        setSupportActionBar(myToolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        getUser = new GetUserTask();
        getUser.execute();

        putUser = new PutUserTask();


        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassword2 = (EditText) findViewById(R.id.edtPassword2);
        edtUsername = (EditText) findViewById(R.id.edtUserName);

    }

    public void updateUserClick(View view) {
        String pass1 = edtPassword.getText().toString(),
                pass2 = edtPassword.getText().toString();
        if (!pass1.isEmpty() && !pass1.equals(pass2)) {
            Snackbar.make(edtPassword, "Senhas não conferem! \nVerifique e tente novamente.", Snackbar.LENGTH_LONG).show();
            edtPassword.setText("");
            edtPassword2.setText("");
            return;
        }

        JSONStringer jsonUser = null;
        try {
            jsonUser = new JSONStringer()
                    .object()
                    .key("email").value(edtEmail.getText().toString())
                    .key("name").value(edtUsername.getText().toString())
                    .key("password").value(pass1)
                    .endObject();

            putUser.execute(jsonUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return this;
    }

    private class GetUserTask extends JsonTask {

        @Override
        protected void setConfig() {
            this.context = getContext();
            this.action = "users/" + id_user;
            this.httpMethod = HttpMethod.GET;
        }

        @NonNull
        @Override
        protected boolean onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject) {
            switch (responseCode) {
                case HttpStatusCode.OK:
                    user = UserDTO.fromJson(jsonObject.toString());
                    SessionResources.getInstance().setUser(user);
                    edtUsername.setText(user.name);
                    edtEmail.setText(user.email);
                    edtPassword.setText("");
                    edtPassword2.setText("");
                    return true;
            }
            return false;
        }
    }

    private class PutUserTask extends JsonTask {

        @Override
        protected void setConfig() {
            this.context = getContext();
            this.action = "users/" + id_user;
            this.httpMethod = HttpMethod.PUT;
        }

        @NonNull
        @Override
        protected boolean onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject) {

            switch (responseCode) {
                case HttpStatusCode.NoContent:
                    Snackbar.make(edtUsername, "Atualizado!", Snackbar.LENGTH_LONG).show();
                    getUser.execute();

                    return true;
            }
            return false;
        }
    }
}