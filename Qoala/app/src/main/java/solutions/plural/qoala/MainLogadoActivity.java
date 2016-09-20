package solutions.plural.qoala;

import android.os.Bundle;
import android.app.Activity;

public class MainLogadoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SessionResources.getInstance().ValidateToken();
        setContentView(R.layout.activity_main_logado);
    }
}
