package model;

import com.google.gson.Gson;

import java.util.List;

public record ListGamesResponse (List<GameListItem> games)  {
    public String toString() {
        return new Gson().toJson(this);
    }
}
