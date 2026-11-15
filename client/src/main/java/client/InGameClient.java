package client;

import chess.*;
import model.*;
import server.ServerFacade;
import ui.EscapeSequences;

import java.util.Objects;

public class InGameClient implements Client {
    private final ServerFacade facade;
    private final ChessGame chessGame = new ChessGame();

    public InGameClient(ServerFacade facade) {
        this.facade = facade;
    }

    public String drawBoard(String perspective) {
        if (Objects.equals(perspective, "WHITE")) {
            return prettyBoard(chessGame.getBoard(),8,1,-1);
        } else {
            return prettyBoard(chessGame.getBoard(),1,8,1);
        }
    }

    @Override
    public void printPrompt() {
        System.out.print("\n" + "INGAME >>> ");
    }

    @Override
    public String help() {
        return """
                 - no commands available, gameplay not implemented yet
                """;
    }
}
