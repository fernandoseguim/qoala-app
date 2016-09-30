package solutions.plural.qoala.Models;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by gabri on 29/09/2016.
 */

public class BlogDTO {

    public List<PostsDTO> posts;

    public int total_number_pages;
    public boolean next_page;
    public int current_page;
    public boolean previous_page;

    public static BlogDTO fromJson(String json) {
        return new Gson().fromJson(json, BlogDTO.class );
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
