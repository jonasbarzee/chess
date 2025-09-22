package chess.piecemoves.slidingpieces;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.piecemoves.Direction;
import chess.piecemoves.PieceMovesCalculator;

import java.util.List;

public abstract class SlidingPieceMovesCalculator implements PieceMovesCalculator {

    void addMoves(int row, int col, ChessBoard board, List<ChessMove> validMoves) {
        chess.ChessPiece currentPiece = board.getPiece(new ChessPosition(row, col));

        List<Direction> Directions = switch (currentPiece.getPieceType()) {
            case QUEEN -> Directions = List.of(Direction.NORTH, Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST, Direction.SOUTH, Direction.EAST, Direction.WEST);
            case BISHOP -> Directions = List.of(Direction.NORTHEAST, Direction.NORTHWEST, Direction.SOUTHEAST, Direction.SOUTHWEST);
            case ROOK -> Directions = List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
            default -> List.of();
        };

        for (Direction dir : Directions) {
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
