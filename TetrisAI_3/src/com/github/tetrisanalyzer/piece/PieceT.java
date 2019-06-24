package com.github.tetrisanalyzer.piece;

import com.github.tetrisanalyzer.settings.PieceSettings;

public class PieceT extends Piece {

    PieceT(PieceSettings settings) {
        super(settings);
    }

    @Override public byte number() { return 7; }
    @Override public char character() { return 'T'; }
    @Override protected int[] widths() { return new int[] { 3, 2, 3, 2 }; }
    @Override protected int[] heights() { return new int[] { 2, 3, 2, 3 }; }
    @Override protected PieceShape[] shapes() {
        return new PieceShape[] {
            new PieceShape(new Point(0,0), new Point(1,0), new Point(2,0), new Point(1,1)),
            new PieceShape(new Point(0,0), new Point(0,1), new Point(1,1), new Point(0,2)),
            new PieceShape(new Point(1,0), new Point(0,1), new Point(1,1), new Point(2,1)),
            new PieceShape(new Point(1,0), new Point(0,1), new Point(1,1), new Point(1,2))
        };
    }
}
