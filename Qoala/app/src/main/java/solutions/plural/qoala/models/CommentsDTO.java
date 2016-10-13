package solutions.plural.qoala.models;

import com.google.gson.Gson;

/**
 * Created by gabri on 29/09/2016.
 */
public class CommentsDTO {

    public int id_comment;
    public String content;
    public String id_post;
    public String user_name;
    //public String id_user;
    //public String created_at;
    public String created_at;
    //public String approved_at;

    public static CommentsDTO fromJson(String json) {
        return new Gson().fromJson(json, CommentsDTO.class);
    }

    public String getCreatedAt() {
        // 0123456789012345
        // YYYY-MM-DDTHH:MM
        if (created_at == null)
            return "-";

        return (created_at.substring(8, 10)
                + "/" +
                created_at.substring(5, 7)
                + "/" +
                created_at.substring(0, 4)
                + " " +
                created_at.substring(11, 16));
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}
