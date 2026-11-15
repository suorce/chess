package client;

import dataaccess.DataAccessException;
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
            try {
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
            } catch (DataAccessException ex){
                System.out.println(ex.message());
            }
        }
    }

    private void postLoginLoop(Scanner scanner) {
        while (state == State.POSTLOGIN) {
            try {
                String[] tokens = getInput(scanner, postLoginClient);
                switch (tokens[0]) {
                    case "create" -> System.out.println(postLoginClient.createGame(tokens));
                    case "list" -> System.out.println(postLoginClient.listGames());
                    case "join" -> {
                        System.out.println(postLoginClient.joinGame(tokens));
                        state = State.INGAME;
                    }
                    case "observe" -> {
                        System.out.println(postLoginClient.observeGame(tokens));
                        state = State.INGAME;
                    }
                    case "logout" -> {
                        System.out.println(postLoginClient.logout());
                        state = State.PRELOGIN;
                    }
                    case "quit" -> state = State.QUIT;
                    default -> System.out.println(postLoginClient.help());
                }
            } catch (DataAccessException ex){
                System.out.println(ex.message());
            }
        }
    }

    private void inGameLoop(Scanner scanner) {
        while (state == State.INGAME) {
            try {
                String[] tokens = getInput(scanner, inGameClient);
                switch (tokens[0]) {
                    case "draw" -> System.out.println(inGameClient.drawBoard());
                    case "exit" -> {
                        System.out.println(inGameClient.exitGame());
                        state = State.POSTLOGIN;
                    }
                    case "quit" -> state = State.QUIT;
                    default -> System.out.println(inGameClient.help());
                }
            } catch (DataAccessException ex){
                System.out.println(ex.message());
            }
        }
    }

    private String[] getInput(Scanner scanner, Client client) {
        client.printPrompt();
        String input = scanner.nextLine();
        return input.split(" ");
    }

}
