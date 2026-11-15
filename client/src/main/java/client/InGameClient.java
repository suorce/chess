package client;

import server.ServerFacade;

public class InGameClient {
    private final ServerFacade facade;

    public InGameClient(ServerFacade facade) {
        this.facade = facade;
    }

    public State run() {
        return State.POSTLOGIN;
    }
}
