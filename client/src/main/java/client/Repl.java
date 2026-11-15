package client;

import server.ServerFacade;

import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);
        while (true) {
            switch (state) {
                case QUIT -> { return; }
                case PRELOGIN -> preLoginLoop(scanner);
                case POSTLOGIN -> postLoginLoop(scanner);
                case INGAME -> inGameLoop(scanner);
            }
        }
    }

    private void preLoginLoop(Scanner scanner) {
        while (state == State.PRELOGIN) {

        }
    }

    private void postLoginLoop(Scanner scanner) {
        while (state == State.POSTLOGIN) {

        }
    }

    private void inGameLoop(Scanner scanner) {
        while (state == State.INGAME) {

        }
    }

    private String[] getInput(Scanner scanner, Client client) {
        client.printPrompt();
        String input = scanner.nextLine();
        return input.split(" ");
    }

}
