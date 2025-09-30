package chess.piecemoves.slidingpieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator extends SlidingPieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        int i;
        List<ChessMove> validMoves = new ArrayList<>();
        addMoves(row, col, board, validMoves);

        return validMoves;
    }
}
