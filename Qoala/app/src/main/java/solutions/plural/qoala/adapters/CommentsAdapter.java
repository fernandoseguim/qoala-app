package solutions.plural.qoala.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import solutions.plural.qoala.R;
import solutions.plural.qoala.models.CommentsDTO;
import solutions.plural.qoala.models.PostsDTO;

/**
 * Created by gabri on 29/09/2016.
 */

public class CommentsAdapter extends ArrayAdapter<CommentsDTO> {

    private final Activity context;
    private final PostsDTO post;

    public CommentsAdapter(Activity context, PostsDTO post) {
        super(context, R.layout.itemlist_post_comments, post.comments);
        this.context = context;
        this.post = post;
    }

    @Override
    public int getCount() {
        return post.comments.size();
    }

    @Nullable
    @Override
    public CommentsDTO getItem(int position) {
        return post.comments.get(position);
    }

    @Override
    public int getPosition(CommentsDTO item) {
        return post.comments.indexOf(item);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.itemlist_post_comments, null);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text_User = (TextView) rowView.findViewById(R.id.comment_user);
            viewHolder.text_Date = (TextView) rowView.findViewById(R.id.comment_date);
            viewHolder.text_Message = (TextView) rowView.findViewById(R.id.comment_message);
            rowView.setTag(viewHolder);
            rowView.setClickable(false);
        }

        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

        CommentsDTO comment = getItem(position);
        if (comment != null) {
            holder.comment = comment;

            holder.text_User.setText(comment.user_name);
            holder.text_Date.setText(comment.getCreatedAt());
            holder.text_Message.setText(comment.content);

        }
        return rowView;
    }

    public static class ViewHolder {
        public CommentsDTO comment;
        public TextView text_User;
        public TextView text_Date;
        public TextView text_Message;
    }

}
