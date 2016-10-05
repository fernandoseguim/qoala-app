package solutions.plural.qoala.models;

import com.google.gson.Gson;

import solutions.plural.qoala.utils.UserPermission.Permission;

public class UserDTO {

    public int id_user;
    public String email;
    public String name;
    @Permission
    public int permission;

    public String address;
    public String district;
    public String city;
    public String state;
    public String zipcode;

    public static UserDTO fromJson(String json) {
        return new Gson().fromJson(json, UserDTO.class);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}