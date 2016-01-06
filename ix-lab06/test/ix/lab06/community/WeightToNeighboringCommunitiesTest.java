package ix.lab06.community;

import static org.junit.Assert.assertEquals;
import ix.lab06.utils.WeightedGraph;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class WeightToNeighboringCommunitiesTest {
    @Test
    public void testWeightToNeighboringCommunities() throws IOException {
        HashMap<String, Integer> nodeCommunities = new HashMap<String, Integer>();
        nodeCommunities.put("0", 0);
        nodeCommunities.put("1", 0);
        nodeCommunities.put("2", 0);
        nodeCommunities.put("4", 0);
        nodeCommunities.put("5", 0);
        nodeCommunities.put("3", 1);
        nodeCommunities.put("7", 1);
        nodeCommunities.put("6", 1);
        nodeCommunities.put("8", 2);
        nodeCommunities.put("9", 2);
        nodeCommunities.put("10", 2);
        nodeCommunities.put("12", 2);
        nodeCommunities.put("14", 2);
        nodeCommunities.put("15", 2);
        nodeCommunities.put("11", 3);
        nodeCommunities.put("13", 3);

        WeightedGraph graph = WeightedGraph.parse("test/test_04.txt");
        Status s = new Status(graph, nodeCommunities);

        Map<Integer, Long> w1 = s.weightToNeighboringCommunities("0");
        assertEquals(w1.get(0).longValue(), 4);
        assertEquals(w1.get(1).longValue(), 3);

        Map<Integer, Long> w2 = s.weightToNeighboringCommunities("4");
        assertEquals(w2.get(0).longValue(), 10);
        assertEquals(w2.get(2).longValue(), 1);

        Map<Integer, Long> w3 = s.weightToNeighboringCommunities("6");
        assertEquals(w3.get(0).longValue(), 1);
        assertEquals(w3.get(1).longValue(), 3);
        assertEquals(w3.get(3).longValue(), 1);

        Map<Integer, Long> w4 = s.weightToNeighboringCommunities("10");
        assertEquals(w4.get(0).longValue(), 1);
        assertEquals(w4.get(2).longValue(), 3);
        assertEquals(w4.get(3).longValue(), 6);
    }

}
