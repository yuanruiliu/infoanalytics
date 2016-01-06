package ix.lab06.community;

import static org.junit.Assert.assertEquals;
import ix.lab06.utils.WeightedGraph;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

public class ModularityTest {

    private static final double DELTA = 0.001;

    @Test
    public void testModularity1() throws IOException {
        HashMap<String, Integer> nodeCommunities = new HashMap<String, Integer>();
        nodeCommunities.put("1", 0);
        nodeCommunities.put("2", 0);
        nodeCommunities.put("3", 0);
        nodeCommunities.put("10", 0);
        nodeCommunities.put("4", 1);
        nodeCommunities.put("5", 1);
        nodeCommunities.put("6", 1);
        nodeCommunities.put("7", 2);
        nodeCommunities.put("8", 2);
        nodeCommunities.put("9", 2);
        this.testModularity("test/test_01.txt", nodeCommunities, 0.4895833);
    }

    @Test
    public void testModularity2() throws IOException {
        HashMap<String, Integer> nodeCommunities = new HashMap<String, Integer>();
        nodeCommunities.put("1", 0);
        nodeCommunities.put("2", 0);
        nodeCommunities.put("3", 0);
        nodeCommunities.put("10", 0);
        nodeCommunities.put("4", 1);
        nodeCommunities.put("5", 1);
        nodeCommunities.put("6", 1);
        nodeCommunities.put("7", 2);
        nodeCommunities.put("8", 2);
        nodeCommunities.put("9", 2);
        this.testModularity("test/test_02.txt", nodeCommunities, 0.48214285);
    }

    private void testModularity(String fileName,
            HashMap<String, Integer> nodeCommunities, double truth)
            throws IOException {
        WeightedGraph graph = WeightedGraph.parse(fileName);
        Status s = new Status(graph, nodeCommunities);
        assertEquals(truth, s.modularity(), DELTA);
    }

}
