package client;

import dataaccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private static String serverUrl;

    private ServerFacade facade;
    private ServerFacade bobFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverUrl = "http://localhost:" + port;
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        facade = new ServerFacade(serverUrl);
        facade.clear();

        facade.register(new UserData("alice", "password123", "alice@email.com"));
        facade.logout();

        bobFacade = new ServerFacade(serverUrl);
        bobFacade.register(new UserData("bob", "password456", "bob@email.com"));
        bobFacade.logout();
    }

    @Test
    @DisplayName("Clear database")
    public void clear() throws DataAccessException {
        facade.login(new LoginRequest("alice", "password123"));
        facade.clear();

        // clear should remove Alice's existing auth
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> facade.listGames()
        );
        assertEquals(DataAccessException.Code.Unauthorized, ex.code());
    }

    @Test
    @DisplayName("Register User")
    public void register() throws DataAccessException {
        AuthData authData = facade.register(
                new UserData("eve", "password789", "eve@email.com")
        );
        assertEquals("eve", authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    @DisplayName("Register Existing User")
    public void registerExistingUser() {
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> facade.register(
                        new UserData("alice", "password123", "alice2@email.com")
                )
        );
        assertEquals(DataAccessException.Code.AlreadyTaken, ex.code());
    }
}