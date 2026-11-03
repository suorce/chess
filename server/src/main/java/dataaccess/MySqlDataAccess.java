package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        String[] clearStatements = {
                "TRUNCATE TABLE users",
                "TRUNCATE TABLE auth",
                "TRUNCATE TABLE games"
        };
        for (String statement : clearStatements) {
            executeUpdate(statement);
        }
    }

    public UserData createUser(UserData userData) throws DataAccessException {
        String statement = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
        executeUpdate(statement, userData.username(), userData.password(), userData.email());
        return userData;
    }

    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("Unable to read data: %s%n", e.getMessage());
            throw new DataAccessException(DataAccessException.Code.ServerError);
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        String username = rs.getString(1);
        String passwordHash = rs.getString(2);
        String email = rs.getString(3);
        return new UserData(username, passwordHash, email);
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        String statement = "INSERT INTO auth (username, authToken) VALUES (?,?)";
        executeUpdate(statement, authData.username(), authData.authToken());
        return authData;
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("Unable to read data: %s%n", e.getMessage());
            throw new DataAccessException(DataAccessException.Code.ServerError);
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        String username = rs.getString(1);
        String authToken = rs.getString(2);
        return new AuthData(username, authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String statement = "DELETE FROM auth WHERE authToken=?";
        executeUpdate(statement, authToken);
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT * FROM games WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    } else {
                        return null;
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("Unable to read data: %s%n", e.getMessage());
            throw new DataAccessException(DataAccessException.Code.ServerError);
        }
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            System.out.printf("Unable to read data: %s", e.getMessage());
            throw new DataAccessException(DataAccessException.Code.ServerError);
        }
        return result;
    }

    public GameData updateGame(String playerName, String playerColor, int gameID) throws DataAccessException {
        String colorUsername;
        if ("WHITE".equals(playerColor)) {
            colorUsername = "whiteUsername";
        } else {
            colorUsername = "blackUsername";
        }
        String statement = String.format("UPDATE games SET %s=? WHERE gameID=?", colorUsername);
        executeUpdate(statement, playerName, gameID);
        return getGame(gameID);
    }

    public GameData createGame(GameData gameData) throws DataAccessException {
        String statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?,?,?,?,?)";
        String gameString = new Gson().toJson(gameData.game(), ChessGame.class);
        executeUpdate(statement, gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameString);
        return gameData;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        int gameID = rs.getInt(1);
        String whiteUsername = rs.getString(2);
        String blackUsername = rs.getString(3);
        String gameName = rs.getString(4);
        String gameString = rs.getString(5);
        ChessGame game = new Gson().fromJson(gameString, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case ChessGame p -> ps.setString(i + 1, p.toString());
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            System.out.println(statement);
            System.out.println(e.getMessage());
            throw new DataAccessException(DataAccessException.Code.ServerError);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
                `username` varchar(256) NOT NULL,
                `password` varchar(256) NOT NULL,
                `email` varchar(256) NOT NULL,
                PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  auth (
                `username` varchar(256) NOT NULL,
                `authToken` varchar(256) NOT NULL,
                PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  games (
                `gameID` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256) DEFAULT NULL,
                `blackUsername` varchar(256) DEFAULT NULL,
                `gameName` varchar(256) NOT NULL,
                `game` TEXT NOT NULL,
                PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(DataAccessException.Code.ServerError); // MAY NEED TO REWORK DATAACCESSEXCEPTION CLASS
        }
    }

}
