package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.*;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() throws DataAccessException {
        dataAccess.clear();
    }

    public AuthData createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        if (getUser(username) == null) {
            throw new DataAccessException("username already taken");
        } else {
            dataAccess.createUser(userData);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        return dataAccess.getUser(username);
    }
}
