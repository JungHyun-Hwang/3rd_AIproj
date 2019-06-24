package test.java.com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.game.StringUtils;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StringUtilsTest {

    @Test
    public void test() {
        assertEquals("12 345", StringUtils.format(12345.0));
    }
}
