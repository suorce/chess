package dataaccess;

import model.*;

public interface DataAccess {

    void clear() throws DataAccessException;

    UserData createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;
//
//    GameData createGame() throws DataAccessException;
//
//    GameData getGame() throws DataAccessException;
//
//    void listGames() throws DataAccessException;
//
//    void updateGame() throws DataAccessException;
//
//    AuthData createAuth() throws DataAccessException;
//
//    AuthData getAuth() throws DataAccessException;
//
//    void deleteAuth() throws DataAccessException;

}
