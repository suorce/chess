package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private DataAccess dataAccess;
    private GameService gameService;
    private AuthService authService;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        authService = new AuthService(dataAccess);
        gameService = new GameService(dataAccess, authService);

        dataAccess.createUser(new UserData("alice", "password123", "alice@email.com"));
        dataAccess.createAuth(new AuthData("alice", "alice-token"));

        dataAccess.createUser(new UserData("bob", "password456", "bob@email.com"));
        dataAccess.createAuth(new AuthData("bob", "bob-token"));

        dataAccess.createUser(new UserData("eve", "password789", "eve@email.com"));
        dataAccess.createAuth(new AuthData("eve", "eve-token"));

        dataAccess.createUser(new UserData("ferb", "password000", "ferb@email.com"));
        dataAccess.createAuth(new AuthData("ferb", "ferb-token"));

        dataAccess.createGame(new GameData(1, "alice", "bob", "game1", new ChessGame()));
        dataAccess.createGame(new GameData(2, "eve", null, "game2", new ChessGame()));
    }

    @Test
    @Order(1)
    @DisplayName("Get Game")
    public void getGameValid() throws DataAccessException {
        assertEquals(new GameData(1, "alice", "bob", "game1", new ChessGame()), gameService.getGame(1));
    }

    @Test
    @Order(2)
    @DisplayName("Get nonexistent game")
    public void getGameGameIDInvalid() throws DataAccessException {
        assertNull(gameService.getGame(3));
    }

    @Test
    @Order(3)
    @DisplayName("List Games")
    public void listGamesValid() throws DataAccessException {
        List<GameListItem> gamesList = new ArrayList<GameListItem>();
        gamesList.add(new GameListItem(1, "alice", "bob", "game1"));
        gamesList.add(new GameListItem(2, "eve", null, "game2"));
        assertEquals(new ListGamesResponse(gamesList), gameService.listGames("alice-token"));
    }

    @Test
    @Order(4)
    @DisplayName("List Games Unauthorized")
    public void listGamesUnauthorized() throws DataAccessException {
        DataAccessException e = assertThrows(DataAccessException.class, () -> gameService.listGames("john-token"));
        assertEquals(DataAccessException.Code.Unauthorized, e.code());
    }

    @Test
    @Order(5)
    @DisplayName("Update Game")
    public void updateGameValid() throws DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 2);
        GameData gameData = gameService.updateGame("ferb-token", joinGameRequest);

        assertEquals(new GameData(2, "eve", "ferb", "game2", new ChessGame()), gameService.getGame(2));
    }

    @Test
    @Order(6)
    @DisplayName("Update Game Already Taken")
    public void updateGameColorTaken() throws DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 1);
        DataAccessException e = assertThrows(DataAccessException.class, () -> gameService.updateGame("ferb-token", joinGameRequest));
        assertEquals(DataAccessException.Code.AlreadyTaken, e.code());
    }

    @Test
    @Order(7)
    @DisplayName("Update Nonexistent Game")
    public void updateNonexistantGame() throws DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("BLACK", 3);
        DataAccessException e = assertThrows(DataAccessException.class, () -> gameService.updateGame("ferb-token", joinGameRequest));
        assertEquals(DataAccessException.Code.BadRequest, e.code());
    }

    @Test
    @Order(8)
    @DisplayName("Update Nonexistent Game")
    public void updateGameNonexistentColor() throws DataAccessException {
        JoinGameRequest joinGameRequest = new JoinGameRequest("GREEN", 3);
        DataAccessException e = assertThrows(DataAccessException.class, () -> gameService.updateGame("ferb-token", joinGameRequest));
        assertEquals(DataAccessException.Code.BadRequest, e.code());
    }
}
