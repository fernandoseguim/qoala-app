package solutions.plural.qoala;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import solutions.plural.qoala.adapters.CommentsAdapter;
import solutions.plural.qoala.models.CommentsListDTO;
import solutions.plural.qoala.utils.HttpMethod;
import solutions.plural.qoala.utils.HttpStatusCode;
import solutions.plural.qoala.utils.JSONAPI;
import solutions.plural.qoala.utils.JsonTask;
import solutions.plural.qoala.utils.SessionResources;

public class PostCommentsActivity extends AppCompatActivity {

    private CommentsAdapter commentsAdapter;
    private int postId;
    private ListView listView;
    private EditText edtComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle(R.string.title_activity_post_comments);
        setSupportActionBar(myToolbar);

        new StartTask().execute();
    }

    public Activity getContext() {
        return this;
    }

    public int getPostId() {
        return postId;
    }

    private void setFooterList(ListView lista) {
        View footerView = getLayoutInflater().inflate(R.layout.itemlist_post_comments_footer, null, false);

        lista.addFooterView(footerView);
        edtComment = (EditText) footerView.findViewById(R.id.edtComment);

        ImageButton btn_add = (ImageButton) footerView.findViewById(R.id.btn_comments_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("COMMENTS", "Adicionando comentário...");
                JSONStringer comment;
                try {
                    comment = new JSONStringer()
                            .object()
                            .key("content").value(edtComment.getText().toString())
                            .key("id_user").value(SessionResources.getInstance().getUserID())
                            .key("id_post").value(getPostId())
                            .endObject();
                    new AddCommentTask().execute(comment);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class StartTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            listView = (ListView) findViewById(R.id.lista);

            postId = getIntent().getExtras().getInt("postId");

            commentsAdapter = new CommentsAdapter(getContext(), new CommentsListDTO());

            setFooterList(listView);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listView.setAdapter(commentsAdapter);
            new GetComments().execute();
        }
    }

    public class AddCommentTask extends JsonTask {
        @Override
        protected void setConfig() {
            this.context = getContext();
            action = "posts/" + getPostId() + "/comments";
            httpMethod = HttpMethod.POST;
        }

        @NonNull
        @Override
        protected boolean onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject) {
            switch (responseCode) {
                case HttpStatusCode.Created:
                    edtComment.setText("");
                    new GetComments().execute();
//                    CommentsDTO comment = CommentsDTO.fromJson(jsonObject.toString());
//                    commentsAdapter.add(comment);
                    return true;
            }
            return false;
        }
    }

    public class GetComments extends JsonTask {
        @Override
        protected void setConfig() {
            this.context = getContext();
            action = "posts/" + getPostId() + "/comments";
            httpMethod = HttpMethod.GET;
        }

        @Override
        protected boolean onPostExecuted(@HttpStatusCode int responseCode, String responseMessage, JSONObject jsonObject) {
            switch (responseCode) {
                case HttpStatusCode.OK:
                    CommentsListDTO news = CommentsListDTO.fromJson(jsonObject.optJSONArray(JSONAPI.json_array).toString());
                    commentsAdapter.clear();
                    commentsAdapter.addAll(news);
                    return true;
            }
            return false;
        }
    }
}
