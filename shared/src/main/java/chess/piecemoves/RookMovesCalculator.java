package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        int i;
        List<ChessMove> validMoves = new ArrayList<>();
//
//        for (i = 1; i < 7; i++) {
//            chess.ChessMove forward = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + i, col), null);
//            if (inBounds(forward) && isBlocked(forward, board) && isEnemy(forward, board)) {
//                addMove(forward, validMoves);
//                break;
//            } else if (inBounds(forward) && !isBlocked(forward, board)) {
//                addMove(forward, validMoves);
//            } else if (inBounds(forward) && isBlocked(forward, board) && !isEnemy(forward, board)) {
//                break;
//            }
//        }
//
//        for (i = 1; i < 7; i++) {
//            chess.ChessMove right = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col + i), null);
//            if (inBounds(right) && isBlocked(right, board) && isEnemy(right, board)) {
//                addMove(right, validMoves);
//                break;
//            } else if (inBounds(right) && !isBlocked(right, board)) {
//                addMove(right, validMoves);
//            } else if (inBounds(right) && isBlocked(right, board) && !isEnemy(right, board)) {
//                break;
//            }
//        }
//        for (i = 1; i < 7; i++) {
//            chess.ChessMove down = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - i, col), null);
//            if (inBounds(down) && isBlocked(down, board) && isEnemy(down, board)) {
//                addMove(down, validMoves);
//                break;
//            } else if (inBounds(down) && !isBlocked(down, board)) {
//                addMove(down, validMoves);
//            } else if (inBounds(down) && isBlocked(down, board) && !isEnemy(down, board)) {
//                break;
//            }
//        }
//        for (i = 1; i < 7; i++) {
//            chess.ChessMove left = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col - i), null);
//            if (inBounds(left) && isBlocked(left, board) && isEnemy(left, board)) {
//                addMove(left, validMoves);
//                break;
//            } else if (inBounds(left) && !isBlocked(left, board)) {
//                addMove(left, validMoves);
//            } else if (inBounds(left) && isBlocked(left, board) && !isEnemy(left, board)) {
//                break;
//            }
//        }


        return validMoves;
    }

    public void addMove(ChessMove move, List<ChessMove> validMoves) {
        validMoves.add(move);
    }
}
