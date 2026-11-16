package client;

import dataaccess.DataAccessException;
import model.*;

public class PreLoginClient implements Client {
    private final ServerFacade facade;

    public PreLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    public String register(String[] tokens) throws DataAccessException {
        if (tokens.length != 4) {
            throw new DataAccessException(DataAccessException.Code.BadRequest);
        }
        UserData userData = new UserData(tokens[1], tokens[2], tokens[3]);
        AuthData authData = facade.register(userData);
        return "Logged in as " + authData.username();
    }

    public String login(String[] tokens) throws DataAccessException {
        if (tokens.length != 3) {
            throw new DataAccessException(DataAccessException.Code.BadRequest);
        }
        LoginRequest loginRequest = new LoginRequest(tokens[1], tokens[2]);
        AuthData authData = facade.login(loginRequest);
        return "Logged in as " + authData.username();
    }

    @Override
    public void printPrompt() {
        System.out.print("\n" + "PRELOGIN >>> ");
    }

    @Override
    public String help() {
        return """
                 - register <USERNAME> <PASSWORD> <EMAIL> - an account
                 - login <USERNAME> <PASSWORD> - to existing account
                 - quit - program
                 - help - see available commands""";
    }
}
