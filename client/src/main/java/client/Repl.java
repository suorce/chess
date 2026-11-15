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
        inGameClient = new inGameClient(facade);
    }

    public void run() {
        state = preLoginClient.run();
        while (true) {
            switch (state) {
                case QUIT -> { return; }
                case PRELOGIN -> state = preLoginClient.run();
                case POSTLOGIN -> state = postLoginClient.run();
                case INGAME -> state = inGameClient.run();
            }
        }
    }

}
