package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Map;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    public String toString() {
        return new Gson().toJson(this);
    }

    public String gameIDJson() {
        return new Gson().toJson(Map.of("gameID", gameID));
    }
}