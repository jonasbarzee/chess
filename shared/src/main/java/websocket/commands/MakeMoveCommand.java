package websocket.commands;

import chess.ChessMove;
import chess.ChessPiece;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final ChessPiece.PieceType promoPiece;
    private final String prettyMove;

    public MakeMoveCommand (String authToken, int gameId, ChessMove move, String playerColor, ChessPiece.PieceType promoPiece, String prettyMove) {
        super(CommandType.MAKE_MOVE, authToken, gameId, playerColor);
        this.promoPiece = promoPiece;
        this.move = move;
        this.prettyMove = prettyMove;
    }
    public ChessMove getMove() {
        return move;
    }

    public ChessPiece.PieceType getPromoPiece() {
        return promoPiece;
    }

    public String getPrettyMove() {
        return prettyMove;
    }
}
