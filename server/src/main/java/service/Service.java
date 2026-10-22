package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.UUID;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() throws DataAccessException {
        dataAccess.clear();
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        if (getUser(username) != null) {
            throw new DataAccessException("username already taken");
        }
        dataAccess.createUser(userData);
        return createAuth(username);
    }

    public UserData getUser(String username) throws DataAccessException {
        return dataAccess.getUser(username);
    }

    private AuthData createAuth(String username) throws DataAccessException {
        String authToken = generateToken();
        AuthData authData = new AuthData(username, authToken);
        return dataAccess.createAuth(authData);
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
