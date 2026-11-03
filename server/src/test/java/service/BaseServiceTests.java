package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.sql.MySqlDataAccess;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.mindrot.jbcrypt.BCrypt;

public abstract class BaseServiceTests {

    protected DataAccess dataAccess;
    protected AuthService authService;

    @BeforeEach
    void startServer() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();

        authService = new AuthService(dataAccess);

        createTestUsers();
        createTestGames();
    }

    private void createTestUsers() throws DataAccessException {
        createUser("alice", "password123", "alice@email.com", "alice-token");
        createUser("bob", "password456", "bob@email.com", "bob-token");
        createUser("eve", "password789", "eve@email.com", "eve-token");
        createUser("ferb", "password000", "ferb@email.com", "ferb-token");
    }

    private void createUser(String username, String password, String email, String token) throws DataAccessException {
        String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
        dataAccess.createUser(new UserData(username, passwordHash, email));
        dataAccess.createAuth(new AuthData(username, token));
    }

    private void createTestGames() throws DataAccessException {
        dataAccess.createGame(new GameData(1, "alice", "bob", "game1", new ChessGame()));
        dataAccess.createGame(new GameData(2, "eve", null, "game2", new ChessGame()));
    }
}