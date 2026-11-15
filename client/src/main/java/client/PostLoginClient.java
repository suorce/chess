package client;

import server.ServerFacade;

public class PostLoginClient {
    private final ServerFacade facade;

    public PostLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    public State run() {
        return State.PRELOGIN;
    }
}
