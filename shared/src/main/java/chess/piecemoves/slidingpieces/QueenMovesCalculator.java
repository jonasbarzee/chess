package chess.piecemoves.slidingpieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.piecemoves.Direction;
import chess.piecemoves.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator extends SlidingPieceMovesCalculator {

    private static final List<Direction> QUEENMOVES = List.of(Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.SOUTH, Direction.EAST, Direction.WEST);

    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        List<ChessMove> validMoves = new ArrayList<>();

        addMoves(row, col, board, validMoves);

        return validMoves;
    }
}

