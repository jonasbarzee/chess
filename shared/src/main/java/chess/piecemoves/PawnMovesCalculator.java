package chess.piecemoves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessPosition position, ChessBoard board) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPiece currentPiece = board.getPiece(position);
        List<ChessMove> validMoves = new ArrayList<>();


        // White moves
        chess.ChessMove forwardOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col), null);
        chess.ChessMove forwardTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 2, col), null);
        chess.ChessMove forwardLeft = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col - 1), null);
        chess.ChessMove forwardRight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col + 1), null);

        // Black moves
        chess.ChessMove backOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col), null);
        chess.ChessMove backTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 2, col), null);
        chess.ChessMove backLeft = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 1), null);
        chess.ChessMove backRight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 1), null);

        // White validate moves
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            validateMoves(forwardOne, board, validMoves, false);
            validateMoves(forwardLeft, board, validMoves, true);
            validateMoves(forwardRight, board, validMoves, true);
            // if the pawn is at it's starting row
            if (row == 2 && !isBlocked(forwardOne, board)) {
                validateMoves(forwardTwo, board, validMoves, false);
            }
            // if the pawn is in the row before moving into the promotion row
            if (row == 7) {

            }
        }
        // Black validate moves
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            validateMoves(backOne, board, validMoves, false);
            validateMoves(backLeft, board,  validMoves, true);
            validateMoves(backRight, board,  validMoves, true);
            // if the pawn is at it's starting row
            if (row == 7 && !isBlocked(backOne, board)) {
                validateMoves(backTwo, board, validMoves, false);
            }
        }

        return validMoves;
    }

    public void validateMoves(ChessMove move, ChessBoard board, List<ChessMove> validMoves, boolean isCapture) {
        boolean inBounds = inBounds(move);
        boolean isBlocked = isBlocked(move, board);
        boolean isEnemy = isEnemy(move, board);

        if (inBounds) {
           if (isCapture) {
               if (isEnemy) {
                   validMoves.add(move);
               }
           } else {
               if (!isBlocked) {
                   validMoves.add(move);
               }
           }
        }


    }


}
