package chess;

import java.util.*;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessPiece.copyPiece;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null; // removing the piece by setting it to null
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        Collection<ChessPiece.PieceType> boardInitList = List.of(ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK);
        int col = 1;

        for (ChessPiece.PieceType pieceType : boardInitList) {
            ChessPosition curPosWhite = new ChessPosition(1, col);
            ChessPosition curPosBlack = new ChessPosition(8, col);
            ChessPiece curPieceWhite = new ChessPiece(ChessGame.TeamColor.WHITE, pieceType);
            ChessPiece curPieceBlack = new ChessPiece(ChessGame.TeamColor.BLACK, pieceType);
            addPiece(curPosWhite, curPieceWhite);
            addPiece(curPosBlack, curPieceBlack);
            col += 1;
        }

        for (int i = 1; i < 9; i++) {
            ChessPosition pawnPosWhite = new ChessPosition(2, i);
            ChessPiece pawnPieceWhite = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(pawnPosWhite, pawnPieceWhite);

            ChessPosition pawnPosBlack = new ChessPosition(7, i);
            ChessPiece pawnPieceBlack = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(pawnPosBlack, pawnPieceBlack);
        }
    }


    public ChessBoard copyBoard(ChessBoard realBoard) {
        ChessBoard boardCopy = new ChessBoard();
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = realBoard.getPiece(curPos);
                if (curPiece != null) {
                    // copy chess piece and add to boardCopy
                    ChessPiece curPieceCopy = copyPiece(curPiece);
                    boardCopy.addPiece(curPos, curPieceCopy);
                }
            }
        }
        return boardCopy;

    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.deepToString(squares) +
                '}';
    }
}
