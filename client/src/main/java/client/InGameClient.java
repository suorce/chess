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

    private String prettyBoard(ChessBoard board, int i, int j, int inc) {
        StringBuilder result = new StringBuilder();
        result.append("\n");

        for (int row = i; row != j-1; row += inc) {
            for (int col = j-1; col != i+1; col += -inc) {
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                if (row + col % 2 == 0) {
                    result.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                } else {
                    result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }
                result.append(prettyPiece(piece));
            }
            result.append("\n");
        }

        return result.toString();
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
