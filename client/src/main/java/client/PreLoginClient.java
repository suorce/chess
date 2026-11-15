package client;

import server.ServerFacade;

public class PreLoginClient {
    private final ServerFacade facade;

    public PreLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    public State run() {
        return State.QUIT;
    }
}
