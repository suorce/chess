package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    final private HashMap<String, UserData> users = new HashMap<>();
    final private HashMap<String, AuthData> auth = new HashMap<>();
    final private HashMap<Integer, GameData> games = new HashMap<>();


    public void clear() {
        users.clear();
        auth.clear();
        games.clear();
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
        games.put(gameID, game.updateGame(playerName, playerColor));
        return game;
    }

    public GameData createGame(GameData gameData) {
        int gameID = gameData.gameID();
        games.put(gameID, gameData);
        return gameData;
    }

}
