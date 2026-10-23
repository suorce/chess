package model;

import com.google.gson.Gson;

public record LoginData(String username, String password) {
    public String toString() {
        return new Gson().toJson(this);
    }
};