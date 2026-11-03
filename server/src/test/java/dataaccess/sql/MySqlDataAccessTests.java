package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MySqlDataAccess;
import model.*;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MySqlDataAccessTests {

    private MySqlDataAccess dataAccess;

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
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

    @Test
    @Order(1)
    @DisplayName("Clear database, then get user")
    public void clearDatabaseThenGetUser() throws DataAccessException {
        dataAccess.clear();
        assertNull(dataAccess.getUser("alice"));
    }

    @Test
    @Order(2)
    @DisplayName("Clear database, then list games")
    public void clearDatabaseThenListGames() throws DataAccessException {
        dataAccess.clear();
        assertTrue(dataAccess.listGames().isEmpty());
    }

    @Test
    @Order(3)
    @DisplayName("Create User")
    public void createUser() throws DataAccessException {
        String passwordHashGurt = BCrypt.hashpw("passwordGURT", BCrypt.gensalt());
        UserData userData = new UserData("gurt", passwordHashGurt, "gurt@email.com");
        dataAccess.createUser(userData);
        assertEquals(userData, dataAccess.getUser("gurt"));
    }

    @Test
    @Order(4)
    @DisplayName("Create Existing User")
    public void createExistingUser() throws DataAccessException {
        String passwordHashAlice = BCrypt.hashpw("password123", BCrypt.gensalt());
        UserData userData = new UserData("alice", passwordHashAlice, "alice@email.com");
        DataAccessException f = assertThrows(DataAccessException.class, () -> dataAccess.createUser(userData));
        assertEquals(DataAccessException.Code.ServerError, f.code());   // AlreadyTaken (and others) handled in service, this is ServerError because it should NEVER happen
    }

    @Test
    @Order(5)
    @DisplayName("Get User")
    public void getUser() throws DataAccessException {
        UserData userData = dataAccess.getUser("alice");
        assertEquals("alice", userData.username());
        assert(BCrypt.checkpw("password123", userData.password()));
        assertEquals("alice@email.com", userData.email());
    }

    @Test
    @Order(6)
    @DisplayName("Get Nonexistent User")
    public void getNonexistentUser() throws DataAccessException {
        assertNull(dataAccess.getUser("gurt"));
    }

    @Test
    @Order(7)
    @DisplayName("Create Auth")
    public void createAuth() throws DataAccessException {
        AuthData authData = new AuthData("alice", "alice-token-2");
        dataAccess.createAuth(authData);
        assertEquals(authData, dataAccess.getAuth(authData.authToken()));
    }

    @Test
    @Order(8)
    @DisplayName("Create Auth With Duplicate Token")
    public void createAuthDuplicateToken() throws DataAccessException {
        AuthData authData = new AuthData("alice", "alice-token");
        DataAccessException f = assertThrows(DataAccessException.class, () -> dataAccess.createAuth(authData));
        assertEquals(DataAccessException.Code.ServerError, f.code());
    }

    @Test
    @Order(9)
    @DisplayName("Get Auth")
    public void getAuth() throws DataAccessException {
        AuthData authData = new AuthData("alice", "alice-token");
        assertEquals(authData, dataAccess.getAuth("alice-token"));
    }

    @Test
    @Order(10)
    @DisplayName("Get Auth with Fake Token")
    public void getAuthFakeToken() throws DataAccessException {
        assertNull(dataAccess.getAuth("alice-token-fake"));
    }

    @Test
    @Order(11)
    @DisplayName("Delete Auth")
    public void deleteAuth() throws DataAccessException {
        dataAccess.deleteAuth("alice-token");
        assertNull(dataAccess.getAuth("alice-token"));
    }

    @Test
    @Order(12)
    @DisplayName("Delete Nonexistent Auth")
    public void deleteNonexistentAuth() throws DataAccessException {
        dataAccess.deleteAuth("alice-token-fake");
        assertNull(dataAccess.getAuth("alice-token-fake"));
    }

    @Test
    @Order(13)
    @DisplayName("Get Game")
    public void getGame() throws DataAccessException {
        GameData gameData = new GameData(1, "alice", "bob", "game1", new ChessGame());
        assertEquals(gameData, dataAccess.getGame(1));
    }

    @Test
    @Order(14)
    @DisplayName("Get Nonexistent Game")
    public void getNonexistentGame() throws DataAccessException {
        assertNull(dataAccess.getGame(4));
    }

    @Test
    @Order(15)
    @DisplayName("List Games")
    public void listGames() throws DataAccessException {
        var gamesList = new ArrayList<GameData>();
        gamesList.add(new GameData(1, "alice", "bob", "game1", new ChessGame()));
        gamesList.add(new GameData(2, "eve", null, "game2", new ChessGame()));
        assertEquals(gamesList, dataAccess.listGames());
    }

}
