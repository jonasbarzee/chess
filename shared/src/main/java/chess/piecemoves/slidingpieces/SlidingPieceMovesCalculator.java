package chess.piecemoves.slidingpieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.piecemoves.Direction;
import chess.piecemoves.PieceMovesCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class SlidingPieceMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        List<ChessMove> validMoves = new ArrayList<>();
        addMoves(row, col, board, validMoves);

        return validMoves;
    }

    void addMoves(int row, int col, ChessBoard board, List<ChessMove> validMoves) {
        chess.ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));

        List<Direction> directions = switch (currentPiece.getPieceType()) {
            case QUEEN -> List.of(Direction.values());
            case BISHOP -> List.of(Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST);
            case ROOK -> List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
            default -> List.of();
        };

        for (Direction dir : directions) {
            for (int i = 1; i < 8; i++) {
                int newRow = row + (i * dir.getRowChange());
                int newCol = col + (i * dir.getColChange());
                chess.ChessMove move = new ChessMove(new ChessPosition(row, col), new ChessPosition(newRow, newCol), null);

                boolean inBounds = inBounds(move);
                boolean isBlocked = isBlocked(move, board);
                boolean isEnemy = isEnemy(move, board);

                if ((inBounds & !isBlocked) || (inBounds & isBlocked & isEnemy)) {
                    validMoves.add(move);
                    if (isEnemy) { // so the loop will stop when you capture the first enemy
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }
}
