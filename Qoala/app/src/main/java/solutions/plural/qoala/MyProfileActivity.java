package solutions.plural.qoala;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.models.UserDTO;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JsonTask;
import solutions.plural.qoala.utils.SessionResources;

public class MyProfileActivity extends AppCompatActivity {

    UserDTO user = null;

    private EditText edtEmail = null;
    private EditText edtPassword = null;
    private EditText edtPassword2 = null;
    private EditText edtUsername = null;
    private TextView edtAddress = null;
    private TextView edtDistrict = null;
    private TextView edtCity = null;
    private TextView edtState = null;
    private TextView edtZipcode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_myprofile);
        setSupportActionBar(myToolbar);

        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        new GetUserTask().execute();

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPassword2 = (EditText) findViewById(R.id.edtPassword2);
        edtUsername = (EditText) findViewById(R.id.edtUserName);
        edtAddress = (EditText) findViewById(R.id.edtAddress);
        edtDistrict = (EditText) findViewById(R.id.edtDistrict);
        edtCity = (EditText) findViewById(R.id.edtCity);
        edtState = (EditText) findViewById(R.id.edtState);
        edtZipcode = (EditText) findViewById(R.id.edtZipCode);

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

        JSONStringer jsonUser;
        try {
            jsonUser = new JSONStringer()
                    .object()
                    .key("email").value(edtEmail.getText().toString().trim())
                    .key("name").value(edtUsername.getText().toString().trim())
                    .key("password").value(pass1)
                    .key("address").value(edtAddress.getText().toString().trim())
                    .key("district").value(edtDistrict.getText().toString().trim())
                    .key("city").value(edtCity.getText().toString().trim())
                    .key("state").value(edtState.getText().toString().trim())
                    .key("zipcode").value(edtZipcode.getText().toString().trim())
                    .endObject();

            new PutUserTask().execute(jsonUser);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return this;
    }

    public String getUserId() {
        return String.valueOf(SessionResources.getInstance().getUser().id_user);
    }

    private class GetUserTask extends JsonTask {

        @Override
        protected void setConfig() {
            setSilent(true);
            this.context = getContext();
            this.action = "accounts/me";
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
                    edtAddress.setText(user.address);
                    edtDistrict.setText(user.district);
                    edtCity.setText(user.city);
                    edtState.setText(user.state);
                    edtZipcode.setText(user.zipcode);
                    return true;
                default:
                    return false;
            }
        }
    }

    private class PutUserTask extends JsonTask {

        @Override
        protected void setConfig() {
            this.context = getContext();
            this.action = "users/" + getUserId();
            this.httpMethod = HttpMethod.PUT;
        }

        @NonNull
        @Override
        protected boolean onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject) {
            switch (responseCode) {
                case HttpStatusCode.NoContent:

                    new GetUserTask().execute();

                    new AlertDialog.Builder(getContext()).setMessage("Atualizado")
                    .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create().show();

                    return true;
            }
            return false;
        }
    }
}