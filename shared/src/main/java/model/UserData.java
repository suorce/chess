package model;

import com.google.gson.Gson;

public record UserData(String username, String password, String email) {

    public LoginData loginData() {
        return new LoginData(username, password);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
};