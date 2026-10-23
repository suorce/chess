package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.UUID;

public class AuthService {
    private final DataAccess dataAccess;

    public AuthService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = generateToken();
        AuthData authData = new AuthData(username, authToken);
        return dataAccess.createAuth(authData);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return dataAccess.getAuth(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        dataAccess.deleteAuth(authToken);
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
