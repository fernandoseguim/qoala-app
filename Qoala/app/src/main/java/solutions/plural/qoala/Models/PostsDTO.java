package solutions.plural.qoala.models;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gabri on 29/09/2016.
 */
public class PostsDTO implements Serializable {

    public int id_post;
    public String title;
    public String content;
    public String published_at;
    public String id_user;

    public String getContentPartial() {
        if (content.length() > 25)
            return content.substring(0, 25).concat("(...)");
        else
            return content;
    }

    public String getPublishedAt() {
        // 0123456789012345
        // YYYY-MM-DDTHH:MM
        return String.format(published_at.substring(8, 10)
                + "/" +
                published_at.substring(5, 7)
                + "/" +
                published_at.substring(0, 4)
                + " " +
                published_at.substring(11, 16));
    }

    public List<CommentsDTO> comments;

    public static PostsDTO fromJson(String json) {
        return new Gson().fromJson(json, PostsDTO.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
