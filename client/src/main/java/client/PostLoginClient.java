package client;

import server.ServerFacade;

public class PostLoginClient implements Client {
    private final ServerFacade facade;

    public PostLoginClient(ServerFacade facade) {
        this.facade = facade;
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
                 - help - see available commands
                """;
    }
}
