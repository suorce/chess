package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.LoginRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests extends BaseServiceTests {
    private UserService userService;

    @BeforeEach
    void startUserService() throws DataAccessException {
        userService = new UserService(dataAccess, authService);
    }

    @Test
    @Order(1)
    @DisplayName("Get User")
    public void getUserValid() throws DataAccessException {
        assertEquals("alice", userService.getUser("alice").username());
        assert(BCrypt.checkpw("password123", userService.getUser("alice").password()));
        assertEquals("alice@email.com", userService.getUser("alice").email());
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
        AuthData authData2 = authService.getAuth(authData.authToken());
        assertEquals(authData, authData2);
    }

    @Test
    @Order(4)
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

    @Test
    @Order(7)
    @DisplayName("Logout user")
    public void logoutUser() throws DataAccessException {
        authService.deleteAuth("alice-token");
        assertNull(authService.getAuth("alice-token"));
    }

    @Test
    @Order(8)
    @DisplayName("Logout with fake token")
    public void logoutFakeToken() throws DataAccessException {
        DataAccessException e = assertThrows(DataAccessException.class, () -> authService.deleteAuth("john-token"));
        assertEquals(DataAccessException.Code.Unauthorized, e.code());
    }

}
