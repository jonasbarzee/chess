package chess.piecemoves;

// need to know: chess piece type, the position

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;

public interface PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves (ChessPosition position, ChessBoard board);
    default public boolean inBounds(ChessMove move)  {
        int col = move.getEndPosition().getColumn();
        int row = move.getEndPosition().getRow();

        if ((row > 8 || col > 8) || (row < 1 || col < 1)) {
            return false;
        }
        return true;
    };

}
