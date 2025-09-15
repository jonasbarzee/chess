package chess.piecemoves;

// need to know: chess piece type, the position

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;

public interface PieceMovesCalculator {

    public Collection<ChessMove> PieceMove (ChessPosition position, ChessBoard board);

}
