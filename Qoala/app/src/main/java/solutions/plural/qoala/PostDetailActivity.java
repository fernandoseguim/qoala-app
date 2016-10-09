package solutions.plural.qoala;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import solutions.plural.qoala.adapters.CommentsAdapter;
import solutions.plural.qoala.models.PostsDTO;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JsonTask;

public class PostDetailActivity extends AppCompatActivity {

    private AlertDialog commentsDialog;
    PostsDTO post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_post);
        setSupportActionBar(myToolbar);

        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
        }

        String json = getIntent().getExtras().getString("post");
        post = PostsDTO.fromJson(json.toString());

    }

    @Override
    protected void onStart() {
        super.onStart();
        new PostDetailTask().execute();
    }

    public void seeCommentsClick(View view) {
        commentsDialog.show();
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
            action = "posts/"+getPostId();
            httpMethod = HttpMethod.GET;
        }

        @NonNull
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

                    //
                    ListView listView = new ListView(getContext());

                    CommentsAdapter adapter = new CommentsAdapter(getContext(), post);
                    listView.setAdapter(adapter);
                    commentsDialog = new AlertDialog.Builder(getContext())
                            .setView(listView)
                            .setTitle(R.string.title_activity_comments)
                            .setCancelable(true)
                            .setNeutralButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            )
                            .create();

                    return true;
            }
            // Mensagem que o post nao foi encontrado
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.error_connection_failure)
                    .setTitle(R.string.title_activity_comments)
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
