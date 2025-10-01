package chess;

import chess.piecemoves.*;
import chess.piecemoves.slidingpieces.BishopMovesCalculator;
import chess.piecemoves.slidingpieces.QueenMovesCalculator;
import chess.piecemoves.slidingpieces.RookMovesCalculator;
import chess.piecemoves.steppingpieces.KingMovesCalculator;
import chess.piecemoves.steppingpieces.KnightMovesCalculator;

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
    private final PieceType type;

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
        return type;
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
        if (piece.getPieceType() == PieceType.KING) {
            KingMovesCalculator kingCalc = new KingMovesCalculator();
            return kingCalc.pieceMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.ROOK) {
            RookMovesCalculator rookCalc = new RookMovesCalculator();
            return rookCalc.pieceMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.BISHOP) {
            BishopMovesCalculator bishopCalc = new BishopMovesCalculator();
            return bishopCalc.pieceMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.QUEEN) {
            QueenMovesCalculator queenCalc = new QueenMovesCalculator();
            return queenCalc.pieceMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.KNIGHT) {
            KnightMovesCalculator knightCalc = new KnightMovesCalculator();
            return knightCalc.pieceMoves(myPosition, board);
        } else if (piece.getPieceType() == PieceType.PAWN) {
            chess.piecemoves.PawnMovesCalculator pawnCalc = new PawnMovesCalculator();
            return pawnCalc.pieceMoves(myPosition, board);
        }
        return List.of();
    }

    public static ChessPiece copyPiece(ChessPiece piece) {
        ChessGame.TeamColor pieceColor = piece.getTeamColor();
        PieceType pieceType = piece.getPieceType();
        return new ChessPiece(pieceColor, pieceType);

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

    @Override
    public String toString() {
        return "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
