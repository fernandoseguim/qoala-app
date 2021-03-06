package solutions.plural.qoala.adapters;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import solutions.plural.qoala.R;
import solutions.plural.qoala.models.BlogDTO;
import solutions.plural.qoala.models.PostsDTO;

/**
 * Created by gabri on 29/09/2016.
 */
public class BlogAdapter extends ArrayAdapter<PostsDTO> {

    private final Activity context;
    private BlogDTO blog;

    public void setBlog(@NonNull BlogDTO blog) {
        this.clear();
        this.blog.pagination=blog.pagination;
        this.addAll(blog.posts);
        //notifyDataSetChanged();
    }

    public BlogAdapter(Activity context, BlogDTO blog) {
        super(context, R.layout.itemlist_blog_posts, blog.posts);
        this.context = context;
        this.blog = blog;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id_post;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View rowView = convertView;
        // reuse views
        if (rowView == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.itemlist_blog_posts, null);

            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text_title = (TextView) rowView.findViewById(R.id.post_title);
            viewHolder.text_date = (TextView) rowView.findViewById(R.id.post_date);
            viewHolder.text_content = (TextView) rowView.findViewById(R.id.post_content);
            rowView.setTag(viewHolder);

        }


        // fill data
        ViewHolder holder = (ViewHolder) rowView.getTag();

        PostsDTO post = getItem(position);
        if (post != null) {
            holder.post = post;
            holder.text_title.setText(post.title);
            holder.text_date.setText(post.getPublishedAt());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.text_content.setText(Html.fromHtml(post.content, Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.text_content.setText(Html.fromHtml(post.content));
            }
        }
        return rowView;
    }

    public static class ViewHolder {
        public PostsDTO post;
        TextView text_title;
        TextView text_date;
        TextView text_content;
    }

}
