package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Service {
    private final DataAccess dataAccess;
    private final UserService userService;
    private final GameService gameService;
    private final AuthService authService;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        this.authService = new AuthService(dataAccess);
        this.userService = new UserService(dataAccess, authService);
        this.gameService = new GameService(dataAccess, authService);
    }

    public void clear() throws DataAccessException {
        dataAccess.clear();
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        return userService.createUser(userData);
    }

    public AuthData loginUser(LoginRequest loginRequest) throws DataAccessException {
        return userService.loginUser(loginRequest);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        userService.logoutUser(authToken);
    }

    public UserData getUser(String username) throws DataAccessException {
        return userService.getUser(username);
    }

    public AuthData createAuth(String username) throws DataAccessException {
        return authService.createAuth(username);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return authService.getAuth(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        authService.deleteAuth(authToken);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return dataAccess.getGame(gameID);
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        return gameService.listGames(authToken);
    }

    public GameData updateGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        return gameService.updateGame(authToken, joinGameRequest);
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        return gameService.createGame(authToken, gameName);
    }
}
