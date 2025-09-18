package chess.piecemoves;

// need to know: chess piece type, the position

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMovesCalculator {

    Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board);

    default boolean inBounds(ChessMove move) {
        int col = move.getEndPosition().getColumn();
        int row = move.getEndPosition().getRow();

        return (row <= 8 && col <= 8) && (row >= 1 && col >= 1);
    }

    default boolean isEnemy(ChessMove move, ChessBoard board) {
        boolean isEnemy = false;
        if (inBounds(move)) {
            ChessPiece boardPiece = board.getPiece(move.getEndPosition());
            ChessPiece currentPiece = board.getPiece(move.getStartPosition());
            if (boardPiece == null) {
                return isEnemy;
            }
            if (boardPiece.getTeamColor() != currentPiece.getTeamColor()) {
                isEnemy = true;
            }
        }

        return isEnemy;


    }

    default boolean isBlocked(ChessMove move, ChessBoard board) {
        boolean isBlocked = false;
        if (inBounds(move)) {
            ChessPiece boardPiece = board.getPiece(move.getEndPosition());
            if (boardPiece != null) {
                isBlocked = true;
            }
        }

        return isBlocked;
    }

}
