package dataaccess;

import chess.ChessGame;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, AuthData> auth = new HashMap<>();
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int gameID = 1;

    public void clear() {
        users.clear();
        auth.clear();
        games.clear();
        gameID = 1;
    }

    public UserData createUser(UserData userData) {
        String username = userData.username();
        users.put(username, userData);
        return userData;
    }

    public UserData getUser(String username) {
            return users.get(username);
    }

    public AuthData createAuth(AuthData authData) {
        String authToken = authData.authToken();
        auth.put(authToken, authData);
        return authData;
    }

    public AuthData getAuth(String authToken) {
        return auth.get(authToken);
    }

    public void deleteAuth(String authToken) {
        auth.remove(authToken);
    }

    public GameData getGame(int gameID) {
        return games.get(gameID);
    }

    public ArrayList<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    public GameData updateGame(String playerName, String playerColor, int gameID) {
        GameData game = games.get(gameID);
        GameData updatedGame = game.updateGame(playerName, playerColor);
        games.put(gameID, updatedGame);
        return updatedGame;
    }

    public GameData createGame(String gameName) {
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, gameData);
        gameID += 1;
        return gameData;
    }

}
