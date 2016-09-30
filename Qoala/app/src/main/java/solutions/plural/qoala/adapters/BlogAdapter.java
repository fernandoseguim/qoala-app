package solutions.plural.qoala.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import solutions.plural.qoala.R;
import solutions.plural.qoala.Models.BlogDTO;
import solutions.plural.qoala.Models.PostsDTO;

/**
 * Created by gabri on 29/09/2016.
 */

public class BlogAdapter extends ArrayAdapter<PostsDTO> {

    private final Activity context;
    private final BlogDTO blog;

    static class ViewHolder {
        public TextView text_Id;
        public TextView text_Content;
    }

    public BlogAdapter(Activity context, BlogDTO blog) {
        super(context, R.layout.fragment_post, blog.posts);
        this.context = context;
        this.blog = blog;
    }

    @Override
    public int getCount() {
        return blog.posts.size();
    }

    @Nullable
    @Override
    public PostsDTO getItem(int position) {
        return blog.posts.get(position);
    }

    @Override
    public int getPosition(PostsDTO item) {
        return blog.posts.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.fragment_post, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text_Id = (TextView) rowView.findViewById(R.id.post_id);
            viewHolder.text_Content = (TextView) rowView.findViewById(R.id.post_content);
            rowView.setTag(viewHolder);

        }
        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

        PostsDTO post = getItem(position);
        holder.text_Id.setText(String.valueOf(post.id_post));
        holder.text_Content.setText(post.content);

        return rowView;
    }

}
