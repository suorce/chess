package client;

import server.ServerFacade;

public class PreLoginClient implements Client {
    private final ServerFacade facade;

    public PreLoginClient(ServerFacade facade) {
        this.facade = facade;
    }

    @Override
    public State run() {
        return State.QUIT;
    }

    @Override
    public String help() {
        return "placeholder";
    }
}
