package solutions.plural.qoala;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import solutions.plural.qoala.Models.BlogDTO;
import solutions.plural.qoala.Models.UserDTO;
import solutions.plural.qoala.adapters.BlogAdapter;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JsonTask;
import solutions.plural.qoala.utils.SessionResources;

public class MainLogadoActivity extends AppCompatActivity {

    private Integer paginacao = 1;
    private ListView lista = null;
    private BlogDTO blog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_logado);
        new PostsTask().setSilent(true).execute();

        TextView txtUser = (TextView) findViewById(R.id.txtUser);
        UserDTO user = SessionResources.getInstance().getUser();
        if (user != null)
            txtUser.setText(user.toJson());

        setupMenuBar();

        lista = (ListView) findViewById(R.id.lista);

        lista.setVerticalScrollBarEnabled(true);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BlogAdapter.ViewHolder holder = (BlogAdapter.ViewHolder) view.getTag();
                // TODO: criar uma activity para abrir o conteudo do comment e os commentarios
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
                intent.putExtra("comment", holder.post.toJson());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (blog == null)
            new PostsTask().setSilent(true).execute();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupMenuBar() {
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);
        ActionBar b = getSupportActionBar();
        assert b != null;
        b.setDisplayUseLogoEnabled(true);
        b.setDisplayShowHomeEnabled(true);
        b.setLogo(R.mipmap.ic_launcher);
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
                i = new Intent(this, MyProfileActivity.class);
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

    public Context getContext() {
        return this;
    }

    public String getPaginacao() {
        return paginacao.toString();
    }


    /**
     * Task para carregar Posts
     */
    private class PostsTask extends JsonTask {

        @Override
        protected void setConfig() {
            this.context = getContext();
            this.action = "posts?page=" + getPaginacao();
            this.httpMethod = HttpMethod.GET;
        }

        @Override
        protected boolean onPostExecuted(int responseCode, String responseMessage, JSONObject jsonObject) {
            switch (responseCode) {
                case HttpStatusCode.OK:
                    blog = BlogDTO.fromJson(jsonObject.toString());
                    reloadPosts();
                    return true;
            }
            return false;
        }
    }

    private void reloadPosts() {
        BlogAdapter adapter = new BlogAdapter(this, blog);
        lista.setAdapter(adapter);
    }

}
