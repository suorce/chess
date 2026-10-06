package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {

    }

    public ChessBoard(ChessBoard other) {
        this.squares = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (other.squares[row][col] != null) {
                    ChessPiece otherPiece = other.getPiece(new ChessPosition(row+1, col+1));
                    this.squares[row][col] = new ChessPiece(otherPiece);
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    public void movePiece(ChessMove move){
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessGame.TeamColor color = getPiece(startPosition).getTeamColor();
        ChessPiece.PieceType type = getPiece(startPosition).getPieceType();
        addPiece(startPosition, null);
        if (move.getPromotionPiece() == null) {
            addPiece(endPosition, new ChessPiece(color, type));
        } else {
            addPiece(endPosition, new ChessPiece(color, move.getPromotionPiece()));
        }
    }
    
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int row = 2; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = null;
            }
        }
        for (int col = 0; col < 8; col++) {
            squares[1][col] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[6][col] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }

        ChessPiece.PieceType[] backrank = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        for (int col = 0; col < 8; col++) {
            squares[0][col] = new ChessPiece(ChessGame.TeamColor.WHITE, backrank[col]);
            squares[7][col] = new ChessPiece(ChessGame.TeamColor.BLACK, backrank[col]);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n");

        for (ChessPiece[] row : squares) {
            for (ChessPiece piece : row) {
                if (piece == null) {
                    result.append(". ");
                } else {
                    result.append(piece).append(" ");
                }
            }
            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
