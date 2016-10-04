package solutions.plural.qoala.models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by gabri on 29/09/2016.
 */
public class BlogDTO {

    public List<PostsDTO> posts;

    public Pagination pagination;

    public static BlogDTO fromJson(String json) { return new Gson().fromJson(json, BlogDTO.class ); }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
