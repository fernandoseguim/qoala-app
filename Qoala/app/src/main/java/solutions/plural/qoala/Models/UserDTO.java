package solutions.plural.qoala.Models;

import com.google.gson.Gson;

import solutions.plural.qoala.Models.UserPermission.Permission;

public class UserDTO {

    private int id_user;
    private String email;
    private String name;

    @Permission
    private int permission;

    public static UserDTO fromJson(String json) {
        return new Gson().fromJson(json, UserDTO.class);
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Permission
    public int getPermission() {
        return permission;
    }

    public void setPermission(@Permission int permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Name: " + getName() + " Email: " + getEmail();
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

}