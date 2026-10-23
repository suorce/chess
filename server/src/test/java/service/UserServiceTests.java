package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private DataAccess dataAccess;
    private UserService userService;
    private AuthService authService;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        authService = new AuthService(dataAccess);
        userService = new UserService(dataAccess, authService);

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
    @DisplayName("Get User")
    public void getUserValid() throws DataAccessException {
        assertEquals(new UserData("alice", "password123", "alice@email.com"), userService.getUser("alice"));
    }

    @Test
    @Order(2)
    @DisplayName("Get Non-existent User")
    public void getNonexistentUser() throws DataAccessException {
        assertNull(userService.getUser("john"));
    }

    @Test
    @Order(3)
    @DisplayName("Register User & return Auth")
    public void registerValid() throws DataAccessException {
        AuthData authData = userService.createUser(new UserData("gurt", "passwordGURT", "gurt@email.com"));
        AuthData authData2 = dataAccess.getAuth(authData.authToken());
        assertEquals(authData, authData2);
    }

    @Test
    @Order(4
    @DisplayName("Register taken username")
    public void registerTakenUser() throws DataAccessException {
        DataAccessException e = assertThrows(DataAccessException.class, () -> userService.createUser(new UserData("alice", "password123", "alice@email.com")));
        assertEquals(DataAccessException.Code.AlreadyTaken, e.code());
    }

    @Test
    @Order(5)
    @DisplayName("Login existing User and return Auth")
    public void loginValid() throws DataAccessException {
        AuthData authData = userService.loginUser(new LoginRequest("alice", "password123"));
        AuthData authData2 = authService.getAuth(authData.authToken());
        assertEquals(authData, authData2);
    }

    @Test
    @Order(6)
    @DisplayName("Login with incorrect password")
    public void loginIncorrectPassword() throws DataAccessException {
        DataAccessException e = assertThrows(DataAccessException.class, () -> userService.loginUser(new LoginRequest("alice", "bad-guess")));
        assertEquals(DataAccessException.Code.Unauthorized, e.code());
    }


}
