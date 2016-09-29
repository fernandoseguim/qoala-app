package solutions.plural.qoala;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import solutions.plural.qoala.utils.SessionResources;

public class MainLogadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logado);

        TextView txtUser = (TextView) findViewById(R.id.txtUser);
        txtUser.setText(SessionResources.getInstance().getUser().toString());

        setupMenuBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupMenuBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

    }

    public void logout() {
        SessionResources.getInstance(true).setToken("", this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_settings:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_mydevices:
                i = new Intent(this, MainLogadoActivity.class);
                startActivity(i);
                return true;

            case R.id.action_myprofile:
                i = new Intent(this, MainLogadoActivity.class);
                startActivity(i);
                return true;

            case R.id.action_logout:
                logout();
                i = new Intent(this, SplashActivity.class);
                startActivity(i);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
