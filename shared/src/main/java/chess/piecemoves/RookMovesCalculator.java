package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMovesCalculator implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        int i;
        List<ChessMove> validMoves = new ArrayList<>();

        for (i = 1; i < 7; i++) {
            chess.ChessMove forward = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + i, col), null);
            if (inBounds(forward) && isBlocked(forward, board) && isEnemy(forward, board)) {
                addMove(forward, validMoves);
                break;
            } else if (inBounds(forward) && !isBlocked(forward, board)) {
                addMove(forward, validMoves);
            } else if (inBounds(forward) && isBlocked(forward, board) && !isEnemy(forward,board)) {
                break;
            }
        }

        for (i = 1; i < 7; i++) {
            chess.ChessMove right = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col + i), null);
            if (inBounds(right) && isBlocked(right, board) && isEnemy(right, board)) {
                addMove(right, validMoves);
                break;
            } else if (inBounds(right) && !isBlocked(right, board)) {
                addMove(right, validMoves);
            } else if (inBounds(right) && isBlocked(right, board) && !isEnemy(right, board)) {
                break;
            }
        }
        for (i = 1; i < 7; i++) {
            chess.ChessMove down = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - i, col), null);
            if (inBounds(down) && isBlocked(down, board) && isEnemy(down, board)) {
                addMove(down, validMoves);
                break;
            } else if (inBounds(down) && !isBlocked(down, board)) {
                addMove(down, validMoves);
            } else if (inBounds(down) && isBlocked(down,board) && !isEnemy(down, board)) {
                break;
            }
        }
        for (i = 1; i < 7; i++) {
            chess.ChessMove left = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col - i), null);
            if (inBounds(left) && isBlocked(left, board) && isEnemy(left, board)) {
                addMove(left, validMoves);
                break;
            } else if (inBounds(left) && !isBlocked(left, board)) {
                addMove(left, validMoves);
            } else if (inBounds(left) && isBlocked(left, board) && !isEnemy(left, board)) {
                break;
            }
        }

        return validMoves;
    }

    public void addMove(ChessMove move, List<ChessMove> validMoves) { validMoves.add(move); }

    public boolean isEnemy(ChessMove move, ChessBoard board) {
        boolean isEnemy = false;
        ChessPiece boardPiece = board.getPiece(move.getEndPosition());
        ChessPiece currentPiece = board.getPiece(move.getStartPosition());
        if (boardPiece.getTeamColor() != currentPiece.getTeamColor()) {
           isEnemy = true;
        }
        return isEnemy;


    }

    public boolean isBlocked(ChessMove move, ChessBoard board) {
        boolean isBlocked = false;
        ChessPiece boardPiece = board.getPiece(move.getEndPosition());
        if (boardPiece != null) {
            isBlocked = true;
        }
        return isBlocked;

    }

    public boolean inBounds(ChessMove move) {

        int col = move.getEndPosition().getColumn();
        int row = move.getEndPosition().getRow();

        if ((row > 8 || col > 8) || (row < 1 || col < 1)) {
            return false;
        }
        return true;
    }
}
