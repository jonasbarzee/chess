package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPrinter {

    private static final String whitePieceColor = EscapeSequences.SET_TEXT_COLOR_RED;
    private static final String blackPieceColor = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private static final String textBold = EscapeSequences.SET_TEXT_BOLD;
    private static final String resetTextBold = EscapeSequences.RESET_TEXT_BOLD_FAINT;
    private static final String whiteText = EscapeSequences.SET_TEXT_COLOR_WHITE;
    private static final String resetText = EscapeSequences.RESET_TEXT_COLOR;
    private static final String darkBack = EscapeSequences.SET_BG_COLOR_BLACK;
    private static final String lightBack = EscapeSequences.SET_BG_COLOR_WHITE;
    private static final String resetBack = EscapeSequences.RESET_BG_COLOR;

    public static void printBoard(ChessBoard board, boolean white) {
        printBoardHeader(white);

        for (int dr = 0; dr < 8; dr++) {
            int row = mapRow(dr, white);

            System.out.print(whiteText + " " + row + " ");

            for (int dc = 0; dc < 8; dc++) {
                int col = mapCol(dc, white);

                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                boolean darkSquare = (row + col) % 2 == 0;

                printSquare(piece, darkSquare);
            }
            System.out.println(" " + row);
        }
        printBoardFooter(white);
    }

    private static void printBoardHeader(boolean white) {
        System.out.println(white ? whiteText + "    a  b  c  d  e  f  g  h" : whiteText + "    h  g  f  e  d  c  b  a");

    }

    private static void printBoardFooter(boolean white) {
        System.out.println(white ? whiteText + "    a  b  c  d  e  f  g  h" : whiteText + "    h  g  f  e  d  c  b  a");
    }

    private static int mapRow(int displayRow, boolean white) {
        return white ? 8 - displayRow : 1 + displayRow;
    }

    private static int mapCol(int displayCol, boolean white) {
        return white ? 1 + displayCol : 8 - displayCol;
    }

    private static String renderPiece(ChessPiece piece) {
        if (piece == null) {
            return " ";
        }
        return piece.toLetter();
    }

    private static String renderColor(ChessPiece piece) {
        if (piece == null) {
            return resetText;
        }
        return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? whitePieceColor : blackPieceColor;
    }

    private static void printSquare(ChessPiece piece, boolean isDark) {
        String bg = isDark ? darkBack : lightBack;
        String content = renderPiece(piece);
        String color = renderColor(piece);
        System.out.print(color + bg + textBold + " " + content + " " + resetBack + resetText + resetTextBold);
    }
}
