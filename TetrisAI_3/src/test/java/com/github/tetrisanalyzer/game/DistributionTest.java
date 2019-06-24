package test.java.com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.gui.Vertex;
import com.github.tetrisanalyzer.gui.Vertices;
import com.github.tetrisanalyzer.game.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class DistributionTest {

    @Test
    public void toVertices() {
        List<Long> cells = Arrays.asList(500L, 1000L, 1200L, 700L);
        Distribution distribution = new Distribution(10, 20, cells);
        Vertices vertices = distribution.toVertices();

        Vertices expected = new Vertices(
                new Vertex(0.0, 500.0),
                new Vertex(0.3333333333333333, 1000.0),
                new Vertex(0.6666666666666666, 1200.0),
                new Vertex(1.0, 700.0));

        assertEquals(expected, vertices);
    }

    @Test
    public void setAreaPercentage() {
        List<Long> cells = Arrays.asList(1L, 2L, 4L, 2L, 1L);
        Distribution distribution = new Distribution(10, 20, cells);

        distribution.setAreaPercentage(25);
        assertEquals(distribution.area(), 10.0);
    }
}
