package chess.piecemoves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        List<ChessMove> validMoves = new ArrayList<>();

        chess.ChessMove forward = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col), null);
        chess.ChessMove forwardRight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col + 1), null);
        chess.ChessMove right = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col + 1), null);
        chess.ChessMove downRight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 1), null);
        chess.ChessMove down = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col), null);
        chess.ChessMove downLeft = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 1), null);
        chess.ChessMove left = new ChessMove(new ChessPosition(row, col), new ChessPosition(row, col - 1), null);
        chess.ChessMove forwardLeft = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col - 1), null);

        validateMoves(forward, board, validMoves);
        validateMoves(forwardRight, board, validMoves);
        validateMoves(right, board, validMoves);
        validateMoves(downRight, board, validMoves);
        validateMoves(down, board, validMoves);
        validateMoves(downLeft, board, validMoves);
        validateMoves(left, board, validMoves);
        validateMoves(forwardLeft, board, validMoves);

        return validMoves;

    }

    public void validateMoves(ChessMove move, ChessBoard board, List<ChessMove> validMoves) {

        boolean inBounds = inBounds(move);
        if (inBounds) {
            ChessPiece boardPiece = board.getPiece(move.getEndPosition());
            ChessPiece currentPiece = board.getPiece(move.getStartPosition());
            if (boardPiece != null) {
                if (boardPiece.getTeamColor() != currentPiece.getTeamColor()) {
                    validMoves.add(move);
                }
            } else {
                validMoves.add(move);
            }

        }
    }
}
