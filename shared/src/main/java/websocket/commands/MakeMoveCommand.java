package websocket.commands;

import chess.ChessMove;
import chess.ChessPiece;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final ChessPiece.PieceType promoPiece;

    public MakeMoveCommand (String authToken, int gameId, ChessMove move, String playerColor, ChessPiece.PieceType promoPiece) {
        super(CommandType.MAKE_MOVE, authToken, gameId, playerColor);
        this.promoPiece = promoPiece;
        this.move = move;
    }
    public ChessMove getMove() {
        return move;
    }

    public ChessPiece.PieceType getPromoPiece() {
        return promoPiece;
    }
}
