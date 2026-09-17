package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP) {
            return bishopMoves(board, myPosition);
        }
        return List.of();
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return directionalMoves(board, row, col, directions);
    }
    private Boolean onBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private Boolean squareOccupied(ChessBoard board, int row, int col) {
        return board.getPiece(new ChessPosition(row, col)) != null;
    }

    private Collection<ChessMove> directionalMoves(ChessBoard board, int row, int col, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int tmp_row = row;
        int tmp_col = col;

        for (int[] dir : directions) {
            while (true) {
                tmp_row += dir[0];
                tmp_col += dir[1];
                if (!onBoard(tmp_row, tmp_col)) {
                    break;
                }
                if (squareOccupied(board, tmp_row, tmp_col)) {      // if there is a piece in the way
                    if (board.getPiece(new ChessPosition(tmp_row, tmp_col)).getTeamColor() == pieceColor) {     // teammates block
                        break;
                    } else if (board.getPiece(new ChessPosition(tmp_row, tmp_col)).getTeamColor() != pieceColor) {        // allow piece take, but no further
                        moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmp_row, tmp_col), null));
                        break;
                    }
                }
                moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmp_row, tmp_col), null));
            }
            tmp_row = row;
            tmp_col = col;
        }
        return moves;
    }

}
