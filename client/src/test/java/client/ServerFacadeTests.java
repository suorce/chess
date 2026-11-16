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

    // clear

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

    // register

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

    // login

    @Test
    @DisplayName("Login User")
    public void login() throws DataAccessException {
        AuthData authData = facade.login(
                new LoginRequest("alice", "password123")
        );
        assertEquals("alice", authData.username());
        assertNotNull(authData.authToken());
        assertNotNull(facade.listGames());  // verify token stored in facade
    }

    @Test
    @DisplayName("Login With Incorrect Password")
    public void loginIncorrectPassword() {
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> facade.login(
                        new LoginRequest("alice", "wrongpassword")
                )
        );

        assertEquals(DataAccessException.Code.Unauthorized, ex.code());
    }

    @Test
    @DisplayName("Login With Nonexistent User")
    public void loginNonexistentUser() {
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> facade.login(
                        new LoginRequest("eve", "password123")
                )
        );
        assertEquals(DataAccessException.Code.Unauthorized, ex.code());
    }

    // logout

    @Test
    @DisplayName("Logout User")
    public void logout() throws DataAccessException {
        facade.login(new LoginRequest("alice", "password123"));
        facade.logout();
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> facade.listGames()
        );
        assertEquals(DataAccessException.Code.Unauthorized, ex.code());
    }
}