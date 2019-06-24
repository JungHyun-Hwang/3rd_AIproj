package test.java.com.github.tetrisanalyzer.settings;

import com.github.tetrisanalyzer.settings.RaceGamesSettings;
import com.github.tetrisanalyzer.settings.RaceSettings;
import com.github.tetrisanalyzer.settings.SystemSettings;
import org.junit.Test;

import static test.java.com.github.tetrisanalyzer.settings.SystemSettingsTest.SYSTEM_SETTINGS;
import static org.junit.Assert.*;

public class RaceGamesSettingsTest {

    @Test
    public void size() throws Exception {
        SystemSettings systemSettings = SystemSettings.fromString(SYSTEM_SETTINGS);
        RaceSettings race = RaceSettings.fromString(RaceSettingsTest.RACE_SETTINGS, systemSettings, false);

        RaceGamesSettings games = race.games;

        assertEquals(2, games.size());
    }
}