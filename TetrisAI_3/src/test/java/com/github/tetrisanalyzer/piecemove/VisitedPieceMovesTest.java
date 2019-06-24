package test.java.com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecemove.*;
import com.github.tetrisanalyzer.piecemove.VisitedPieceMoves;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import org.junit.Before;
import org.junit.Test;

import static com.github.tetrisanalyzer.piece.Piece.createPieceS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VisitedPieceMovesTest {
    private Board board;
    private Piece piece;

    @Before
    public void setUp() {
        board = Board.create(6, 6);
        piece = createPieceS(new AtariGameSettings());
    }

    @Test
    public void visitLeft() {
        Move move = new Move(0,0,0);
        VisitedPieceMoves visitedMoves = new VisitedPieceMoves(board.width, board.height, piece);
        visitedMoves.visit(getMovement(move, Direction.LEFT));

        assertFalse(visitedMoves.isUnvisited(getMovement(move, Direction.LEFT)));
        assertTrue(visitedMoves.isUnvisited(getMovement(move, Direction.RIGHT)));
        assertTrue(visitedMoves.isUnvisited(getMovement(move, Direction.DOWN)));
        assertFalse(visitedMoves.isUnvisited(getMovement(move, Direction.ROTATE)));
    }

    private Movement getMovement(Move move, Direction direction) {
        PieceMove pieceMove = new PieceMove(piece, move);
        return new Movement(pieceMove, direction);
    }
}
