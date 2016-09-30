package solutions.plural.qoala.Models;

import com.google.gson.Gson;

import solutions.plural.qoala.Models.UserPermission.Permission;

public class UserDTO {

    public int id_user;
    public String email;
    public String name;
    @Permission
    public int permission;

    public static UserDTO fromJson(String json) {
        return new Gson().fromJson(json, UserDTO.class);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}