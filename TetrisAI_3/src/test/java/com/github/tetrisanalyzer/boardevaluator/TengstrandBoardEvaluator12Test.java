package test.java.com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMoves;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import com.github.tetrisanalyzer.boardevaluator.*;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TengstrandBoardEvaluator12Test {

    @Test
    @Ignore
    public void evaluateVersionOneDotTwo() {
        Board board = Board.create(
                "|----------|",
                "|----------|",
                "|-x---xx---|",
                "|-xx-xx-x--|",
                "|xxxx-xxxx-|",
                "------------");

        BoardEvaluator evaluator = new TengstrandBoardEvaluator12(10, 5, new StandardGameSettings(board));
        AllValidPieceMoves allValidPieceMoves = new AllValidPieceMoves(board, new AtariGameSettings(board));

        assertEquals(47.328689, evaluator.evaluate(board, allValidPieceMoves), 0.0001);
    }
}
