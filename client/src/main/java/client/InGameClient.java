package client;

import server.ServerFacade;

public class InGameClient implements Client {
    private final ServerFacade facade;

    public InGameClient(ServerFacade facade) {
        this.facade = facade;
    }

    @Override
    public String help() {
        return """
                 - no commands available, gameplay not implemented yet
                """;
    }
}
