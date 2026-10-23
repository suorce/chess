package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Service {
    private int gameID = 1;
    private final DataAccess dataAccess;
    private final UserService userService;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
        this.userService = new UserService(dataAccess);
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
        return userService.createAuth(username);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return userService.getAuth(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        userService.deleteAuth(authToken);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return dataAccess.getGame(gameID);
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        AuthData authData = getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException(DataAccessException.Code.Unauthorized);
        }
        List<GameData> games = dataAccess.listGames();
        List<GameListItem> gamesList = new ArrayList<>();
        for (GameData game : games) {
            gamesList.add(game.toListItem());
        }
        return new ListGamesResponse(gamesList);
    }

    public GameData updateGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        AuthData authData = getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException(DataAccessException.Code.Unauthorized);
        }

        String playerName = authData.username();
        String playerColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();

        if ((!"WHITE".equals(playerColor) && !"BLACK".equals(playerColor)) || getGame(gameID) == null) {
            throw new DataAccessException(DataAccessException.Code.BadRequest);
        }

        GameData game = getGame(gameID);
        if ("WHITE".equals(playerColor) && game.whiteUsername() == null) {
            return dataAccess.updateGame(playerName, playerColor, gameID);
        } else if ("BLACK".equals(playerColor) && game.blackUsername() == null) {
            return dataAccess.updateGame(playerName, playerColor, gameID);
        } else {
            throw new DataAccessException(DataAccessException.Code.AlreadyTaken);
        }
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        AuthData authData = getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException(DataAccessException.Code.Unauthorized);
        }
        GameData gameData = new GameData(gameID++, null, null, gameName, new ChessGame());
        return dataAccess.createGame(gameData);
    }
}
