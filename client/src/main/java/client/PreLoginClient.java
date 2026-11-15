package client;

import server.ServerFacade;

public class PreLoginClient implements Client {
    private final ServerFacade facade;

    public PreLoginClient(ServerFacade facade) {
        this.facade = facade;
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
                 - help - see available commands
                """;
    }
}
