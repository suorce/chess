package model;

import com.google.gson.Gson;

public record UserData(String username, String password, String email) {

    public LoginRequest loginData() {
        return new LoginRequest(username, password);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
};