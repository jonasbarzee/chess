package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private ChessMove move;

    public MakeMoveCommand (String authToken, int gameId, ChessMove move, String playerColor) {
        super(CommandType.MAKE_MOVE, authToken, gameId, playerColor);
        this.move = move;
    }

    public ChessMove getMove() {
        return move;
    }
}
