package client;

import server.ServerFacade;

public class Repl {
    private final ServerFacade facade;
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final InGameClient inGameClient;
    private State state = State.PRELOGIN;

    public Repl(String serverUrl) {
        facade = new ServerFacade(serverUrl);
        preLoginClient = new PreLoginClient(facade);
        postLoginClient = new PostLoginClient(facade);
        inGameClient = new InGameClient(facade);
    }

    public void run() {
        System.out.println(" Welcome to the Chess game. Type `help` to start.");
        while (true) {
            switch (state) {
                case QUIT -> { return; }
                case PRELOGIN -> state = preLoginLoop();
                case POSTLOGIN -> state = postLoginLoop();
                case INGAME -> state = inGameLoop();
            }
        }
    }

    private State preLoginLoop() {
        while (state == State.PRELOGIN) {

        }
        return state;
    }

    private State postLoginLoop() {
        while (state == State.POSTLOGIN) {

        }
        return state;
    }

    private State inGameLoop() {
        while (state == State.INGAME) {

        }
        return state;
    }

}
