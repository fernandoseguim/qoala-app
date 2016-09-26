package solutions.plural.qoala;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import solutions.plural.qoala.utils.SessionResources;

public class MainLogadoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logado);

        TextView txtUser = (TextView) findViewById(R.id.txtUser);
        txtUser.setText(SessionResources.getInstance().getUser().toString());
    }

    public void logoutClick(View v) {
        SessionResources.getInstance(true).setToken("", this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}