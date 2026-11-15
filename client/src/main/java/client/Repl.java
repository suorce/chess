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
                case PRELOGIN -> preLoginLoop();
                case POSTLOGIN -> postLoginLoop();
                case INGAME -> inGameLoop();
            }
        }
    }

    private void preLoginLoop() {
        while (state == State.PRELOGIN) {

        }
    }

    private void postLoginLoop() {
        while (state == State.POSTLOGIN) {

        }
    }

    private void inGameLoop() {
        while (state == State.INGAME) {

        }
    }

}
