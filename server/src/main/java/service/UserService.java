package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
        String passwordHash = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        UserData hashedUser = new UserData(username, passwordHash, userData.email());

        if (getUser(username) != null) {
            throw new DataAccessException(DataAccessException.Code.AlreadyTaken);
        }
        dataAccess.createUser(hashedUser);
        return authService.createAuth(username);
    }

    public AuthData loginUser(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        UserData userData = getUser(username);

        if (userData == null) {
            throw new DataAccessException(DataAccessException.Code.Unauthorized);
        }
        String passwordHash = getUser(username).password();
        if (!BCrypt.checkpw(loginRequest.password(), passwordHash)) {
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
