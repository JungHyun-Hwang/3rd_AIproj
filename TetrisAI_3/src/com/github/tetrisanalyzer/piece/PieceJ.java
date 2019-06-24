package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;

public class PieceJ extends Piece {

    PieceJ(PieceSettings settings) {
        super(settings);
    }

    @Override public byte number() { return 6; }
    @Override public char character() { return 'J'; }
    @Override protected int[] widths() { return new int[] { 3, 2, 3, 2 }; }
    @Override protected int[] heights() { return new int[] { 2, 3, 2, 3 }; }
    @Override protected PieceShape[] shapes() {
        return new PieceShape[] {
            new PieceShape(new Point(0,0), new Point(1,0), new Point(2,0), new Point(2,1)),
            new PieceShape(new Point(0,0), new Point(1,0), new Point(0,1), new Point(0,2)),
            new PieceShape(new Point(0,0), new Point(0,1), new Point(1,1), new Point(2,1)),
            new PieceShape(new Point(1,0), new Point(1,1), new Point(0,2), new Point(1,2))
        };
    }
}
