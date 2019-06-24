package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;

import java.util.Arrays;

/**
 * Represents a Tetris board. Default size is 10x20.
 *
 * Each row is represented by a 64 bit integer where each bit corresponds to a column on the board.
 * Bit 0 corresponds to the x-value 0 (left most position), bit 1 to x-value 1 etc.
 *
 * This is a highly optimized version that does not follow best practice in object-orientation!
 */
public class Board implements TextBoard {
    public int width;
    public int height;
    private long completeRow;
    private long[] rows;

    private static long EMPTY_ROW = 0;

    public static Board create() {
        return new Board(10, 20);
    }

    public static Board create(int width, int height) {
        return new Board(width, height);
    }

    /**
     * Creates a board from a string representation.
     */
    public static Board create(String... rows) {
        int width = (rows[0]).length() - 2;
        int height = rows.length - 1;

        long[] boardRows = new long[height];
        for (int y=0; y<height; y++) {
            boardRows[y] = rowFromText(width, rows[y]);
        }
        Board result = new Board(width,  height, boardRows);

        return result;
    }

    /**
     * Creates an empty board
     */
    private Board(int width, int height) {
        this(width, height, emptyBoard(height));
    }

    private Board(int width, int height, long[] rows) {
        if (width < 4 || width > 64) {
            throw new IllegalArgumentException("The board width must be in the range 4 to 64");
        }
        if (height < 4 || height > 128) {
            throw new IllegalArgumentException("The board height must be in the range 4 to 128");
        }
        this.width = width;
        this.height = height;
        this.rows = rows;
        completeRow = calculateCompleteRow(width);
    }

    public Board copy() {
        return new Board(width, height, copy(rows));
    }

    private static long[] emptyBoard(int height) {
        long[] rows = new long[height];
        Arrays.fill(rows, 0);
        return rows;
    }

    private static long[] copy(long[] sourceRows) {
        long[] newRows = new long[sourceRows.length];
        System.arraycopy(sourceRows, 0, newRows, 0, sourceRows.length);
        return newRows;
    }

    public static String bottomString(int chars) {
        return new String(new char[chars]).replace("\0", "=");
    }

    public static String bottomTextRow(int width) {
        return bottomString(width + 2);
    }

    private static long rowFromText(int width, String textRow) {
        long row = EMPTY_ROW;
        for (int x=width; x>=1; x--) {
            row <<= 1L;
            row |= textRow.charAt(x) == '-' ? 0 : 1;
        }
        return row;
    }

    /**
     * Sets part of a piece on the board.
     *
     * @param y board row
     * @param pieceRowCells filled cells of a specific piece row
     */
    public void setBits(int y, long pieceRowCells) {
        rows[y] |= pieceRowCells;
    }

    /**
     * Cleares part of a piece on the board.
     *
     * @param y board row
     * @param inversePieceRowCells filled cells of a specific piece row
     */
    public void clearBits(int y, long inversePieceRowCells) {
        rows[y] &= inversePieceRowCells;
    }

    /**
     * @param y board row
     * @param pieceRowCells filled cells of a specific piece row
     * @return true if the piece row cells are not occupied on the board
     */
    public boolean isBitsFree(int y, long pieceRowCells) {
        try {
            return (rows[y] & pieceRowCells) == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private long calculateCompleteRow(int width) {
        long row = EMPTY_ROW;

        for (int x=0; x<width; x++) {
            row <<= 1L;
            row |= 1;
        }
        return row;
    }

    /**
     * True if the specified cell is not occupied.
     */
    public boolean isFree(int x, int y) {
        try {
            return (rows[y] & (1L << x)) == 0;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Clear all completed rows and return number of cleared rows.
     * This method is called after a piece has been placed on the board.
     *   pieceY: the y position of the piece.
     *   pieceHeight: height of the piece.
     */
    public int clearRows(int pieceY, int pieceHeight) {
        int clearedRows = 0;
        int y1 = pieceY + pieceHeight;

        // Find first row to clear
        do {
            y1--;
            if (rows[y1] == completeRow) {
                clearedRows++;
            }
        } while (clearedRows == 0 && y1 > pieceY);

        // Clear rows
        if (clearedRows > 0) {
            int y2 = y1;

            while (y1 >= 0) {
                y2--;
                while (y2 >= pieceY && rows[y2] == completeRow) {
                    clearedRows++;
                    y2--;
                }
                if (y2 >= 0) {
                    rows[y1] = rows[y2];
                } else {
                    rows[y1] = Board.EMPTY_ROW;
                }
                y1--;
            }
        }
        return clearedRows;
    }

    /**
     * Restores this (mutable) board from a another board.
     */
    public void restore(Board other) {
        System.arraycopy(other.rows, 0, rows, 0, rows.length);
    }

    /**
     * Converts a board row into its string representation.
     */
    public String boardRowAsString(long boardRow) {
        String result = "";

        for (int i=0; i<width; i++) {
            result += (((boardRow >> i) & 1) == 0) ? "-" : "x";
        }

        return result;
    }

    public String export(String title, String tab) {
        String result = "\n  " + title + ": \n" + tab + "[";
        String separator = "";

        for (int y=0; y<height; y++) {
            result += separator + "[" + boardRowAsString(rows[y]) + "]";
            separator = "\n" + tab + " ";
        }
        return result += "]";
    }

    public boolean isBoardEmpty() {
        for (int y=0; y<height; y++) {
            if (rows[y] != EMPTY_ROW) {
                return false;
            }
        }
        return true;
    }

    public int numberOfOccupiedCells(int y) {
        int cnt = 0;

        for (int x=0; x<width; x++) {
            if (!isFree(x, y)) {
                cnt++;
            }
        }
        return cnt;
    }

    public int numberOfOccupiedCells() {
        int cnt = 0;

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                if (!isFree(x, y)) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    /**
     * Returns the board as string and marks where
     * on the board the next piece will be placed.
     *
     *   I: 1,10
     *  |----------|
     *  |----------|
     *  |----------|
     *  |LL--------|
     *  |LL--------|
     *  |LLIIIIOOT-|
     *  |IJS-TTTLLL|
     *  ============
     */
    public String[] asStringRows(Piece piece, Move move) {
        String[] board = new String[height + 2];

        board[0] = " " + piece.character() + ": " + (move == null ? "-" : move.rotation + "," + (move.x + 1));

        for (int y=0; y<height; y++) {
            board[y+1] = "|" + boardRowAsString(rows[y]) + "|";
        }
        board[height+1] = bottomString(width + 2);

        return board;
    }

    public String asString(Piece piece, Move move) {
        String result = "";
        String separator = "";

        for (String row : asStringRows(piece, move)) {
            result += separator + row;
            separator = "\n";
        }
        return result;
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + (rows != null ? Arrays.hashCode(rows) : 0);

        return result;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null || getClass() != that.getClass()) return false;

        Board board = (Board) that;

        if (height != board.height) return false;
        if (width != board.width) return false;
        if (!Arrays.equals(rows, board.rows)) return false;

        return true;
    }

    @Override
    public String toString() {
        String board = "";
        for (int y=0; y<height; y++) {
            board += "|" + boardRowAsString(rows[y]) + "|" + "\n";
        }
        return board + bottomTextRow(width);
    }
}
