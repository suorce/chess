package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;

public class UserServiceTests {
    private DataAccess dataAccess;
    private UserService userService;
    private AuthService authService;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new MemoryDataAccess();
        authService = new AuthService(dataAccess);
        userService = new UserService(dataAccess, authService);

        dataAccess.createUser(new UserData("alice", "password123", "bob@email.com"));
        dataAccess.createAuth(new AuthData("alice", "alice-token"));

        dataAccess.createUser(new UserData("bob", "password456", "alice@email.com"));
        dataAccess.createAuth(new AuthData("bob", "bob-token"));

        dataAccess.createUser(new UserData("eve", "password789", "eve@email.com"));
        dataAccess.createAuth(new AuthData("eve", "eve-token"));

        dataAccess.createUser(new UserData("ferb", "password000", "ferb@email.com"));
        dataAccess.createAuth(new AuthData("ferb", "ferb-token"));

        dataAccess.createGame(new GameData(1, "alice", "bob", "game1", new ChessGame()));
        dataAccess.createGame(new GameData(2, "eve", null, "game2", new ChessGame()));
    }
}
