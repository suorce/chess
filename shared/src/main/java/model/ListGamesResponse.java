package model;

import com.google.gson.Gson;

import java.util.Map;

public record ListGamesResponse (Map<Integer, GameData> listGamesResponse) {
    public String toString() {
        return new Gson().toJson(this);
    }
}
