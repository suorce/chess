package service;

import dataaccess.DataAccessException;
import model.LoginRequest;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ServiceTests extends BaseServiceTests {
    private Service service;

    @BeforeEach
    void startService() throws DataAccessException {
        service = new Service(dataAccess);
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
