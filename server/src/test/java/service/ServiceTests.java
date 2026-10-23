package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceTests {
    private DataAccess dataAccess;
    private Service service;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        service = new Service(dataAccess);

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
    @DisplayName("Clear database, then login")
    public void clearDatabaseThenLogin() throws DataAccessException {
        service.clear();
        DataAccessException e = assertThrows(DataAccessException.class, () -> service.loginUser(new LoginRequest("alice", "password123")));
        assertEquals(DataAccessException.Code.Unauthorized, e.code());
    }

    @Test
    @Order(2)
    @DisplayName("Clear database, then list games")
    public void clearDatabaseThenListGames() throws DataAccessException {
        service.clear();
        DataAccessException f = assertThrows(DataAccessException.class, () -> service.listGames("ferb-token"));
        assertEquals(DataAccessException.Code.Unauthorized, f.code());
    }
}
