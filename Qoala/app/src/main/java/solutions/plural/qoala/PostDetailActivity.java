package solutions.plural.qoala;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import solutions.plural.qoala.adapters.CommentsAdapter;
import solutions.plural.qoala.models.PostsDTO;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JsonTask;

public class PostDetailActivity extends AppCompatActivity {

    PostsDTO post;
    CommentsAdapter commentsAdapter;
    private AlertDialog commentsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_post_comments);
        setSupportActionBar(myToolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        savedInstanceState = getIntent().getExtras();
        String json = savedInstanceState.getString("post");
        post = PostsDTO.fromJson(json);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new PostDetailTask().execute();
    }

    public void seeCommentsClick(View view) {
        Intent i = new Intent(this, PostCommentsActivity.class);
        i.putExtra("postId", post.id_post);
        startActivityForResult(i, 1);
        //commentsDialog.show();
    }

    public Activity getContext() {
        return this;
    }

    public String getPostId() {
        return String.valueOf(post.id_post);
    }

    public class PostDetailTask extends JsonTask {
        @Override
        protected void setConfig() {
            this.context = getContext();
            action = "posts/" + getPostId();
            httpMethod = HttpMethod.GET;
        }

        @Override
        protected boolean onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject) {
            switch (responseCode) {
                case HttpStatusCode.OK:
                    PostsDTO post = PostsDTO.fromJson(jsonObject.toString());

                    TextView post_title = (TextView) findViewById(R.id.post_title);
                    post_title.setText(post.title);

                    TextView post_content = (TextView) findViewById(R.id.post_content);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        post_content.setText(Html.fromHtml(post.content, Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        post_content.setText(Html.fromHtml(post.content));
                    }

                    return true;
            }
            // Mensagem que o post nao foi encontrado
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.error_connection_failure)
                    .setTitle(R.string.title_activity_post_comments)
                    .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                    )
                    .create().show();

            return false;
        }
    }

}