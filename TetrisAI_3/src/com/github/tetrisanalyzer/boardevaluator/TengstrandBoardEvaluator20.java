package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.BoardOutline;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Joakim Tengstrand's Tetris AI, version 2.0 (an experiment, work in progress!)
 */
public class TengstrandBoardEvaluator20 implements BoardEvaluator {
    public int boardWidth;
    public int boardHeight;
    public double maxEquity;
    public double maxEquityFactor = 1.21;

    public double outlineHeightFactor0 = 1.6;
    public double outlineHeightFactor1 = 1.3;
    public double outlineHeightFactor2 = 1.18;
    public double outlineHeightFactor3 = 1.12;
    public double outlineHeightFactor4 = 1.02;

    public double hollowHeightFactor0 = 1.9;
    public double hollowHeightFactor1 = 1;

    public double hollowFactor1 = 0.46;
    public double hollowFactor2 = 0.51;
    public double hollowFactor3 = 0.5;
    public double hollowFactor4 = 0.40;
    public double hollowFactorDelta = 0.85;

    public double areaWidthFactor1 = 4.978;
    public double areaWidthFactor2 = 2.38;
    public double areaWidthFactor3 = 3.01;
    public double areaWidthFactor4 = 2.21;
    public double areaWidthFactor5 = 2.05;
    public double areaWidthFactor6 = 1.87;
    public double areaWidthFactor7 = 1.52;
    public double areaWidthFactor8 = 1.34;
    public double areaWidthFactor9 = 1.18;

    public double areaHeightFactor1 = 0.5;
    public double areaHeightFactor2 = 1.19;
    public double areaHeightFactor3 = 2.35;
    public double areaHeightFactor4 = 3.1;
    public double areaHeightFactor5 = 4.6;
    public double areaHeightFactorDelta = 1.1;

    public double areaHeightEqFactor1 = 0.42;
    public double areaHeightEqFactor2 = 1.05;
    public double areaHeightEqFactor3 = 2.22;
    public double areaHeightEqFactor4 = 3.06;

    public String id;
    public String description;
    public String author;
    public String url;
    public String clazz;

    public double[] outlineHeightFactors;
    public double[] hollowHeightFactors;
    public double[] hollowFactors;
    public double[] areaWidthFactors;
    public double[] areaHeightFactors;
    public double[] areaHeightEqFactors;

    public TengstrandBoardEvaluator20(int boardWidth, int boardHeight, GameSettings rules) {
        init(boardWidth, boardHeight);
    }

    public TengstrandBoardEvaluator20(int boardWidth, int boardHeight, GameSettings rules, Map settings) {
        SettingsReader reader = new SettingsReader(settings, "board evaluators");

        id = reader.readString("id");
        description = reader.readString("description");
        author = reader.readString("author");
        url = reader.readString("url");
        clazz = reader.readString("class");

        maxEquityFactor = reader.readDouble("maxEquityFactor");
        outlineHeightFactor0 = reader.readDouble("outlineHeightFactor0");
        outlineHeightFactor1 = reader.readDouble("outlineHeightFactor1");
        outlineHeightFactor2 = reader.readDouble("outlineHeightFactor2");
        outlineHeightFactor3 = reader.readDouble("outlineHeightFactor3");
        outlineHeightFactor4 = reader.readDouble("outlineHeightFactor4");
        hollowHeightFactor0 = reader.readDouble("hollowHeightFactor0");
        hollowHeightFactor1 = reader.readDouble("hollowHeightFactor1");
        hollowFactor1 = reader.readDouble("hollowFactor1");
        hollowFactor2 = reader.readDouble("hollowFactor2");
        hollowFactor3 = reader.readDouble("hollowFactor3");
        hollowFactor4 = reader.readDouble("hollowFactor4");
        hollowFactorDelta = reader.readDouble("hollowFactorDelta");
        areaWidthFactor1 = reader.readDouble("areaWidthFactor1");
        areaWidthFactor2 = reader.readDouble("areaWidthFactor2");
        areaWidthFactor3 = reader.readDouble("areaWidthFactor3");
        areaWidthFactor4 = reader.readDouble("areaWidthFactor4");
        areaWidthFactor5 = reader.readDouble("areaWidthFactor5");
        areaWidthFactor6 = reader.readDouble("areaWidthFactor6");
        areaWidthFactor7 = reader.readDouble("areaWidthFactor7");
        areaWidthFactor8 = reader.readDouble("areaWidthFactor8");
        areaWidthFactor9 = reader.readDouble("areaWidthFactor9");
        areaHeightFactor1 = reader.readDouble("areaHeightFactor1");
        areaHeightEqFactor1 = reader.readDouble("areaHeightEqFactor1");
        areaHeightFactor2 = reader.readDouble("areaHeightFactor2");
        areaHeightEqFactor2 = reader.readDouble("areaHeightEqFactor2");
        areaHeightFactor3 = reader.readDouble("areaHeightFactor3");
        areaHeightEqFactor3 = reader.readDouble("areaHeightEqFactor3");
        areaHeightFactor4 = reader.readDouble("areaHeightFactor4");
        areaHeightEqFactor4 = reader.readDouble("areaHeightEqFactor4");
        areaHeightFactor5 = reader.readDouble("areaHeightFactor5");
        areaHeightFactorDelta = reader.readDouble("areaHeightFactorDelta");

        init(boardWidth, boardHeight);
    }

