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

        // make the move on the cloned board, see if the king is in check, if yes then remove the move from the validMoves, if not in check leave the move in the validMoves collection.
        for (ChessMove curMove : curPieceMovesCopy) {
            makeMoveOnCloneBoard(curMove);
            System.out.println("Calling isInCheck from validMoves");
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


        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("No piece at ChessMove startPos.");

            // Validating that the current teams turn is the same as the piece that is trying to move as to not move a piece out of its turn.
        }
        if (getTeamTurn() != board.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("Trying to move a piece when it is not its teams turn.");
        }
        if (validMoves(move.getStartPosition()).contains(move)) {
            ChessPiece curPiece = board.getPiece(move.getStartPosition());
            if (curPiece.getPieceType() == ChessPiece.PieceType.PAWN && (move.getEndPosition().getRow() == 1 || move.getEndPosition().getRow() == 8)) {
                System.out.println("Piece is a pawn and we are promoting");
                board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece()));
                board.removePiece(move.getStartPosition());

            } else {
                board.addPiece(move.getEndPosition(), curPiece);
                board.removePiece(move.getStartPosition());
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
        System.out.println(board.toString());
        System.out.println(clonedBoard.toString());

        for (ChessMove move : allPieceMoves) {
            ChessPosition moveEndPos = move.getEndPosition();
            if (moveEndPos.equals(kingPos)) {
                System.out.println("Returning true from isInCheck");
                return true;
            }
        }
        System.out.println("Returning false from isInCheck");
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        System.out.println("Calling isInCheck from isInCheckmate (first call)");
        if (isInCheck(teamColor)) {
            // check all kings moves
            ChessPosition kingPos = getKingPos(teamColor);
            Collection<ChessMove> enemyPieceMoves = getEnemyMoves(kingPos);
            Collection<ChessMove> allyPieceMoves = getAllyMoves(teamColor);

            ChessPiece king = board.getPiece(kingPos);

            for (ChessMove allyMove : allyPieceMoves) {
                for (ChessMove enemyMove : enemyPieceMoves) {
                    if (allyMove.getEndPosition().equals(enemyMove.getStartPosition())) {
                        // do the king move on the simulated board and see if the king is now out of check
                        makeMoveOnCloneBoard(allyMove);
                        System.out.println("Calling isInCheck from isInCheckmate (second call)");
                        if (!isInCheck(teamColor)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;

    }

    private Collection<ChessPosition> getAllyPositions(TeamColor teamColor) {
        Collection<ChessPosition> allyPositions = new ArrayList<>();

        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition curPos = new ChessPosition(row, col);
                ChessPiece curPiece = clonedBoard.getPiece(curPos);
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

        System.out.println("In isInStalemate");
        Collection<ChessMove> allValidMoves = new ArrayList<>();

        Collection<ChessPosition> allyPositions = getAllyPositions(teamColor);
        for (ChessPosition allyPosition : allyPositions) {
            allValidMoves.addAll(validMoves(allyPosition));
        }

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
