package chess;


import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn;
    private ChessBoard board;

    public ChessGame() {

        turn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();

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
            case WHITE -> {
                turn = TeamColor.WHITE;
            }
            case BLACK -> {
                turn = TeamColor.BLACK;
            }
        }
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        } else if (getTeamTurn() != board.getPiece(startPosition).getTeamColor())
            return null;:wq
        return new ArrayList<>();
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        if (validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException ("No piece at ChessMove startPos.");
        } else if (validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException ("Trying to move a piece when it is not its teams turn.");
        } else if (isInCheck(board.getPiece(move.getStartPosition()).getTeamColor())) {
            throw new InvalidMoveException ("Move puts allied king in check.");
        } else if (board.getPiece(move.getStartPosition()) != null && board.getPiece(move.getEndPosition()) != null && board.getPiece(move.getStartPosition()).getTeamColor() == board.getPiece(move.getEndPosition()).getTeamColor()) {
            throw new InvalidMoveException("Move takes own color.");
        }
        else {
            ChessPiece curPiece = board.getPiece(move.getStartPosition());
            board.addPiece(move.getEndPosition(), curPiece);
            board.removePiece(move.getStartPosition());
        }
//        TeamColor turn = (getTeamTurn() == TeamColor.WHITE) ? setTeamTurn(TeamColor.BLACK): setTeamTurn(TeamColor.WHITE);

        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;
        Collection<ChessMove> allPieceMoves = new ArrayList<>();

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = board.getPiece(curPos);
                if (curPiece != null && curPiece.getPieceType() == ChessPiece.PieceType.KING && curPiece.getTeamColor() == teamColor) {
                    kingPos = curPos;
                    break;
                }
            }
            if (kingPos != null) {
                break;
            }
        }
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = board.getPiece(curPos);
                if (curPiece != null && curPiece.getTeamColor() != board.getPiece(kingPos).getTeamColor()) {
                    Collection<ChessMove> curPieceMoves = curPiece.pieceMoves(board, curPos);
                    allPieceMoves.addAll(curPieceMoves);
                }
            }
        }
        for (ChessMove move : allPieceMoves) {
            ChessPosition moveEndPos = move.getEndPosition();
            if (moveEndPos.equals(kingPos)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
    public String toString() {
        return "ChessGame{" +
                "turn=" + turn +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.deepEquals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }
}
