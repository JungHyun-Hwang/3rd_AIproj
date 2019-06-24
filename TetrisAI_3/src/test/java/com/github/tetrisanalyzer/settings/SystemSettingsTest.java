package test.java.com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.settings.SystemSettings;
import org.junit.Test;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class SystemSettingsTest {

    public static String SYSTEM_SETTINGS = "\n" +
            "tetris rules:\n" +
            " - id: Standard\n" +
            "   description: Standard Tetris 2007 June 4 (Colin Fahey)\n" +
            "   url: http://colinfahey.com/tetris/tetris.html\n" +
            "   class: com.github.tetrisanalyzer.settings.StandardGameSettings\n" +
            "   sliding: off\n" +
            "   rotation: anticlockwise\n" +
            "   piece start position on standard board: [3,0]\n" +
            "   O: [[1,1]]\n" +
            "   I: [[0,1] [2,0]]\n" +
            "   S: [[1,1] [2,0]]\n" +
            "   Z: [[1,1] [2,0]]\n" +
            "   L: [[1,1] [2,0] [1,0] [1,0]]\n" +
            "   J: [[1,1] [2,0] [1,0] [1,0]]\n" +
            "   T: [[1,1] [2,0] [1,0] [1,0]]\n" +
            "\n" +
            " - id: Atari\n" +
            "   description: Atari arcade game\n" +
            "   url: http://www.arcade-museum.com/game_detail.php?game_id=10081\n" +
            "   class: com.github.tetrisanalyzer.settings.AtariGameSettings\n" +
            "   sliding: off\n" +
            "   rotation: anticlockwise\n" +
            "   piece start position on standard board: [3,0]\n" +
            "   O: [[0,0]]\n" +
            "   I: [[0,1] [1,0]]\n" +
            "   S: [[0,0] [0,0]]\n" +
            "   Z: [[0,0] [0,0]]\n" +
            "   L: [[0,0] [0,0] [0,0] [0,0]]\n" +
            "   J: [[0,0] [0,0] [0,0] [0,0]]\n" +
            "   T: [[0,0] [0,0] [0,0] [0,0]]\n" +
            "   \n" +
            "piece generators:\n" +
            "  - id: Linear\n" +
            "    description: Standard Linear congruential piece generator\n" +
            "    class: com.github.tetrisanalyzer.piecegenerator.LinearCongrentialPieceGenerator\n" +
            "    constant1: 1664525\n" +
            "    constant2: 555555555\n" +
            "    seed: 1" +
            "\n" +
            "board evaluators:\n" +
            "  - id: Tengstrand 1.2\n" +
            "    description: Tengstrand 1.2\n" +
            "    author: Joakim Tengstrand\n" +
            "    url: http://hem.bredband.net/joakimtengstrand\n" +
            "    class: com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator12\n" +
            "    maxEquityFactor: 1.21\n" +
            "    heightFactor0: 7\n" +
            "    heightFactor1: 2.5\n" +
            "    heightFactorDelta: 0.86\n" +
            "    hollowFactor1: 0.533\n" +
            "    hollowFactor2: 0.6\n" +
            "    hollowFactor3: 0.5\n" +
            "    hollowFactor4: 0.4\n" +
            "    hollowFactorDelta: 0.85\n" +
            "    hollowFactorDeltaDelta: 0.95\n" +
            "    areaWidthFactor1: 4.95\n" +
            "    areaWidthFactor2: 2.39\n" +
            "    areaWidthFactor3: 3.1\n" +
            "    areaWidthFactor4: 2.21\n" +
            "    areaWidthFactor5: 2.05\n" +
            "    areaWidthFactor6: 1.87\n" +
            "    areaWidthFactor7: 1.52\n" +
            "    areaWidthFactor8: 1.34\n" +
            "    areaWidthFactor9: 1.18\n" +
            "    areaHeightFactor1: 0.5\n" +
            "    areaHeightEqFactor1: 0.42\n" +
            "    areaHeightFactor2: 1.19\n" +
            "    areaHeightEqFactor2: 1.05\n" +
            "    areaHeightFactor3: 2.3\n" +
            "    areaHeightEqFactor3: 2.2\n" +
            "    areaHeightEqFactor4: 3.06\n" +
            "    areaHeightFactor4: 3.1\n" +
            "    areaHeightFactor5: 4.6\n" +
            "    areaHeightFactorDelta: 1\n" +
            "  - id: Tengstrand 1.3\n" +
            "    description: Tengstrand 1.3\n" +
            "    author: Joakim Tengstrand\n" +
            "    url: http://hem.bredband.net/joakimtengstrand\n" +
            "    class: com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator13\n" +
            "    maxEquityFactor: 1.21\n" +
            "    piecePenalty: 6\n" +
            "    heightFactor0: 7\n" +
            "    heightFactor1: 2.5\n" +
            "    heightFactorDelta: 0.86\n" +
            "    hollowFactor1: 0.533\n" +
            "    hollowFactor2: 0.6\n" +
            "    hollowFactor3: 0.5\n" +
            "    hollowFactor4: 0.4\n" +
            "    hollowFactorDelta: 0.85\n" +
            "    hollowFactorDeltaDelta: 0.95\n" +
            "    areaWidthFactor1: 4.95\n" +
            "    areaWidthFactor2: 2.39\n" +
            "    areaWidthFactor3: 3.1\n" +
            "    areaWidthFactor4: 2.21\n" +
            "    areaWidthFactor5: 2.05\n" +
            "    areaWidthFactor6: 1.87\n" +
            "    areaWidthFactor7: 1.52\n" +
            "    areaWidthFactor8: 1.34\n" +
            "    areaWidthFactor9: 1.18\n" +
            "    areaHeightFactor1: 0.5\n" +
            "    areaHeightEqFactor1: 0.42\n" +
            "    areaHeightFactor2: 1.19\n" +
            "    areaHeightEqFactor2: 1.05\n" +
            "    areaHeightFactor3: 2.3\n" +
            "    areaHeightEqFactor3: 2.2\n" +
            "    areaHeightEqFactor4: 3.06\n" +
            "    areaHeightFactor4: 3.1\n" +
            "    areaHeightFactor5: 4.6\n" +
            "    areaHeightFactorDelta: 1";

    @Test
    public void readSystemSettings() {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);

        assertEquals(2, systemSettings.gameSettings.size());
        assertEquals(1, systemSettings.pieceGeneratorSettings.size());
        assertEquals(2, systemSettings.boardEvaluatorSettings.size());

        assertEquals(3, systemSettings.gameSettings.get("Standard").pieceStartX(10));
        assertEquals(0, systemSettings.gameSettings.get("Standard").pieceStartY);

        Map pieceGeneratorSettings = systemSettings.pieceGeneratorSettings.get("Linear");
        assertEquals("1664525", pieceGeneratorSettings.get("constant1"));

        assertEquals("0.86", systemSettings.boardEvaluatorSettings.get("Tengstrand 1.2").get("heightFactorDelta"));
    }
}
