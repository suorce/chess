package model;

import com.google.gson.Gson;

import java.util.Collection;
import java.util.Map;

public record ListGamesResponse (Map<String, Collection<GameData>> listGamesResponse) {
    public String toString() {
        return new Gson().toJson(this);
    }
}
