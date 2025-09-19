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
        // White promotion moves
        chess.ChessMove forwardOnePromoKnight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col), ChessPiece.PieceType.KNIGHT);
        chess.ChessMove forwardOnePromoQueen = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col), ChessPiece.PieceType.QUEEN);
        chess.ChessMove forwardOnePromoRook = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col), ChessPiece.PieceType.ROOK);
        chess.ChessMove forwardOnePromoBishop = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col), ChessPiece.PieceType.BISHOP);
        // White promotion capture moves
        chess.ChessMove forwardLeftPromoKnight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.KNIGHT);
        chess.ChessMove forwardLeftPromoQueen = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.QUEEN);
        chess.ChessMove forwardLeftPromoRook = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.ROOK);
        chess.ChessMove forwardLeftPromoBishop = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col - 1), ChessPiece.PieceType.BISHOP);
        chess.ChessMove forwardRightPromoKnight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.KNIGHT);
        chess.ChessMove forwardRightPromoQueen = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.QUEEN);
        chess.ChessMove forwardRightPromoRook = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.ROOK);
        chess.ChessMove forwardRightPromoBishop = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + 1, col + 1), ChessPiece.PieceType.BISHOP);

        // Black moves
        chess.ChessMove backOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col), null);
        chess.ChessMove backTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 2, col), null);
        chess.ChessMove backLeft = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 1), null);
        chess.ChessMove backRight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 1), null);
        // Black Promotion moves
        chess.ChessMove backOnePromoKnight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col), ChessPiece.PieceType.KNIGHT);
        chess.ChessMove backOnePromoQueen = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col), ChessPiece.PieceType.QUEEN);
        chess.ChessMove backOnePromoRook = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col), ChessPiece.PieceType.ROOK);
        chess.ChessMove backOnePromoBishop = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col), ChessPiece.PieceType.BISHOP);
        chess.ChessMove backLeftPromoKnight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.KNIGHT);
        chess.ChessMove backLeftPromoQueen = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.QUEEN);
        chess.ChessMove backLeftPromoRook = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.ROOK);
        chess.ChessMove backLeftPromoBishop = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col - 1), ChessPiece.PieceType.BISHOP);
        chess.ChessMove backRightPromoKnight = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.KNIGHT);
        chess.ChessMove backRightPromoQueen = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.QUEEN);
        chess.ChessMove backRightPromoRook = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.ROOK);
        chess.ChessMove backRightPromoBishop = new ChessMove(new ChessPosition(row, col), new ChessPosition(row - 1, col + 1), ChessPiece.PieceType.BISHOP);

        // White validate moves
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            // if the pawn is at it's starting row
            if (row == 2 && !isBlocked(forwardOne, board)) {
                validateMoves(forwardTwo, board, validMoves, false);
            }
            // if the pawn is in the row before moving into the promotion row
            if (row == 7) {
                validateMoves(forwardOnePromoKnight, board, validMoves, false);
                validateMoves(forwardOnePromoQueen, board, validMoves, false);
                validateMoves(forwardOnePromoRook, board, validMoves, false);
                validateMoves(forwardOnePromoBishop, board, validMoves, false);
                validateMoves(forwardLeftPromoKnight, board, validMoves, true);
                validateMoves(forwardLeftPromoQueen, board, validMoves, true);
                validateMoves(forwardLeftPromoRook, board, validMoves, true);
                validateMoves(forwardLeftPromoBishop, board, validMoves, true);
                validateMoves(forwardRightPromoKnight, board, validMoves, true);
                validateMoves(forwardRightPromoQueen, board, validMoves, true);
                validateMoves(forwardRightPromoRook, board, validMoves, true);
                validateMoves(forwardRightPromoBishop, board, validMoves, true);

            } else {
                validateMoves(forwardOne, board, validMoves, false);
                validateMoves(forwardLeft, board, validMoves, true);
                validateMoves(forwardRight, board, validMoves, true);
            }
        }
        // Black validate moves
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            // if the pawn is at it's starting row
            if (row == 7 && !isBlocked(backOne, board)) {
                validateMoves(backTwo, board, validMoves, false);
            }
            if (row == 2) {
                validateMoves(backOnePromoKnight, board, validMoves, false);
                validateMoves(backOnePromoQueen, board, validMoves, false);
                validateMoves(backOnePromoRook, board, validMoves, false);
                validateMoves(backOnePromoBishop, board, validMoves, false);
                validateMoves(backLeftPromoKnight, board, validMoves, true);
                validateMoves(backLeftPromoQueen, board, validMoves, true);
                validateMoves(backLeftPromoRook, board, validMoves, true);
                validateMoves(backLeftPromoBishop, board, validMoves, true);
                validateMoves(backRightPromoKnight, board, validMoves, true);
                validateMoves(backRightPromoQueen, board, validMoves, true);
                validateMoves(backRightPromoRook, board, validMoves, true);
                validateMoves(backRightPromoBishop, board, validMoves, true);
            } else {
                validateMoves(backOne, board, validMoves, false);
                validateMoves(backLeft, board,  validMoves, true);
                validateMoves(backRight, board,  validMoves, true);
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
