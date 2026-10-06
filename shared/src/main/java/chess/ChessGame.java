package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board = new ChessBoard();
    private TeamColor teamTurn;

    public ChessGame() {
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    public ChessGame(ChessGame other) {
        this.board = new ChessBoard(other.board);
        this.teamTurn = other.teamTurn;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        Collection<ChessMove> moves = piece.pieceMoves(board,startPosition);
        TeamColor color = piece.getTeamColor();

        moves.removeIf(move -> {
            ChessGame gameCopy = new ChessGame(this);
            gameCopy.board.movePiece(move);
            return gameCopy.isInCheck(color);
        });
        return moves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("No piece here");
        }
        if (piece.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not this player's turn");
        }
        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if (moves.isEmpty()) {
            throw new InvalidMoveException("Piece has no valid moves");
        }
        if (moves.contains(move)) {
            board.movePiece(move);
            swapTurns(piece.getTeamColor());
        } else {
            throw new InvalidMoveException("Move either not valid or leaves king in check");
        }
    }

    private void swapTurns(TeamColor teamColor) {
        if (teamColor == TeamColor.WHITE) {
            teamTurn = TeamColor.BLACK;
        } else {
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        ChessPosition kingPosition = null;
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(new ChessPosition(row,col));
                if (piece == null){
                    continue;
                }
                //find kingPosition
                if (piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = new ChessPosition(row,col);
                }
                // find enemy moves
                else if (piece.getTeamColor() != teamColor) {
                    moves.addAll(piece.pieceMoves(board,position));
                }
            }
        }
        for (ChessMove move : moves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return noValidMoves(teamColor) && isInCheck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return noValidMoves(teamColor) && !isInCheck(teamColor);
    }

    private boolean noValidMoves(TeamColor teamColor) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        for (int r = 1; r <= 8; r++) {
            for (int c = 1; c <= 8; c++) {
                ChessPosition position = new ChessPosition(r,c);
                ChessPiece piece = board.getPiece(new ChessPosition(r,c));
                if (piece == null){
                    continue;
                }
                // find all potential moves to escape check
                if (piece.getTeamColor() == teamColor) {
                    moves.addAll(piece.pieceMoves(board,position));
                }
            }
        }
        for (ChessMove move : moves) {
            ChessPosition startPosition = move.getStartPosition();
            ChessPosition endPosition = move.getEndPosition();
            ChessGame gameClone = new ChessGame(this);
            gameClone.board.addPiece(endPosition, board.getPiece(startPosition));
            gameClone.board.addPiece(startPosition,null);

            if (!gameClone.isInCheck(teamColor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return board.toString() + teamTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && teamTurn == chessGame.teamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, teamTurn);
    }
}
