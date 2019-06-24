
package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;

import java.util.List;
import java.util.Locale;

import static com.github.tetrisanalyzer.game.StringUtils.format;

public class GameState {
    public Duration duration;

    public final Board board;
    public final Board startBoard;
    public final Distribution distribution;
    public ColoredBoard coloredBoard;
    public ColoredBoard coloredStartBoard;
    public final BoardEvaluator boardEvaluator;
    public final PieceGenerator pieceGenerator;
    public final int masterDepth;
    public double totalEquityDiff;
    public final int level;
    public final int numberOfKnownPieces;
    public final List<String> nextPieces;
    public long pieces;
    public long totalPieces;
    public Long gamesToPlay;
    public Long piecesToPlay;
    public long games;
    public long rows;
    public long minRows;
    public long maxRows;
    public long totalRows;
    public XPerLastSeconds rowsPerLastSecond;
    public XPerLastSeconds piecesPerLastSecond;

    public GameState(Duration duration, ColoredBoard coloredBoard, ColoredBoard coloredStartBoard,
                     Distribution distribution, BoardEvaluator boardEvaluator, PieceGenerator pieceGenerator,
                     int masterDepth, double totalEquityDiff,
                     int level, int numberOfKnownPieces, List<String> nextPieces,
                     long numberOfGames, long numberOfPieces, long totalPieces,
                     long rows, long totalRows, long minRows, long maxRows, Long gamesToPlay, Long piecesToPlay) {
        this.duration = duration;
        this.coloredBoard = coloredBoard;
        this.startBoard = coloredStartBoard.asBoard();
        this.coloredStartBoard = coloredStartBoard;
        this.distribution = distribution;
        this.board = coloredBoard.asBoard();
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = pieceGenerator;
        this.masterDepth = masterDepth;
        this.totalEquityDiff = totalEquityDiff;
        this.level = level;
        this.numberOfKnownPieces = numberOfKnownPieces;
        this.nextPieces = nextPieces;
        this.games = numberOfGames;
        this.pieces = numberOfPieces;
        this.totalPieces = totalPieces;
        this.rows = rows;
        this.totalRows = totalRows;
        this.minRows = minRows;
        this.maxRows = maxRows;
        this.gamesToPlay = gamesToPlay;
        this.piecesToPlay = piecesToPlay;

        resetSpeedometer();
    }

    public void resetSpeedometer() {
        rowsPerLastSecond = new XPerLastSeconds(duration.startMillis, rows + totalRows);
        piecesPerLastSecond = new XPerLastSeconds(duration.startMillis, totalPieces);
    }

    public String games() {
        return games == 0 ? "" : String.valueOf(games);
    }

    public String rowsPerLastSecondsFormatted() {
        return StringUtils.format(rowsPerLastSecond.xPerSecond);
    }

    public String piecesPerSecond() {
        return duration.xPerSecond(totalPieces);
    }

    public String piecesPerLastSecondsFormatted() {
        return StringUtils.format(piecesPerLastSecond.xPerSecond);
    }

    public String minRows() {
        return minRows == Long.MAX_VALUE ? "" : String.valueOf(minRows);
    }

    public String maxRows() {
        return maxRows == Long.MIN_VALUE ? "" : String.valueOf(maxRows);
    }

    public String minRowsFormatted() {
        return minRows == Long.MAX_VALUE ? "" : format(minRows);
    }

    public String maxRowsFormatted() {
        return maxRows == Long.MIN_VALUE ? "" : format(maxRows);
    }

    public double equityDiffPerPiece() {
        return totalEquityDiff / totalPieces;
    }

    public String equityDiffPerPieceFormatted() {
        return format(equityDiffPerPiece());
    }

    public double area() {
        return distribution.area();
    }

    public String areaFormatted() {
        return String.format(Locale.ENGLISH, "%.8f", area());
    }

    public GameState copy() {
        return new GameState(duration, board, startBoard, distribution, coloredBoard, boardEvaluator, pieceGenerator,
            masterDepth, totalEquityDiff, level, numberOfKnownPieces, nextPieces, totalPieces,
                gamesToPlay, piecesToPlay, games, rows, minRows, maxRows, totalRows);
    }

    private GameState(Duration duration, Board board, Board startBoard, Distribution distribution,
                      ColoredBoard coloredBoard, BoardEvaluator boardEvaluator, PieceGenerator pieceGenerator,
                      int masterDepth, double totalEquityDiff, int level, int numberOfKnownPieces, List<String> nextPieces,
                      long totalPieces, Long gamesToPlay, Long piecesToPlay,
                      long games, long rows, long minRows, long maxRows, long totalRows) {
        this.duration = duration;
        this.board = board.copy();
        this.startBoard = startBoard.copy();
        this.distribution = distribution.copy();
        this.coloredBoard = coloredBoard == null ? null : coloredBoard.copy();
        this.coloredStartBoard = coloredStartBoard.copy();
        this.boardEvaluator = boardEvaluator;
        this.pieceGenerator = pieceGenerator.copy();
        this.masterDepth = masterDepth;
        this.totalEquityDiff = totalEquityDiff;
        this.level = level;
        this.numberOfKnownPieces = numberOfKnownPieces;
        this.nextPieces = nextPieces;
        this.totalPieces = totalPieces;
        this.gamesToPlay = gamesToPlay;
        this.piecesToPlay = piecesToPlay;
        this.games = games;
        this.rows = rows;
        this.minRows = minRows;
        this.maxRows = maxRows;
        this.totalRows = totalRows;
    }

    public double rowsPerGame() {
        return (double)totalRows / games;
    }

    public String rowsPerGameString() {
        if (games == 0) {
            return "";
        }
        return String.valueOf(rowsPerGame());
    }

    public String rowsPerGameFormatted() {
        if (games == 0) {
            return "";
        }
        return format((double) totalRows / games);
    }

    public boolean hasGamesLeftToPlay() {
        return gamesToPlay == null || games < gamesToPlay;
    }

    public boolean hasPiecesLeftToPlay() {
        return piecesToPlay == null || pieces < piecesToPlay;
    }

    private String board() {
        String result = "\n  board size: [" + board.width + "," + board.height + "]";
        if (!board.isBoardEmpty()) {
            if (coloredBoard == null) {
                result += board.export("start board", "    ");
            } else {
                result += coloredBoard.export("start board", "    ");
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "duration=" + duration +
                ", board=" + board +
                ", coloredBoard=" + coloredBoard +
                ", boardEvaluator=" + boardEvaluator +
                ", pieceGenerator=" + pieceGenerator +
                ", masterDepth=" + masterDepth +
                ", totalEquityDiff=" + totalEquityDiff +
                ", level=" + level +
                ", numberOfKnownPieces" + numberOfKnownPieces +
                ", nextPieces=" + nextPieces +
                ", pieces=" + pieces +
                ", totalPieces=" + totalPieces +
                ", gamesToPlay=" + gamesToPlay +
                ", piecesToPlay=" + piecesToPlay +
                ", games=" + games +
                ", rows=" + rows +
                ", minRowsFormatted=" + minRows +
                ", maxRowsFormatted=" + maxRows +
                ", totalRows=" + totalRows +
                '}';
    }
}
