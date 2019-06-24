package test.java.com.github.tetrisanalyzer.text;

import com.github.tetrisanalyzer.game.Duration;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import com.github.tetrisanalyzer.text.RaceInfo;
import org.junit.Test;

import java.util.List;

import static test.java.com.github.tetrisanalyzer.settings.RaceSettingsTest.RACE_SETTINGS;
import static test.java.com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static junit.framework.Assert.assertEquals;

public class RaceInfoTest {

    @Test
    public void parameterValuesAsString() {
        List<String> rows = parameters().rows().rows;

        String result = "";
        String separator = "";
        for (String string : rows) {
            result += separator + string;
            separator = "\n";
        }

        String expected =
                "parameter value:                                \n" +
                "--------------------  --------------  --------------\n" +
                "duration:              1d 3h 52m 10s   1d 3h 52m 10s\n" +
                "board:                       10 x 12         10 x 12\n" +
                "tetris rules id:               Atari        Standard\n" +
                "piece generator id:           Linear          Linear\n" +
                "board evaluator id:   Tengstrand 1.2  Tengstrand 1.2\n" +
                "                                                    \n" +
                "level, known pieces:           4 : 3           4 : 3\n" +
                "equity diff:                 0.00000         0.00000\n" +
                "games:                             2          19 488\n" +
                "rows:                          2 666           6 466\n" +
                "area (25.0%):             2.38095238      2.27272727\n" +
                "                                                    \n" +
                "rows/game:                   500 000           4 561\n" +
                "r/g delta:                                          \n" +
                "min rows:                          2               2\n" +
                "max rows:                     27 478          29 919\n" +
                "rows/s:                            0               0\n" +
                "pieces/s:                          0               0";

        assertEquals(expected, result);
    }

    private RaceInfo parameters() {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RACE_SETTINGS, systemSettings, false);

        race.games.get(0).gameState.games = 2;
        race.games.get(0).gameState.totalPieces = 4000000;
        race.games.get(0).gameState.totalRows = 1000000;
        race.games.get(0).duration = new Duration(1,2,3,4,5);

        return new RaceInfo(race);
    }
}
