package client;

import server.ServerFacade;

public class PostLoginClient implements Client {
    private final ServerFacade facade;

    public PostLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    @Override
    public State run() {
        return State.PRELOGIN;
    }

    @Override
    public String help() {
        return "placeholder";
    }
}
