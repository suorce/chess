package model;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Map;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

    public GameData updateGame(String playerName, String playerColor) {
        if (playerColor.equals("WHITE")) {
            return new GameData(gameID, playerName, blackUsername, gameName, game);
        } else if (playerColor.equals("BLACK")) {
            return new GameData(gameID, whiteUsername, playerName, gameName, game);
        }
        return this;
    }

    public GameListItem toListItem() {
        return new GameListItem(gameID, whiteUsername, blackUsername, gameName);
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public String gameIDJson() {
        return new Gson().toJson(Map.of("gameID", gameID));
    }
}