package client;

import dataaccess.DataAccessException;

import java.util.Scanner;

public class Repl {
    private final ServerFacade facade;
    private final PreLoginClient preLoginClient;
    private final PostLoginClient postLoginClient;
    private final InGameClient inGameClient;
    private State state = State.PRELOGIN;
    private String perspective;

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
        try {
            while (state == State.PRELOGIN) {
                String[] tokens = getInput(scanner, preLoginClient);
                switch (tokens[0]) {
                    case "register" -> {
                        System.out.println(preLoginClient.register(tokens));
                        state = State.POSTLOGIN;
                    }
                    case "login" -> {
                        System.out.println(preLoginClient.login(tokens));
                        state = State.POSTLOGIN;
                    }
                    case "quit" -> state = State.QUIT;
                    default -> System.out.println(preLoginClient.help());
                }
            }
        } catch (DataAccessException ex){
            System.out.println(ex.message());
        }
    }

    private void postLoginLoop(Scanner scanner) {
        try {
            postLoginClient.refreshGamesList(); // show games list and populate PostLoginClient.listToId
            while (state == State.POSTLOGIN) {
                String[] tokens = getInput(scanner, postLoginClient);
                switch (tokens[0]) {
                    case "create" -> System.out.println(postLoginClient.createGame(tokens));
                    case "list" -> System.out.println(postLoginClient.listGames());
                    case "join" -> {
                        System.out.println(postLoginClient.joinGame(tokens));
                        perspective = tokens[2];
                        state = State.INGAME;
                    }
                    case "observe" -> {
                        System.out.println(postLoginClient.observeGame(tokens));
                        perspective = "WHITE";
                        state = State.INGAME;
                    }
                    case "logout" -> {
                        System.out.println(postLoginClient.logout());
                        state = State.PRELOGIN;
                    }
                    case "quit" -> state = State.QUIT;
                    default -> System.out.println(postLoginClient.help());
                }
            }
        } catch (DataAccessException ex){
            System.out.println(ex.message());
        }
    }

    private void inGameLoop(Scanner scanner) {
        System.out.println(inGameClient.drawBoard(perspective));
        while (state == State.INGAME) {
            String[] tokens = getInput(scanner, inGameClient);
            switch (tokens[0]) {
                case "draw" -> System.out.println(inGameClient.drawBoard(perspective));
                case "exit" -> {
                    perspective = null;
                    state = State.POSTLOGIN;
                }
                case "quit" -> state = State.QUIT;
                default -> System.out.println(inGameClient.help());
            }
        }
    }

    private String[] getInput(Scanner scanner, Client client) {
        client.printPrompt();
        String input = scanner.nextLine();
        return input.split(" ");
    }

}
