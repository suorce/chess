package client;

import dataaccess.DataAccessException;
import model.*;
import java.util.HashMap;
import java.util.List;

public class PostLoginClient implements Client {
    private final ServerFacade facade;
    private HashMap<Integer, Integer> listToId = new HashMap<>();

    public PostLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    public String createGame(String[] tokens) throws DataAccessException {
        if (tokens.length != 2) {
            throw new DataAccessException(DataAccessException.Code.BadRequest);
        }
        CreateGameRequest createGameRequest = new CreateGameRequest(tokens[1]);
        facade.createGame(createGameRequest);
        refreshGamesList();
        return "Game created with name = " + tokens[1];
    }

    public String listGames() throws DataAccessException {
        StringBuilder result = new StringBuilder();
        List<GameListItem> games = refreshGamesList();
        int listNum = 1;

        for (GameListItem game : games) {
            result.append("\n   ")
                    .append(listNum++)
                    .append(". ")
                    .append(game.gameName())
                    .append(": ")
                    .append(game.whiteUsername())
                    .append(" vs. ")
                    .append(game.blackUsername());
        }
        return result.toString();
    }

    private List<GameListItem> refreshGamesList() throws DataAccessException {
        listToId.clear();
        int listNum = 1;
        ListGamesResponse listGamesResponse = facade.listGames();
        for (GameListItem game : listGamesResponse.games()) {
            listToId.put(listNum++, game.gameID());
        }
        return listGamesResponse.games();
    }

    public String joinGame(String[] tokens) throws DataAccessException {
        if (tokens.length != 3) {
            throw new DataAccessException(DataAccessException.Code.BadRequest);
        }
        if (!listToId.containsKey(Integer.parseInt(tokens[1]))) {
            throw new DataAccessException(DataAccessException.Code.BadRequest);
        }
        int gameID = listToId.get(Integer.parseInt(tokens[1]));
        JoinGameRequest joinGameRequest = new JoinGameRequest(tokens[2], gameID);
        facade.joinGame(joinGameRequest);
        return "Successfully joined game " + gameID + " as " + tokens[2];
    }

    public String observeGame(String[] tokens) {
        return "work on this later";
    }

    public String logout() throws DataAccessException {
        facade.logout();
        return "Logout Successful";
    }

    @Override
    public void printPrompt() {
        System.out.print("\n" + "POSTLOGIN >>> ");
    }

    @Override
    public String help() {
        return """
                 - create <NAME> - a chess game
                 - list - games
                 - join <ID> [WHITE|BLACK] - a game
                 - observe <ID> - a game
                 - logout - of current session
                 - help - see available commands""";
    }
}
