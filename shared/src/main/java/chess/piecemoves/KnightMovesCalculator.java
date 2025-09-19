package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        List<ChessMove> validMoves = new ArrayList<>();

        chess.ChessMove forwardTwoRightOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 2, col + 1), null);
        chess.ChessMove forwardOneRightTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col + 2), null);
        chess.ChessMove rightTwoDownOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 2), null);
        chess.ChessMove rightOneDownTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 2, col + 1), null);
        chess.ChessMove downTwoLeftOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 2, col - 1), null);
        chess.ChessMove downOneLeftTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 2), null);
        chess.ChessMove leftTwoForwardOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col - 2), null);
        chess.ChessMove leftOneForwardTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 2, col - 1), null);

        validateMove(forwardTwoRightOne, board, validMoves);
        validateMove(forwardOneRightTwo, board, validMoves);
        validateMove(rightTwoDownOne, board, validMoves);
        validateMove(rightOneDownTwo, board, validMoves);
        validateMove(downTwoLeftOne, board, validMoves);
        validateMove(downOneLeftTwo, board, validMoves);
        validateMove(leftTwoForwardOne, board, validMoves);
        validateMove(leftOneForwardTwo, board, validMoves);

        return validMoves;
    }

    public void validateMove(ChessMove move, ChessBoard board, List<ChessMove> validMoves) {
        boolean inBounds = inBounds(move);
        boolean isEnemy = isEnemy(move, board);
        boolean isBlocked = isBlocked(move, board);

        if (inBounds && isEnemy && isBlocked) {
            validMoves.add(move);
        }
        if (inBounds && !isBlocked) {
            validMoves.add(move);
        }
    }

}
