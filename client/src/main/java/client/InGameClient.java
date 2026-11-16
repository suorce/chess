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
        StringBuilder colLabel = new StringBuilder();
        result.append("\n");

        colLabel.append("  ");
        for (int row = i; row != j+inc; row += inc) {
            result.append(String.valueOf(row) + " ");
            for (int col = j; col != i-inc; col -= inc) {
                if (row == 1) {
                    colLabel.append(String.valueOf(col) + "\u2003" + " ");
                }
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                if ((row + col) % 2 == 0) {
                    result.append(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                } else {
                    result.append(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                }
                result.append(prettyPiece(piece));
            }
            result.append(EscapeSequences.RESET_BG_COLOR);
            result.append("\n");
        }
        result.append(colLabel);
        return result.toString();
    }

    private String prettyPiece(ChessPiece piece) {
        String result = "";
        if (piece == null) {
            result = EscapeSequences.EMPTY;
        } else {
            switch (piece.getTeamColor()) {
                case WHITE -> {
                    switch (piece.getPieceType()) {
                        case KING -> {
                            result = EscapeSequences.WHITE_KING;
                        }
                        case QUEEN -> {
                            result = EscapeSequences.WHITE_QUEEN;
                        }
                        case BISHOP -> {
                            result = EscapeSequences.WHITE_BISHOP;
                        }
                        case KNIGHT -> {
                            result = EscapeSequences.WHITE_KNIGHT;
                        }
                        case ROOK -> {
                            result = EscapeSequences.WHITE_ROOK;
                        }
                        case PAWN -> {
                            result = EscapeSequences.WHITE_PAWN;
                        }
                    }
                }
                case BLACK -> {
                    switch (piece.getPieceType()) {
                        case KING -> {
                            result = EscapeSequences.BLACK_KING;
                        }
                        case QUEEN -> {
                            result = EscapeSequences.BLACK_QUEEN;
                        }
                        case BISHOP -> {
                            result = EscapeSequences.BLACK_BISHOP;
                        }
                        case KNIGHT -> {
                            result = EscapeSequences.BLACK_KNIGHT;
                        }
                        case ROOK -> {
                            result = EscapeSequences.BLACK_ROOK;
                        }
                        case PAWN -> {
                            result = EscapeSequences.BLACK_PAWN;
                        }
                    }
                }
            }
        }
        return result;
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
