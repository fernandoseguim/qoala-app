package solutions.plural.qoala;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void registerClick(View v) {
        Toast.makeText(RegisterActivity.this, "Registrando...", Toast.LENGTH_SHORT).show();
    }
}