    private void init(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        outlineHeightFactors = new double[boardHeight < 5 ? 5 : boardHeight + 1];
        hollowHeightFactors = new double[boardHeight < 5 ? 5 : boardHeight + 1];
        hollowFactors = new double[boardWidth < 5 ? 5 : boardWidth];
        areaWidthFactors = new double[boardWidth < 10 ? 10 : boardWidth];
        areaHeightFactors = new double[boardHeight < 6 ? 6 : boardHeight + 1];
        areaHeightEqFactors = new double[boardHeight < 6 ? 6 : boardHeight + 1];

        maxEquity = boardWidth * (boardHeight - 1) * maxEquityFactor;

        initHeightOutlineFactors();
        initHeightHollowFactors();
        initHollowFactors();
        initAreaWidthFactors();
        initAreaHeightFactors();
        initAreaHeightEqFactors();
    }

    private void initHeightOutlineFactors() {
        outlineHeightFactors[0] = outlineHeightFactor0;
        outlineHeightFactors[1] = outlineHeightFactor1;
        outlineHeightFactors[2] = outlineHeightFactor2;
        outlineHeightFactors[3] = outlineHeightFactor3;
        outlineHeightFactors[4] = outlineHeightFactor4;
        for (int i=5; i< outlineHeightFactors.length; i++) {
            outlineHeightFactors[i] = 1;
        }
    }

    private void initHeightHollowFactors() {
        hollowHeightFactors[0] = hollowHeightFactor0;
        hollowHeightFactors[1] = hollowHeightFactor1;
        for (int i=2; i< hollowHeightFactors.length; i++) {
            hollowHeightFactors[i] = 1;
        }
    }

    private void initHollowFactors() {
        hollowFactors[1] = hollowFactor1;
        hollowFactors[2] = hollowFactor2;
        hollowFactors[3] = hollowFactor3;
        hollowFactors[4] = hollowFactor4;
        double factor = hollowFactor4;

        for (int i=5; i<hollowFactors.length; i++) {
            factor *= hollowFactorDelta;
            hollowFactors[i] = factor;
        }
    }

    private void initAreaWidthFactors() {
        areaWidthFactors[1] = areaWidthFactor1;
        areaWidthFactors[2] = areaWidthFactor2;
        areaWidthFactors[3] = areaWidthFactor3;
        areaWidthFactors[4] = areaWidthFactor4;
        areaWidthFactors[5] = areaWidthFactor5;
        areaWidthFactors[6] = areaWidthFactor6;
        areaWidthFactors[7] = areaWidthFactor7;
        areaWidthFactors[8] = areaWidthFactor8;
        areaWidthFactors[9] = areaWidthFactor9;
        double factor = areaWidthFactor9;
        double delta = areaWidthFactor8 - areaWidthFactor9;

        for (int i=10; i<areaWidthFactors.length; i++) {
            factor -= delta;
            areaWidthFactors[i] = factor;
        }
    }

