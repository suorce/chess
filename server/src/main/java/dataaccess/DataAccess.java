package dataaccess;

import model.*;

public interface DataAccess {

    void clear() throws DataAccessException;

    UserData createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    GameData createGame(GameData gameData) throws DataAccessException;
//
//    GameData getGame() throws DataAccessException;
//
//    void listGames() throws DataAccessException;
//
//    void updateGame() throws DataAccessException;
//
    AuthData createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;

}
