package service;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTests extends BaseServiceTests {

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
