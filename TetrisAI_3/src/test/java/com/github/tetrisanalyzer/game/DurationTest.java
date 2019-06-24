package test.java.com.github.tetrisanalyzer.game;

import org.junit.Test;
import com.github.tetrisanalyzer.game.*;
import static junit.framework.Assert.assertEquals;

public class DurationTest {

    @Test
    public void two_point_five_sec() {
        Duration duration = Duration.create(10000, 12500);
        assertEquals("0d 0h 0m 2.500s", duration.toString());
    }

    @Test
    public void days_hours_minutes_seconds() {
        Duration duration = Duration.create(1234, time(3, 5, 18, 39) + 1234);
        assertEquals("3d 5h 18m 39.000s", duration.asString());
    }

    private long time(int d, int h, int m, int s) {
        return d*24*60*60*1000 + h*60*60*1000 + m*60*1000 + s*1000;
    }
}
