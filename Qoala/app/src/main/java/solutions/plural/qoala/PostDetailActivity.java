package solutions.plural.qoala;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import solutions.plural.qoala.Models.PostsDTO;

public class PostDetailActivity extends AppCompatActivity {

    private PostsDTO post = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.app_name);
        setSupportActionBar(myToolbar);

//        ActionBar bar = getActionBar();
//        if (bar != null)
//            bar.setDisplayHomeAsUpEnabled(true);

        String json = getIntent().getExtras().getString("post");
        post = PostsDTO.fromJson(json);

        // TODO: 30/09/2016 desenhar a tela...
        // TODO: 30/09/2016 fazer lógica para exibir post e comentários no final...

    }

}
