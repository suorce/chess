package dataaccess;

import model.*;

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

    public UserData createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        users.put(username, userData);
        return userData;
    }

    public UserData getUser(String username) throws DataAccessException {
        if (!users.containsKey(username)) {
            return null;
        } else {
            return users.get(username);
        }
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        String authToken = authData.authToken();
        auth.put(authToken, authData);
        return authData;
    }

}
