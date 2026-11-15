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
            String[] tokens = getInput(scanner, preLoginClient);
            switch (tokens[0]) {
                case "register" -> state = preLoginClient.register(tokens);
                case "login" -> state = preLoginClient.login(tokens);
                case "quit" -> state = State.QUIT;
                case "help" -> System.out.println(preLoginClient.help());
            }
        }
    }

    private void postLoginLoop(Scanner scanner) {
        while (state == State.POSTLOGIN) {
            String[] tokens = getInput(scanner, postLoginClient);
            switch (tokens[0]) {
                case "create" -> postLoginClient.createGame(tokens);
                case "list" -> postLoginClient.listGames();
                case "join" -> state = postLoginClient.joinGame(tokens);
                case "observe" -> state = postLoginClient.observeGame(tokens);
                case "logout" -> state = postLoginClient.logout();
                case "quit" -> state = State.QUIT;
                case "help" -> System.out.println(postLoginClient.help());
            }
        }
    }

    private void inGameLoop(Scanner scanner) {
        while (state == State.INGAME) {
            String[] tokens = getInput(scanner, inGameClient);
            switch (tokens[0]) {
                case "draw" -> inGameClient.drawBoard();
                case "exit" -> state = inGameClient.exitGame();
                case "quit" -> state = State.QUIT;
                case "help" -> System.out.println(inGameClient.help());
            }
        }
    }

    private String[] getInput(Scanner scanner, Client client) {
        client.printPrompt();
        String input = scanner.nextLine();
        return input.split(" ");
    }

}
