package ui;

import chess.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class BoardPrinter {

    private static final String WHITE_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_RED;
    private static final String BLACK_PIECE_COLOR = EscapeSequences.SET_TEXT_COLOR_BLUE;
    private static final String TEXT_BOLD = EscapeSequences.SET_TEXT_BOLD;
    private static final String RESET_TEXT_BOLD = EscapeSequences.RESET_TEXT_BOLD_FAINT;
    private static final String WHITE_TEXT = EscapeSequences.SET_TEXT_COLOR_WHITE;
    private static final String RESET_TEXT = EscapeSequences.RESET_TEXT_COLOR;
    private static final String DARK_BACK = EscapeSequences.SET_BG_COLOR_BLACK;
    private static final String LIGHT_BACK = EscapeSequences.SET_BG_COLOR_WHITE;
    private static final String RESET_BACK = EscapeSequences.RESET_BG_COLOR;

    public static void printBoard(ChessBoard board, boolean white) {
        printBoard(board, white, null, Collections.emptySet());
    }

    public static void printBoard(ChessBoard board, boolean white, ChessPosition highlightFrom, Set<ChessPosition> highlightTo) {
        System.out.println("\n");
        printBoardHeader(white);

        for (int dr = 0; dr < 8; dr++) {
            int row = mapRow(dr, white);

            System.out.print(WHITE_TEXT + " " + row + " ");

            for (int dc = 0; dc < 8; dc++) {
                int col = mapCol(dc, white);

                ChessPosition position = new ChessPosition(row, col);

                boolean highlightFromSquare = highlightFrom != null && highlightFrom.equals(position);
                boolean highlightToSquare = highlightTo != null && highlightTo.contains(position);

                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                boolean darkSquare = (row + col) % 2 == 0;

                printSquare(piece, darkSquare, highlightFromSquare, highlightToSquare);
            }
            System.out.println(" " + row);
        }
        printBoardFooter(white);
    }

    private static void printBoardHeader(boolean white) {
        System.out.println(white ? WHITE_TEXT + "    a  b  c  d  e  f  g  h" : WHITE_TEXT + "    h  g  f  e  d  c  b  a");

    }

    private static void printBoardFooter(boolean white) {
        System.out.println(white ? WHITE_TEXT + "    a  b  c  d  e  f  g  h" : WHITE_TEXT + "    h  g  f  e  d  c  b  a");
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
            return RESET_TEXT;
        }
        return piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PIECE_COLOR : BLACK_PIECE_COLOR;
    }

    private static void printSquare(ChessPiece piece, boolean isDark, boolean highlightFrom, boolean highlightTo) {
        String bg;

        if (highlightFrom) {
            bg = "\u001B[42m";
        } else if (highlightTo) {
            bg = "\u001B[43m";
        } else {
            bg = isDark ? DARK_BACK : LIGHT_BACK;
        }

        String content = renderPiece(piece);
        String color = renderColor(piece);

        System.out.print(color + bg + TEXT_BOLD + " " + content + " " + RESET_BACK + RESET_TEXT + RESET_TEXT_BOLD);
    }
}
