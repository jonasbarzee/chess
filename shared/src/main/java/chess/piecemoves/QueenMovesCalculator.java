package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        int i;
        List<ChessMove> validMoves = new ArrayList<>();

        for (i = 1; i < 7; i++) {
            chess.ChessMove forwardright = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + i, col + i), null);

            if (inBounds(forwardright) && isBlocked(forwardright, board) && isEnemy(forwardright, board)) {
                validMoves.add(forwardright);
                break;
            } else if (inBounds(forwardright) && !isBlocked(forwardright, board)) {
                validMoves.add(forwardright);
            } else if (inBounds(forwardright) && isBlocked(forwardright, board) && !isEnemy(forwardright, board)) {
                break;
            }
        }
        for (i = 1; i < 7; i++) {
            chess.ChessMove downright = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - i, col + i), null);

            if (inBounds(downright) && isBlocked(downright, board) && isEnemy(downright, board)) {
                validMoves.add(downright);
                break;
            } else if (inBounds(downright) && !isBlocked(downright, board)) {
                validMoves.add(downright);
            } else if (inBounds(downright) && isBlocked(downright, board) && !isEnemy(downright, board)) {
                break;
            }
        }
        for (i = 1; i < 7; i++) {
            chess.ChessMove downleft = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - i, col - i), null);

            if (inBounds(downleft) && isBlocked(downleft, board) && isEnemy(downleft, board)) {
                validMoves.add(downleft);
                break;
            } else if (inBounds(downleft) && !isBlocked(downleft, board)) {
                validMoves.add(downleft);
            } else if (inBounds(downleft) && isBlocked(downleft, board) && !isEnemy(downleft, board)) {
                break;
            }
        }
        for (i = 1; i < 7; i++) {
            chess.ChessMove forwardleft = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + i, col - i), null);

            if (inBounds(forwardleft) && isBlocked(forwardleft, board) && isEnemy(forwardleft, board)) {
                validMoves.add(forwardleft);
                break;
            } else if (inBounds(forwardleft) && !isBlocked(forwardleft, board)) {
                validMoves.add(forwardleft);
            } else if (inBounds(forwardleft) && isBlocked(forwardleft, board) && !isEnemy(forwardleft, board)) {
                break;
            }
        }

        for (i = 1; i < 7; i++) {
            chess.ChessMove forward = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + i, col), null);
            if (inBounds(forward) && isBlocked(forward, board) && isEnemy(forward, board)) {
                validMoves.add(forward);
                break;
            } else if (inBounds(forward) && !isBlocked(forward, board)) {
                validMoves.add(forward);
            } else if (inBounds(forward) && isBlocked(forward, board) && !isEnemy(forward, board)) {
                break;
            }
        }

        for (i = 1; i < 7; i++) {
            chess.ChessMove right = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col + i), null);
            if (inBounds(right) && isBlocked(right, board) && isEnemy(right, board)) {
                validMoves.add(right);
                break;
            } else if (inBounds(right) && !isBlocked(right, board)) {
                validMoves.add(right);
            } else if (inBounds(right) && isBlocked(right, board) && !isEnemy(right, board)) {
                break;
            }
        }
        for (i = 1; i < 7; i++) {
            chess.ChessMove down = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - i, col), null);
            if (inBounds(down) && isBlocked(down, board) && isEnemy(down, board)) {
                validMoves.add(down);
                break;
            } else if (inBounds(down) && !isBlocked(down, board)) {
                validMoves.add(down);
            } else if (inBounds(down) && isBlocked(down, board) && !isEnemy(down, board)) {
                break;
            }
        }
        for (i = 1; i < 7; i++) {
            chess.ChessMove left = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col - i), null);
            if (inBounds(left) && isBlocked(left, board) && isEnemy(left, board)) {
                validMoves.add(left);
                break;
            } else if (inBounds(left) && !isBlocked(left, board)) {
                validMoves.add(left);
            } else if (inBounds(left) && isBlocked(left, board) && !isEnemy(left, board)) {
                break;
            }
        }
        return validMoves;
    }
}

