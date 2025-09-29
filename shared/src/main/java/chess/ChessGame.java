package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;
    private boolean isInCheckWhite;
    private boolean isInCheckBlack;
    private boolean isInCheckmateWhite;
    private boolean isInCheckmateBlack;
    private boolean isInStalemateWhite;
    private boolean isInStalemateBlack;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        turn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        switch (team) {
            case WHITE -> turn = TeamColor.WHITE;
            case BLACK -> turn = TeamColor.BLACK;
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE, BLACK
    }

    private void isInCheckHelper(TeamColor teamColor, ChessMove curMove, ChessMove newMove) {

        if (newMove.getEndPosition() == curMove.getEndPosition()) {
            switch (teamColor) {
                case WHITE -> isInCheckWhite = true;
                case BLACK -> isInCheckBlack = true;
            }
        } else {
            switch (teamColor) {
                case WHITE -> isInCheckWhite = false;
                case BLACK -> isInCheckBlack = false;
            }
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        /*
        for each square
            if enemy
                if enemyMoveEndPos == kingMoveEndPos
                    inCheck = ture;
         */
        switch (teamColor) {
            case WHITE -> {
                return isInCheckWhite;
            }
            case BLACK -> {
                return isInCheckBlack;
            }
            default -> {
                return true;
            }
        }
    }


    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        /*
        validMoves is a filter method that filters the moves that come back from the pieceMoves of each specific piece.
        for each pieceMove check to see if the copied move on the copied board puts the king in check. if it does the move isn't valid
        return back the collection of chessMoves
         */
        ChessPiece curPiece = board.getPiece(startPosition);
        TeamColor curPieceColor = curPiece.getTeamColor();
        List<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> curPieceMoves = curPiece.pieceMoves(board, startPosition);

        for (ChessMove curMove : curPieceMoves) {
            for (int row = 1; row < 9; row++) {
                for (int col = 1; col < 9; col++) {
                    ChessPosition newPieceStartPos = new ChessPosition(row, col);
                    ChessPiece newPiece = board.getPiece(newPieceStartPos);
                    if (newPiece != null) {
                        Collection<ChessMove> newPieceMoves = newPiece.pieceMoves(board, newPieceStartPos);
                        for (ChessMove newMove : newPieceMoves) {
                            isInCheckHelper(curPieceColor, curMove, newMove);
                            switch (curPieceColor) {
                                case TeamColor.WHITE -> {
                                    if (!isInCheckWhite && !validMoves.contains(curMove)) {
                                        validMoves.add(curMove);
                                    }
                                }
                                case TeamColor.BLACK -> {
                                    if (!isInCheckBlack && !validMoves.contains(curMove)) {
                                        validMoves.add(curMove);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return validMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        /*
        given a move calls the validMoves method to see if the move is valid i.e. in the collection of validMoves
        if it is valid then update the board
         */
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        switch (teamColor) {
            case WHITE -> {
                return isInCheckmateWhite;
            }
            case BLACK -> {
                return isInCheckmateBlack;
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // make a class called "CheckStalemate" that takes isInStalemateWhite and
        // isInStalemateBlack, modifies them depending on the game board
        switch (teamColor) {
            case WHITE -> {
                return isInStalemateWhite;
            }
            case BLACK -> {
                return isInStalemateBlack;
            }
            default -> {
                return true;
            }
        }
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.deepEquals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
    }

    @Override
    public String toString() {
        return "ChessGame{" + "turn=" + turn + ", board=" + board + '}';
    }
}
