package service;

import chess.ChessGame;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import dataaccess.MySqlDataAccess;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;
import passoff.model.*;
import server.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceTests {
    private DataAccess dataAccess;
    private Service service;

    @BeforeEach
    void startServer() throws DataAccessException {
        dataAccess = new MySqlDataAccess();
        service = new Service(dataAccess);
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
