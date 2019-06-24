package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;

public class PieceO extends Piece {

    PieceO(PieceSettings settings) {
        super(settings);
    }

    @Override public byte number() { return 1; }
    @Override public char character() { return 'O'; }
    @Override protected int[] widths() { return new int[] { 2 }; }
    @Override protected int[] heights() { return new int[] { 2 }; }

    @Override protected PieceShape[] shapes() {
        return new PieceShape[] {
            new PieceShape(new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1))
        };
    }
}