    private void initAreaHeightFactors() {
        areaHeightFactors[1] = areaHeightFactor1;
        areaHeightFactors[2] = areaHeightFactor2;
        areaHeightFactors[3] = areaHeightFactor3;
        areaHeightFactors[4] = areaHeightFactor4;
        areaHeightFactors[5] = areaHeightFactor5;

        double factor = areaHeightFactor5;

        for (int i=6; i<areaHeightFactors.length; i++) {
            factor += areaHeightFactorDelta;
            areaHeightFactors[i] = factor;
        }
    }

    private void initAreaHeightEqFactors() {
        areaHeightEqFactors[1] = areaHeightEqFactor1;
        areaHeightEqFactors[2] = areaHeightEqFactor2;
        areaHeightEqFactors[3] = areaHeightEqFactor3;
        areaHeightEqFactors[4] = areaHeightEqFactor4;
        areaHeightEqFactors[5] = areaHeightFactor5;

        double factor = areaHeightFactor5;

        for (int i=6; i<areaHeightEqFactors.length; i++) {
            factor += areaHeightFactorDelta;
            areaHeightEqFactors[i] = factor;
        }
    }

    @Override
    public double maxEquity() {
        return maxEquity;
    }

    @Override
    public double evaluate(Board board, AllValidPieceMoves allValidPieceMoves)  {
        return allValidPieceMoves.adjustEquityIfOccupiedStartPiece(evaluate(board), maxEquity, board);
    }

    @Override
    public Map<String, String> parameters() {
        Map<String,String> parameters = new LinkedHashMap<>();

        parameters.put("id", id);
        parameters.put("description", description);
        parameters.put("author", author);
        parameters.put("url", url);
        parameters.put("class", clazz);

        parameters.put("maxEquityFactor", Double.toString(maxEquityFactor));
        parameters.put("outlineHeightFactor0", Double.toString(outlineHeightFactor0));
        parameters.put("outlineHeightFactor1", Double.toString(outlineHeightFactor1));
        parameters.put("outlineHeightFactor2", Double.toString(outlineHeightFactor2));
        parameters.put("outlineHeightFactor3", Double.toString(outlineHeightFactor3));
        parameters.put("outlineHeightFactor4", Double.toString(outlineHeightFactor4));
        parameters.put("hollowHeightFactor0", Double.toString(hollowHeightFactor0));
        parameters.put("hollowHeightFactor1", Double.toString(hollowHeightFactor1));
        for (int i=1; i<hollowFactors.length; i++) {
            parameters.put("hollowFactor" + (i), Double.toString(hollowFactors[i]));
        }
        parameters.put("hollowFactorDelta", Double.toString(hollowFactorDelta));
        parameters.put("areaWidthFactor1", Double.toString(areaWidthFactor1));
        parameters.put("areaWidthFactor2", Double.toString(areaWidthFactor2));
        parameters.put("areaWidthFactor3", Double.toString(areaWidthFactor3));
        parameters.put("areaWidthFactor4", Double.toString(areaWidthFactor4));
        parameters.put("areaWidthFactor5", Double.toString(areaWidthFactor5));
        parameters.put("areaWidthFactor6", Double.toString(areaWidthFactor6));
        parameters.put("areaWidthFactor7", Double.toString(areaWidthFactor7));
        parameters.put("areaWidthFactor8", Double.toString(areaWidthFactor8));
        parameters.put("areaWidthFactor9", Double.toString(areaWidthFactor9));
        parameters.put("areaHeightFactor1", Double.toString(areaHeightFactor1));
        parameters.put("areaHeightEqFactor1", Double.toString(areaHeightEqFactor1));
        parameters.put("areaHeightFactor2", Double.toString(areaHeightFactor2));
        parameters.put("areaHeightEqFactor2", Double.toString(areaHeightEqFactor2));
        parameters.put("areaHeightFactor3", Double.toString(areaHeightFactor3));
        parameters.put("areaHeightEqFactor3", Double.toString(areaHeightEqFactor3));
        parameters.put("areaHeightFactor4", Double.toString(areaHeightFactor4));
        parameters.put("areaHeightEqFactor4", Double.toString(areaHeightEqFactor4));
        parameters.put("areaHeightFactor5", Double.toString(areaHeightFactor5));
        parameters.put("areaHeightFactorDelta", Double.toString(areaHeightFactorDelta));

        return parameters;
    }

