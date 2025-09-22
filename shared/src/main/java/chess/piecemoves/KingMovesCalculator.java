package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {

    private static final List<Direction> KING_DIRECTIONS = List.of(Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST, Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.SOUTHEAST, Direction.SOUTHWEST);

    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        List<ChessMove> validMoves = new ArrayList<>();

        for (Direction direction : KING_DIRECTIONS) {
            int newRow = row + direction.getRowChange();
            int newCol = col + direction.getColChange();
            chess.ChessMove move = new ChessMove(new ChessPosition(row, col), new ChessPosition(newRow, newCol), null);
            validateMove(move, board, validMoves);

        }
        return validMoves;
    }

    public void validateMove(ChessMove move, ChessBoard board, List<ChessMove> validMoves) {
        boolean inBounds = inBounds(move);
        boolean isBlocked = isBlocked(move, board);
        boolean isEnemy = isEnemy(move, board);

        if ((inBounds & !isBlocked) || (inBounds & isBlocked & isEnemy)) {
            validMoves.add(move);
        }
    }
}
