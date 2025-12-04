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
    private ChessBoard clonedBoard;
    private boolean isGameOver = false;

    public ChessGame() {
        turn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
        clonedBoard = board;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    public void setGameOver() {
        isGameOver = true;
    }

    public boolean getIsGameOver() {
        return isGameOver;
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

    private void copyBoard() {
        clonedBoard = board.copyBoard(board);
    }

    private void makeMoveOnCloneBoard(ChessMove move) {
        // calls cloneBoard to get a new cloned chess board then makes the move on the cloned board
        copyBoard();
        ChessPiece pieceToMove = clonedBoard.getPiece(move.getStartPosition());
        clonedBoard.addPiece(move.getEndPosition(), pieceToMove);
        clonedBoard.removePiece(move.getStartPosition());

    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Making curPieceMoves and curPieceMovesCopy to avoid modifying a collection while iterating through the collection.
        ChessPiece curPiece = board.getPiece(startPosition);
        Collection<ChessMove> curPieceMoves = curPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> curPieceMovesCopy = curPiece.pieceMoves(board, startPosition);

        // make the move on the cloned board, see if the king is in check
        // if yes then remove the move from the validMoves, if not in check leave the move in the validMoves collection.
        for (ChessMove curMove : curPieceMovesCopy) {
            makeMoveOnCloneBoard(curMove);
            if (isInCheck(clonedBoard.getPiece(curMove.getEndPosition()).getTeamColor())) {
                curPieceMoves.remove(curMove);
            }
        }
        return curPieceMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // Checking to see if there is a ChessPiece at the starting position as to not use a method on a null pointer.
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();


        if (board.getPiece(startPos) == null) {
            throw new InvalidMoveException("No piece at ChessMove startPos.");

        }
        if (getTeamTurn() != board.getPiece(startPos).getTeamColor()) {
            throw new InvalidMoveException("Trying to move a piece when it is not its teams turn.");
        }
        if (validMoves(startPos).contains(move)) {
            ChessPiece curPiece = board.getPiece(startPos);
            if (curPiece.getPieceType() == ChessPiece.PieceType.PAWN && (endPos.getRow() == 1 || endPos.getRow() == 8)) {
                board.addPiece(endPos, new ChessPiece(board.getPiece(startPos).getTeamColor(), move.getPromotionPiece()));
                board.removePiece(startPos);

            } else {
                board.addPiece(endPos, curPiece);
                board.removePiece(startPos);
            }
        } else {
            throw new InvalidMoveException("error");
        }
        switch (turn) {
            case WHITE -> setTeamTurn(TeamColor.BLACK);
            case BLACK -> setTeamTurn(TeamColor.WHITE);
        }
    }

    private ChessPosition getKingPos(TeamColor teamColor) {
        ChessPosition kingPos = null;

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = clonedBoard.getPiece(curPos);
                if (curPiece != null && curPiece.getPieceType() == ChessPiece.PieceType.KING && curPiece.getTeamColor() == teamColor) {
                    kingPos = curPos;
                    break;
                }
            }
            if (kingPos != null) {
                break;
            }
        }
        return kingPos;
    }

    private Collection<ChessMove> getEnemyMoves(ChessPosition kingPos) {
        Collection<ChessMove> allPieceMoves = new ArrayList<>();

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = clonedBoard.getPiece(curPos);
                if (curPiece != null && curPiece.getTeamColor() != clonedBoard.getPiece(kingPos).getTeamColor()) {
                    Collection<ChessMove> curPieceMoves = curPiece.pieceMoves(clonedBoard, curPos);
                    allPieceMoves.addAll(curPieceMoves);
                }
            }
        }
        return allPieceMoves;
    }

    private Collection<ChessMove> getAllyMoves(TeamColor teamColor) {
        Collection<ChessMove> allPieceMoves = new ArrayList<>();

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = clonedBoard.getPiece(curPos);
                if (curPiece != null && curPiece.getTeamColor() == teamColor) {
                    Collection<ChessMove> curPieceMoves = curPiece.pieceMoves(clonedBoard, curPos);
                    allPieceMoves.addAll(curPieceMoves);
                }
            }
        }
        return allPieceMoves;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // Takes the team color, finds that teams king, finds all enemies and their movesets, verifies that the current team's king is not in check.
        ChessPosition kingPos = getKingPos(teamColor);
        Collection<ChessMove> allPieceMoves = getEnemyMoves(kingPos);


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
        if (!isInCheck(teamColor)) { // if king is not in check then king cannot be in checkmate
            return false;
        }

        // check all possible moves of allied piece to see if king can get out of check
        ChessPosition kingPos = getKingPos(teamColor);
        Collection<ChessMove> enemyPieceMoves = getEnemyMoves(kingPos);
        Collection<ChessMove> allyPieceMoves = getAllyMoves(teamColor);

        for (ChessMove allyMove : allyPieceMoves) {
            for (ChessMove enemyMove : enemyPieceMoves) {
                if (allyMove.getEndPosition().equals(enemyMove.getStartPosition())) {
                    // do the king move on the simulated board and see if the king is now out of check
                    makeMoveOnCloneBoard(allyMove);
                    if (!isInCheck(teamColor)) {
                        return false;
                    }
                }
            }
        }
        return true;


    }

    private Collection<ChessPosition> getAllyPositions(TeamColor teamColor) {
        Collection<ChessPosition> allyPositions = new ArrayList<>();

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = board.getPiece(curPos);
                if (curPiece != null && curPiece.getTeamColor() == teamColor) {
                    allyPositions.add(curPos);
                }
            }
        }
        return allyPositions;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        Collection<ChessMove> allValidMoves = new ArrayList<>();

        Collection<ChessPosition> allyPositions = getAllyPositions(teamColor);
        for (ChessPosition allyPosition : allyPositions) {
            allValidMoves.addAll(validMoves(allyPosition));
        }

        copyBoard();
        if (!isInCheck(teamColor) && (allValidMoves.isEmpty())) {
            return true;
        }
        return false;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        clonedBoard = this.board;
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