    public double evaluate(Board board)  {
        if (board.width > boardWidth) {
            throw new IllegalArgumentException("Can not evaluate board width > " + boardWidth);
        }
        BoardOutline outline = new BoardOutline(board);

        return evaluateBasedOnHollows(board, outline) +
               evaluateBasedOnOutlineStructure(outline);
    }

    private double evaluateBasedOnHollows(Board board, BoardOutline outline) {
        double equity = 0;
        double[] hollowFactorForRow = new double[boardHeight + 1];

        for (int y=outline.minY; y<boardHeight; y++) {
            int numberOfEmptySquaresPerRow = 0;
            int minYForOutlineHole = boardHeight;

            for (int x=0; x<boardWidth; x++) {
                if (board.isFree(x, y)) {
                    numberOfEmptySquaresPerRow++;
                    if (outline.get(x) < minYForOutlineHole && outline.get(x) < y) {
                        minYForOutlineHole = outline.get(x);
                    }
                }
            }
            hollowFactorForRow[y] = hollowFactors[numberOfEmptySquaresPerRow];

            if (minYForOutlineHole < boardHeight) {
                double hollowFactor = 1;

                for (int row=minYForOutlineHole; row<=y; row++) {
                    hollowFactor *= hollowFactorForRow[row];
                }
                equity += (1 - hollowFactor) * boardWidth * hollowHeightFactors[minYForOutlineHole];
            }
        }
        return equity;
    }

    private double evaluateBasedOnOutlineStructure(BoardOutline outline) {
        double equity = 0;

        for (int x=1; x<=boardWidth; x++) {
            boolean hasAreaWallsSameHeight = false;
            boolean isAreaWallsSameHeightNotInitialized = true;
            int areaHeight = 0;
            int previousAreaWidth = 0;

            int rightWallY = outline.get(x);
            int startY = (x == boardWidth) ? outline.minY : outline.get(x);

            // Calculate the size of the closed area in the outline (areaWidth * areaHeight).
            for (int y=startY; y<=boardHeight; y++) {
                int areaWidth = 0;

                for (int areaX=x-1; areaX>=0; areaX--) {
                    if (outline.get(areaX) <= y) {
                        if (isAreaWallsSameHeightNotInitialized) {
                            hasAreaWallsSameHeight = outline.get(areaX) == rightWallY;
                            isAreaWallsSameHeightNotInitialized = false;
                        }
                        break;
                    }
                    areaWidth++;
                }
                if (areaWidth == 0 && previousAreaWidth == 0) {
                    break;
                } else {
                    if (areaWidth > 0 && (areaWidth == previousAreaWidth || previousAreaWidth == 0)) {
                        areaHeight++;
                    } else {
                        if (hasAreaWallsSameHeight) {
                            equity += areaWidthFactors[previousAreaWidth] * areaHeightEqFactors[areaHeight] * outlineHeightFactors[startY];
                        } else {
                            equity += areaWidthFactors[previousAreaWidth] * areaHeightFactors[areaHeight] * outlineHeightFactors[startY];
                        }
                        areaHeight = 1;
                        isAreaWallsSameHeightNotInitialized = true;
                    }
                    previousAreaWidth = areaWidth;
                }
            }
        }
        return equity;
    }
}
