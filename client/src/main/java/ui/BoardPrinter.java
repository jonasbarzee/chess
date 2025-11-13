package ui;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardPrinter {

    private static void printBoardHeader(boolean isWhitePerspective) {
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.println(isWhitePerspective ? "    a  b  c  d  e  f  g  h" : "    h  g  f  e  d  c  b  a");

    }

    private static void printBoardFooter(boolean isWhitePerspective) {
        System.out.println(isWhitePerspective ? "    a  b  c  d  e  f  g  h" : "    h  g  f  e  d  c  b  a");
        System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
    }

    private static void printBoardHelper(ChessBoard board, int row, boolean isWhitePerspective) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition pos = new ChessPosition(row, col);
            ChessPiece piece = board.getPiece(pos);

            boolean dark = isWhitePerspective ? (row + col) % 2 == 0 : (row + col + 1) % 2 == 0;
            String background = dark ? EscapeSequences.SET_BG_COLOR_DARK_GREY : EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
            if (piece == null) {
                System.out.print(background + "   " + EscapeSequences.SET_BG_COLOR_BLACK);
            } else if (!isWhitePerspective) {
                if (piece.toLetter().equals("K")) {
                    System.out.print(background + " " + "Q" + " " + EscapeSequences.SET_BG_COLOR_BLACK);
                } else if (piece.toLetter().equals("Q")) {
                    System.out.print(background + " " + "K" + " " + EscapeSequences.SET_BG_COLOR_BLACK);
                } else {
                    System.out.print(background + " " + piece.toLetter() + " " + EscapeSequences.SET_BG_COLOR_BLACK);
                }

            } else {
                System.out.print(background + " " + piece.toLetter() + " " + EscapeSequences.SET_BG_COLOR_BLACK);
            }
        }
    }
    public static void printBoard(ChessBoard board, boolean isWhitePerspective) {
        printBoardHeader(isWhitePerspective);
        for (int row = isWhitePerspective ? 8 : 1; isWhitePerspective ? row >= 1 : row <= 8; row += isWhitePerspective ? -1 : 1) {
            System.out.print(EscapeSequences.SET_BG_COLOR_BLACK + " " + row + " ");
            printBoardHelper(board, row, isWhitePerspective);
            System.out.print(" " + row);
            System.out.println();
        }
        printBoardFooter(isWhitePerspective);
    }
}
