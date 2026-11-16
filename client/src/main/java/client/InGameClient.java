package client;

import chess.*;
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
        StringBuilder colLabel = new StringBuilder();
        result.append("\n");

        colLabel.append("  ");
        for (int col = j; col != i-inc; col -= inc) {
            char letter = (char) ('a' + col - 1);
            colLabel.append(" " + letter + "\u2003");
        }
        result.append(colLabel).append("\n");

        for (int row = i; row != j+inc; row += inc) {
            result.append(String.valueOf(row) + " ");
            for (int col = j; col != i-inc; col -= inc) {
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                if ((row + col) % 2 == 0) {
                    result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                } else {
                    result.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                }
                result.append(prettyPiece(piece));
            }
            result.append(EscapeSequences.RESET_BG_COLOR)
                    .append(" ")
                    .append(String.valueOf(row))
                    .append("\n");
        }
        result.append(colLabel);
        return result.toString();
    }

    private String prettyPiece(ChessPiece piece) {
        StringBuilder result = new StringBuilder();
        if (piece == null) {
            result.append(EscapeSequences.EMPTY);
        } else {
            switch (piece.getTeamColor()) {
                case WHITE -> result.append(EscapeSequences.SET_TEXT_COLOR_WHITE);
                case BLACK -> result.append(EscapeSequences.SET_TEXT_COLOR_BLACK);
            }
            switch (piece.getPieceType()) {
                case KING -> result.append(EscapeSequences.BLACK_KING);
                case QUEEN -> result.append(EscapeSequences.BLACK_QUEEN);
                case BISHOP -> result.append(EscapeSequences.BLACK_BISHOP);
                case KNIGHT -> result.append(EscapeSequences.BLACK_KNIGHT);
                case ROOK -> result.append(EscapeSequences.BLACK_ROOK);
                case PAWN -> result.append(EscapeSequences.BLACK_PAWN);
            }
        }
        result.append(EscapeSequences.RESET_TEXT_COLOR);
        return result.toString();
    }

    @Override
    public void printPrompt() {
        System.out.print("\n" + "INGAME >>> ");
    }

    @Override
    public String help() {
        return """
                 - draw - the board
                 - exit - the game
                 - quit - the application
                """;
    }
}
