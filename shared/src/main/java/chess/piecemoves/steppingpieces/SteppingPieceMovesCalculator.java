package chess.piecemoves.steppingpieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.piecemoves.PieceMovesCalculator;

import java.util.List;

public abstract class SteppingPieceMovesCalculator implements PieceMovesCalculator {

     void validateMove(ChessMove move, ChessBoard board, List<ChessMove> validMoves) {
        boolean inBounds = inBounds(move);
        boolean isBlocked = isBlocked(move, board);
        boolean isEnemy = isEnemy(move, board);

        if ((inBounds & !isBlocked) || (inBounds & isBlocked & isEnemy)) {
            validMoves.add(move);
        }
    }
}
