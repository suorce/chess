package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.sql.MySqlDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests {
    private DataAccess dataAccess;
    private AuthService authService;

    @BeforeEach
    void startServer() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
        authService = new AuthService(dataAccess);
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

    @Test
    @Order(4)
    @DisplayName("Delete Auth")
    public void deleteAuth() throws DataAccessException {
        authService.deleteAuth("alice-token");
        assertNull(authService.getAuth("alice-token"));
    }

    @Test
    @Order(5)
    @DisplayName("Delete Auth Unauthorized")
    public void deleteAuthInvalid() throws DataAccessException {
        DataAccessException e = assertThrows(DataAccessException.class, () -> authService.deleteAuth("john-token"));
        assertEquals(DataAccessException.Code.Unauthorized, e.code());
    }

    @Test
    @Order(6)
    @DisplayName("Require Auth")
    public void requireAuthValid() throws DataAccessException {
        assertEquals(authService.getAuth("alice-token"), authService.requireAuth("alice-token"));
    }

    @Test
    @Order(7)
    @DisplayName("Require Auth Unauthorized")
    public void requireAuthUnauthorized() throws DataAccessException {
        DataAccessException e = assertThrows(DataAccessException.class, () -> authService.requireAuth("john-token"));
        assertEquals(DataAccessException.Code.Unauthorized, e.code());
    }
}
