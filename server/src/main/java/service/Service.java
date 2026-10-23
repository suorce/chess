package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.Objects;
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

    public AuthData loginUser(LoginData loginData) throws DataAccessException {
        String username = loginData.username();
        String password = loginData.password();

        UserData userData = getUser(username);
        if (userData == null) {
            throw new DataAccessException("unauthorized");
        } else if (!Objects.equals(userData.password(), password)) {
            throw new DataAccessException("unauthorized");
        } else {
            return createAuth(username);
        }
    }

    public void logoutUser(String authToken) throws DataAccessException {
        AuthData authData = getAuth(authToken);

        if (authData == null) {
            throw new DataAccessException("unauthorized");
        } else {
            deleteAuth(authToken);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        return dataAccess.getUser(username);
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
