package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public ChessPiece(ChessPiece other) {
        this.pieceColor = other.pieceColor;
        this.type = other.type;
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
        switch (piece.getPieceType()) {
            case PieceType.BISHOP:
                return bishopMoves(board, myPosition);
            case PieceType.KING:
                return kingMoves(board, myPosition);
            case PieceType.KNIGHT:
                return knightMoves(board, myPosition);
            case PieceType.PAWN:
                return pawnMoves(board, myPosition);
            case PieceType.QUEEN:
                return queenMoves(board, myPosition);
            case PieceType.ROOK:
                return rookMoves(board, myPosition);
            default:
                throw new IllegalArgumentException("There must be a piece at the position");
        }
    }


    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return dynamicMoves(board, row, col, directions);
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        return staticMoves(board, row, col, directions);
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] directions = {{1, 2}, {2, 1}, {2, -1}, {1, -2}, {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};
        return staticMoves(board, row, col, directions);
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[][] directions;

        switch (pieceColor) {
            case ChessGame.TeamColor.WHITE:
                directions = new int[][]{{1, 0},{1, -1},{1, 1},{2, 0}};
                break;
            case ChessGame.TeamColor.BLACK:
                directions = new int[][]{{-1, 0},{-1, 1},{-1, -1},{-2, 0}};
                break;
            default:
                throw new IllegalArgumentException("Pawn must have color");
        }
        return pMoves(board, row, col, directions, pieceColor);
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        return dynamicMoves(board, row, col, directions);
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        int[][] directions = {{1, 0}, {0, 1}, {-1, 0}, {0,-1}};
        return dynamicMoves(board, row, col, directions);
    }

    private Boolean onBoard(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private Boolean squareOccupied(ChessBoard board, int row, int col) {
        return board.getPiece(new ChessPosition(row, col)) != null;
    }

    private Collection<ChessMove> dynamicMoves(ChessBoard board, int row, int col, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int tmpRow = row;
        int tmpCol = col;

        for (int[] dir : directions) {
            tmpRow = row;
            tmpCol = col;
            while (true) {
                tmpRow += dir[0];
                tmpCol += dir[1];
                if (!onBoard(tmpRow, tmpCol)) {
                    break;
                }
                if (squareOccupied(board, tmpRow, tmpCol)) {      // if there is a piece in the way
                    // teammates block
                    if (board.getPiece(new ChessPosition(tmpRow, tmpCol)).getTeamColor() == pieceColor) {
                        break;

                    // allow piece take, but no further
                    } else if (board.getPiece(new ChessPosition(tmpRow, tmpCol)).getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmpRow, tmpCol), null));
                        break;
                    }
                }
                moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmpRow, tmpCol), null));
            }
        }
        return moves;
    }

    private Collection<ChessMove> staticMoves(ChessBoard board, int row, int col, int[][] directions) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        for (int[] dir : directions) {
            int tmpRow = row;
            int tmpCol = col;
            tmpRow += dir[0];
            tmpCol += dir[1];
            if (!onBoard(tmpRow, tmpCol)) {
                continue;
            }
            if (squareOccupied(board, tmpRow, tmpCol)) {      // if there is a piece in the way
                // teammates block
                if (board.getPiece(new ChessPosition(tmpRow, tmpCol)).getTeamColor() == pieceColor) {
                    continue;

                // allow piece take, but no further
                } else if (board.getPiece(new ChessPosition(tmpRow, tmpCol)).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmpRow, tmpCol), null));
                    continue;
                }
            }
            moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmpRow, tmpCol), null));
        }
        return moves;
    }

    private Collection<ChessMove> pMoves(ChessBoard board, int row, int col, int[][] directions, ChessGame.TeamColor pieceColor) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        for (int[] dir : directions) {
            int tmpRow = row;
            int tmpCol = col;
            tmpRow += dir[0];
            tmpCol += dir[1];
            if (!onBoard(tmpRow, tmpCol)) {
                continue;
            }
            if (tmpRow - row == 2 || tmpRow - row == -2) {       // white on 2, black on 7
                if ((pieceColor == ChessGame.TeamColor.WHITE && row == 2) || (pieceColor == ChessGame.TeamColor.BLACK && row == 7)) {
                    doubleMove(moves, board, row, tmpRow, col, pieceColor);
                }
                break;
            }
            if (squareOccupied(board, tmpRow, tmpCol)) {      // there is a piece in the way
                // teammate or forward block
                if ((board.getPiece(new ChessPosition(tmpRow, tmpCol)).getTeamColor() == pieceColor) || (tmpCol == col)) {
                    continue;
                // diagonal enemy block, take
                } else if (board.getPiece(new ChessPosition(tmpRow, tmpCol)).getTeamColor() != pieceColor) {
                    checkPromoAddMove(moves, row, col, tmpRow, tmpCol);
                    continue;
                }
            } else {
                if (tmpCol == col) {   // empty forward square
                    checkPromoAddMove(moves, row, col, tmpRow, tmpCol);
                }
            }
        }
        return moves;
    }

    private void doubleMove(Collection<ChessMove> moves, ChessBoard board, int row, int tmpRow, int col, ChessGame.TeamColor pieceColor) {
        int jumped_row = (tmpRow + row) / 2;

        if (!squareOccupied(board, jumped_row, col) && !squareOccupied(board, tmpRow, col)) {
            moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmpRow, col), null));
        }
    }

    private void checkPromoAddMove(Collection<ChessMove> moves, int row, int col, int tmpRow, int tmpCol) {
        PieceType[] promotionPieces = {PieceType.QUEEN, PieceType.ROOK, PieceType.BISHOP, PieceType.KNIGHT};

        if (tmpRow > 1 && tmpRow < 8) {           // normal advance
            moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmpRow, tmpCol), null));
        } else {
            for (PieceType piece : promotionPieces) {   // promotion
                moves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(tmpRow, tmpCol), piece));
            }
        }
    }

    @Override
    public String toString() {
        if (getTeamColor() == ChessGame.TeamColor.WHITE) {
            switch (getPieceType()) {
                case KING: return "K";
                case QUEEN: return "Q";
                case BISHOP: return "B";
                case KNIGHT: return "N";
                case ROOK: return "R";
                case PAWN: return "P";
            }
        }
        if (getTeamColor() == ChessGame.TeamColor.BLACK) {
            switch (getPieceType()) {
                case KING: return "k";
                case QUEEN: return "q";
                case BISHOP: return "b";
                case KNIGHT: return "n";
                case ROOK: return "r";
                case PAWN: return "p";
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
