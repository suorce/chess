package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        String username = userData.username();

        if (getUser(username) != null) {
            throw new DataAccessException(DataAccessException.Code.AlreadyTaken);
        }
        dataAccess.createUser(userData);
        return createAuth(username);
    }

    public AuthData loginUser(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData userData = getUser(username);

        if (userData == null || !userData.password().equals(password)) {
            throw new DataAccessException(DataAccessException.Code.Unauthorized);
        }

        return createAuth(username);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        AuthData authData = getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException(DataAccessException.Code.Unauthorized);
        }
        deleteAuth(authToken);
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
