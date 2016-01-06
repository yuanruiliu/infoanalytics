package ix.lab06.utils;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class WeightedGraphTest {

    @Test
    public void testTotalWeight() throws IOException {
        WeightedGraph graph = WeightedGraph.parse("test/test_weightedGraph.txt");
        
        assertEquals(21, graph.getTotalWeight());
    }

}
