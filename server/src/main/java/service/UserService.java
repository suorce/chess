package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;
    private final AuthService authService;

    public UserService(DataAccess dataAccess, AuthService authService) {
        this.dataAccess = dataAccess;
        this.authService = authService;
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        String username = userData.username();

        if (getUser(username) != null) {
            throw new DataAccessException(DataAccessException.Code.AlreadyTaken);
        }
        dataAccess.createUser(userData);
        return authService.createAuth(username);
    }

    public AuthData loginUser(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        String password = loginRequest.password();
        UserData userData = getUser(username);

        if (userData == null || !userData.password().equals(password)) {
            throw new DataAccessException(DataAccessException.Code.Unauthorized);
        }

        return authService.createAuth(username);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        authService.requireAuth(authToken);
        authService.deleteAuth(authToken);
    }

    public UserData getUser(String username) throws DataAccessException {
        return dataAccess.getUser(username);
    }

}
