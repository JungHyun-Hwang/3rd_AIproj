package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.BoardOutline;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.SettingsReader;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Joakim Tengstrand's Tetris AI, version 1.5 (an experiment, work in progress!)
 */
public class TengstrandBoardEvaluator15 implements BoardEvaluator {
    public int boardWidth;
    public int boardHeight;
    public double maxEquity;
    public double maxEquityFactor = 1.21;

    public double heightFactor0 = 7;
    public double heightFactor1 = 2.5;
    public double heightFactorDelta = 0.81;

    public double hollowFactor1 = 0.5;
    public double hollowFactorDelta = 0.85;
    public double hollowTopFactor1 = 0.6;
    public double hollowTopFactorDelta = 0.9;
    public double hollowUncoveredFactor1 = 0.5;
    public double hollowUncoveredFactorDelta = 0.95;

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

    public double[] heightFactors;
    public double[] hollowFactors;
    public double[] hollowTopFactors;
    public double[] hollowUncoveredFactors;
    public double[] areaWidthFactors;
    public double[] areaHeightFactors;
    public double[] areaHeightEqFactors;

    public TengstrandBoardEvaluator15(int boardWidth, int boardHeight, GameSettings rules) {
        init(boardWidth, boardHeight);
    }

    public TengstrandBoardEvaluator15(int boardWidth, int boardHeight, GameSettings rules, Map settings) {
        SettingsReader reader = new SettingsReader(settings, "board evaluators");

        id = reader.readString("id");
        description = reader.readString("description");
        author = reader.readString("author");
        url = reader.readString("url");
        clazz = reader.readString("class");

        maxEquityFactor = reader.readDouble("maxEquityFactor");
        heightFactor0 = reader.readDouble("heightFactor0");
        heightFactor1 = reader.readDouble("heightFactor1");
        heightFactorDelta = reader.readDouble("heightFactorDelta");
        hollowFactor1 = reader.readDouble("hollowFactor1");
        hollowFactorDelta = reader.readDouble("hollowFactorDelta");
        hollowUncoveredFactor1 = reader.readDouble("hollowUncoveredFactor1");
        hollowUncoveredFactorDelta = reader.readDouble("hollowUncoveredFactorDelta");
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

        heightFactors = new double[boardHeight + 1];
        hollowFactors = new double[boardWidth < 5 ? 5 : boardWidth];
        hollowTopFactors = new double[boardWidth < 5 ? 5 : boardWidth];
        hollowUncoveredFactors = new double[boardWidth < 5 ? 5 : boardWidth];
        areaWidthFactors = new double[boardWidth < 10 ? 10 : boardWidth];
        areaHeightFactors = new double[boardHeight < 6 ? 6 : boardHeight + 1];
        areaHeightEqFactors = new double[boardHeight < 6 ? 6 : boardHeight + 1];

        maxEquity = boardWidth * (boardHeight - 1) * maxEquityFactor;

        initHeightFactors();
        initHollowFactors();
        initAreaWidthFactors();
        initAreaHeightFactors();
        initAreaHeightEqFactors();
    }

    private void initHeightFactors() {
        heightFactors[0] = heightFactor0;
        heightFactors[1] = heightFactor1;
        double factor = heightFactor1;

        for (int i=2; i< heightFactors.length; i++) {
            factor *= heightFactorDelta;
            heightFactors[i] = factor;
        }
    }

    private void initHollowFactors() {
        initHollowFactors(hollowFactors, hollowFactor1, hollowFactorDelta);
        initHollowFactors(hollowTopFactors, hollowTopFactor1, hollowTopFactorDelta);
        initHollowFactors(hollowUncoveredFactors, hollowUncoveredFactor1, hollowUncoveredFactorDelta);
    }

    private void initHollowFactors(double[] factors, double startFactor, double delta) {
        factors[1] = startFactor;
        double factor = startFactor;

        for (int i=2; i<factors.length; i++) {
            factor *= delta;
            factors[i] = factor;
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
        parameters.put("heightFactor0", Double.toString(heightFactor0));
        parameters.put("heightFactor1", Double.toString(heightFactor1));
        parameters.put("heightFactorDelta", Double.toString(heightFactorDelta));

        for (int i=1; i<hollowFactors.length; i++) {
            parameters.put("hollowFactor" + (i), Double.toString(hollowFactors[i]));
        }
        parameters.put("hollowFactorDelta", Double.toString(hollowFactorDelta));

        for (int i=1; i<hollowTopFactors.length; i++) {
            parameters.put("hollowTopFactor" + (i), Double.toString(hollowTopFactors[i]));
        }
        parameters.put("hollowFactorDelta", Double.toString(hollowFactorDelta));

        for (int i=1; i<hollowUncoveredFactors.length; i++) {
            parameters.put("hollowUncoveredFactor" + (i), Double.toString(hollowFactors[i]));
        }
        parameters.put("hollowUncoveredFactorDelta", Double.toString(hollowUncoveredFactorDelta));

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
                evaluateBasedOnOutlineHeight(outline) +
                evaluateBasedOnOutlineStructure(outline);
    }

    private double evaluateBasedOnHollows(Board board, BoardOutline outline) {
        int minrowy = boardHeight;
        int[] squares = new int[boardHeight + 1];
        boolean[] coveredhole = new boolean[boardHeight];

        for (int x=0; x<boardWidth; x++) {
            int miny = outline.get(x);
            squares[miny]++;

            for (int y=miny + 1; y<boardHeight; y++) {
                if (board.isFree(x, y)) {
                    coveredhole[y] = true;
                    if (outline.get(x) < minrowy) {
                        minrowy = outline.get(x);
                    }
                } else {
                    squares[y]++;
                }
            }
        }
        int maxy;
        for (maxy=boardHeight-1; maxy>=0 && !coveredhole[maxy]; maxy--);

        double equity = 0;
        double factor = 1;
        int y;

        // Calculate the top rows
        for (y=minrowy; y<=maxy && !coveredhole[y]; y++) {
            factor *= hollowTopFactors[boardWidth-squares[y]];
            equity += (1 - factor) * boardWidth;
        }
        for (; y<=maxy; y++) {
            if (coveredhole[y]) {
                factor *= hollowFactors[boardWidth-squares[y]];
                equity += (1 - factor) * boardWidth;
            } else {
                equity += hollowUncoveredFactors[squares[y]] * boardWidth;
            }
        }
        for (; y<boardHeight; y++) {
            equity += hollowUncoveredFactors[squares[y]] * boardWidth;
        }

        return equity;
    }

    private double evaluateBasedOnOutlineHeight(BoardOutline outline) {
        double sum = 0;

        for (int x=0; x<boardWidth; x++) {
            sum += heightFactors[outline.get(x)];
        }
        return sum;
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
                            equity += areaWidthFactors[previousAreaWidth] * areaHeightEqFactors[areaHeight];
                        } else {
                            equity += areaWidthFactors[previousAreaWidth] * areaHeightFactors[areaHeight];
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
