package solutions.plural.qoala.Modelos;

import com.google.gson.Gson;

/**
 * Created by gabri on 29/09/2016.
 */

public class CommentsDTO {

    public int id_comment;;
    public String content;
    public String id_post;
    //public String id_user;
    //public String created_at;
    //public String approved_at;

    public static CommentsDTO fromJson(String json) {
        return new Gson().fromJson(json, CommentsDTO.class );
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
