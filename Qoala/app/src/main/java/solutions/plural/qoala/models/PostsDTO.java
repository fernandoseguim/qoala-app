package solutions.plural.qoala.Models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by gabri on 29/09/2016.
 */

public class PostsDTO {

    public int id_post;
    public String title;
    public String content;
    public String published_at;
    public String id_user;

    public List<CommentsDTO> comments;

    public static PostsDTO fromJson(String json) {
        return new Gson().fromJson(json, PostsDTO.class );
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
