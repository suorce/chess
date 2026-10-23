package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private int gameID = 1;
    private final DataAccess dataAccess;
    private final AuthService authService;

    public GameService(DataAccess dataAccess, AuthService authService) {
        this.dataAccess = dataAccess;
        this.authService = authService;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return dataAccess.getGame(gameID);
    }

    public ListGamesResponse listGames(String authToken) throws DataAccessException {
        authService.requireAuth(authToken);
        List<GameData> games = dataAccess.listGames();
        List<GameListItem> gamesList = new ArrayList<>();
        for (GameData game : games) {
            gamesList.add(game.toListItem());
        }
        return new ListGamesResponse(gamesList);
    }

    public GameData updateGame(String authToken, JoinGameRequest joinGameRequest) throws DataAccessException {
        AuthData authData = authService.requireAuth(authToken);

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
        authService.requireAuth(authToken);
        GameData gameData = new GameData(gameID++, null, null, gameName, new ChessGame());
        return dataAccess.createGame(gameData);
    }

//    ONLY FOR TESTING

    public void incrementGameIDForTesting() {
        gameID++;
    }

}
