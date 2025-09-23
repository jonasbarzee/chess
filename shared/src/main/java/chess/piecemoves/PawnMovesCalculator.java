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
        int[] diagDir = {1, -1};
        ChessPiece currentPiece = board.getPiece(position);
        ChessGame.TeamColor currentColor = currentPiece.getTeamColor();
        List<ChessMove> validMoves = new ArrayList<>();

        int direction = (currentColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (currentColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promoRow = (currentColor == ChessGame.TeamColor.WHITE) ? 7 : 2;

        chess.ChessMove moveOne = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + direction, col), null);

        if (row == startRow && !isBlocked(moveOne, board)) {
            chess.ChessMove moveTwo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + direction * 2, col), null);
            validateMoves(moveTwo, board, validMoves, false);
        }

        if (row == promoRow) { // if the piece is in the row before getting promoted
            for (ChessPiece.PieceType promoPiece : ChessPiece.PieceType.values()) {
                if (promoPiece == ChessPiece.PieceType.PAWN || promoPiece == ChessPiece.PieceType.KING) { }
                else {
                    chess.ChessMove moveOnePromo = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + direction, col), promoPiece);
                    validateMoves(moveOnePromo, board, validMoves, false);
                    for (int captureDir : diagDir) {
                        chess.ChessMove moveCapture = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + direction, col + captureDir), promoPiece);
                        validateMoves(moveCapture, board, validMoves, true);
                    }
                }
            }
        }
        else {
            validateMoves(moveOne, board, validMoves, false);
            for (int captureDir : diagDir) {
                chess.ChessMove moveCapture = new ChessMove(new ChessPosition(row, col), new ChessPosition(row + direction, col + captureDir), null);
                validateMoves(moveCapture, board, validMoves, true);
            }
        }
        return validMoves;
    }

    public void validateMoves(ChessMove move, ChessBoard board, List<ChessMove> validMoves, boolean isCapture) {
        boolean inBounds = inBounds(move);
        boolean isBlocked = isBlocked(move, board);
        boolean isEnemy = isEnemy(move, board);

        if (inBounds & isCapture & isEnemy) {
            validMoves.add(move);
        }
        if (inBounds & !isCapture & !isBlocked) {
            validMoves.add(move);
        }

    }
}
