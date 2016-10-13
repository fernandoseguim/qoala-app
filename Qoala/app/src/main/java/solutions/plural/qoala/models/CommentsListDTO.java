package solutions.plural.qoala.models;

import com.google.gson.Gson;

import java.util.ArrayList;

public class CommentsListDTO extends ArrayList<CommentsDTO> {

    public static CommentsListDTO fromJson(String json) {
        return new Gson().fromJson(json, CommentsListDTO.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
