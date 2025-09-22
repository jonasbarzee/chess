package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {

    private static final List<Direction> ROOKMOVES = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        int i;
        List<ChessMove> validMoves = new ArrayList<>();
        addMoves(row, col, board, validMoves);

        return validMoves;
    }

    void addMoves(int row, int col, ChessBoard board, List<ChessMove> validMoves) {
        for (Direction dir : ROOKMOVES) {
            for (int i = 1; i < 8; i++) {
                int newRow = row + (i * dir.getRowChange());
                int newCol = col + (i * dir.getColChange());
                chess.ChessMove move = new ChessMove(new ChessPosition(row, col), new ChessPosition(newRow, newCol), null);

                boolean inBounds = inBounds(move);
                boolean isBlocked = isBlocked(move, board);
                boolean isEnemy = isEnemy(move, board);

                if ((inBounds & !isBlocked) || (inBounds & isBlocked & isEnemy)) {
                    validMoves.add(move);
                    if (isEnemy) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }
}
