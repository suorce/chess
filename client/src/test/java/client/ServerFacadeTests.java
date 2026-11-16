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
}