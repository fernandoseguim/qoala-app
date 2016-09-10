package solutions.plural.qoala;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail = null;
    private EditText edtPassword = null;

    // Request code for register activity callback
    final static int RC_Register = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
    }

    public void loginClick(View v) {
        login();
    }

    protected void login() {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("email", edtEmail.getText().toString());
        intent.putExtra("pwd", edtPassword.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, RC_Register);
        Toast.makeText(MainActivity.this, edtEmail.getText() + "-" + edtPassword.getText(), Toast.LENGTH_SHORT).show();
    }

    public void registerClick(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("email", edtEmail.getText());
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent, RC_Register);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_Register) {
            if (resultCode == 1) {
                Bundle b = data.getExtras();
                if (b.containsKey("email"))
                    edtEmail.setText(b.getString("email"));
                if (b.containsKey("pwd"))
                    edtEmail.setText(b.getString("pwd"));
                if (edtEmail.getText().length() > 0 && edtPassword.getText().length() > 0) {
                    login();
                }
            }
        }
    }
}
