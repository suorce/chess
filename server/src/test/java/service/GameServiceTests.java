package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.sql.MySqlDataAccess;
import model.*;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {
    private DataAccess dataAccess;
    private GameService gameService;
    private AuthService authService;

    @BeforeEach
    void startServer() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
        authService = new AuthService(dataAccess);
        gameService = new GameService(dataAccess, authService);
        dataAccess.clear();

        String passwordHashAlice = BCrypt.hashpw("password123", BCrypt.gensalt());
        dataAccess.createUser(new UserData("alice", passwordHashAlice, "alice@email.com"));
        dataAccess.createAuth(new AuthData("alice", "alice-token"));

        String passwordHashBob = BCrypt.hashpw("password456", BCrypt.gensalt());
        dataAccess.createUser(new UserData("bob", passwordHashBob, "bob@email.com"));
        dataAccess.createAuth(new AuthData("bob", "bob-token"));

        String passwordHashEve = BCrypt.hashpw("password789", BCrypt.gensalt());
        dataAccess.createUser(new UserData("eve", passwordHashEve, "eve@email.com"));
        dataAccess.createAuth(new AuthData("eve", "eve-token"));

        String passwordHashFerb = BCrypt.hashpw("password000", BCrypt.gensalt());
        dataAccess.createUser(new UserData("ferb", passwordHashFerb, "ferb@email.com"));
        dataAccess.createAuth(new AuthData("ferb", "ferb-token"));

        dataAccess.createGame(new GameData(1, "alice", "bob", "game1", new ChessGame()));
        dataAccess.createGame(new GameData(2, "eve", null, "game2", new ChessGame()));
        gameService.incrementGameIDForTesting();
        gameService.incrementGameIDForTesting();
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

    @Test
    @Order(9)
    @DisplayName("Create Game")
    public void createGameValid() throws DataAccessException {
        GameData gameData = gameService.createGame("ferb-token", "new-game");

        assertEquals(new GameData(3, null, null, "new-game", new ChessGame()), gameService.getGame(3));
    }

    @Test
    @Order(9)
    @DisplayName("Create Game Unauthorized")
    public void createGameNoName() throws DataAccessException {
        DataAccessException e = assertThrows(DataAccessException.class, () -> gameService.createGame("john-token", "new-game"));
        assertEquals(DataAccessException.Code.Unauthorized, e.code());    }
}
