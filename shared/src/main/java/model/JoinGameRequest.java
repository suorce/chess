package model;

import com.google.gson.Gson;

public record JoinGameRequest (String playerColor, int gameID) {
    public String toString() {
        return new Gson().toJson(this);
    }
}