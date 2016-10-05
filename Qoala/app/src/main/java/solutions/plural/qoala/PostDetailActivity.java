package solutions.plural.qoala;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import solutions.plural.qoala.adapters.CommentsAdapter;
import solutions.plural.qoala.models.PostsDTO;

public class PostDetailActivity extends AppCompatActivity {

    private AlertDialog commentsDialog;

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

        String json = getIntent().getExtras().getString("comment");
        PostsDTO post = PostsDTO.fromJson(json);

        TextView post_title = (TextView) findViewById(R.id.post_title);
        post_title.setText(post.title);

        TextView post_content = (TextView) findViewById(R.id.post_content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            post_content.setText(Html.fromHtml(post.content, Html.FROM_HTML_MODE_COMPACT));
        } else {
            post_content.setText(Html.fromHtml(post.content));
        }

        //
        ListView listView = new ListView(this);

        CommentsAdapter adapter = new CommentsAdapter(this, post);
        listView.setAdapter(adapter);
        commentsDialog = new AlertDialog.Builder(this)
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

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void seeCommentsClick(View view) {
        commentsDialog.show();
    }
}
