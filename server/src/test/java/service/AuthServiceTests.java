package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {
    private DataAccess dataAccess;
    private AuthService authService;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        authService = new AuthService(dataAccess);

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

//

    @Test
    @Order(1)
    @DisplayName("Create Auth")
    public void createAuthValid() throws DataAccessException {
        AuthData authData = authService.createAuth("alice");
        assertEquals(dataAccess.getAuth(authData.authToken()), authService.getAuth(authData.authToken()));
    }

    @Test
    @Order(2)
    @DisplayName("Get Auth")
    public void getAuthValid() throws DataAccessException {
        AuthData authData = authService.getAuth("alice-token");
        assertEquals(dataAccess.getAuth(authData.authToken()), authService.getAuth(authData.authToken()));
    }

    @Test
    @Order(3)
    @DisplayName("Get Auth Invalid")
    public void getAuthNull() throws DataAccessException {
        assertNull(authService.getAuth("john-token"));
    }


}
