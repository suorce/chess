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

    // listGames

    @Test
    @DisplayName("List Games")
    public void listGames() throws DataAccessException {
        facade.login(new LoginRequest("alice", "password123"));

        GameData.GameIDResponse gameId1 = facade.createGame(new CreateGameRequest("game1"));
        GameData.GameIDResponse gameId2 = facade.createGame(new CreateGameRequest("game2"));

        ListGamesResponse response = facade.listGames();

        // verify list size, position of gameID, name
        assertEquals(2, response.games().size());
        assertEquals(gameId1.gameID(), response.games().get(0).gameID());
        assertEquals(gameId2.gameID(), response.games().get(1).gameID());
        assertEquals("game1", response.games().get(0).gameName());
        assertEquals("game2", response.games().get(1).gameName());
    }

    @Test
    @DisplayName("List Games Without Auth")
    public void listGamesUnauthorized() {
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> facade.listGames()
        );
        assertEquals(DataAccessException.Code.Unauthorized, ex.code());
    }

    // createGame

    @Test
    @DisplayName("Create Game")
    public void createGame() throws DataAccessException {
        facade.login(new LoginRequest("alice", "password123"));
        GameData.GameIDResponse response = facade.createGame(
                new CreateGameRequest("game1")
        );
        assertTrue(response.gameID() > 0);  // verify gameID returned

        ListGamesResponse games = facade.listGames();   // verify list size, gameID, name
        assertEquals(1, games.games().size());
        assertEquals(response.gameID(), games.games().get(0).gameID());
        assertEquals("game1", games.games().get(0).gameName());
    }

    @Test
    @DisplayName("Create Game Without Authentication")
    public void createGameUnauthorized() {
        DataAccessException ex = assertThrows(
                DataAccessException.class,
                () -> facade.createGame(new CreateGameRequest("game1"))
        );
        assertEquals(DataAccessException.Code.Unauthorized, ex.code());
    }
}