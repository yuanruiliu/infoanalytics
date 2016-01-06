package ix.lab06.community;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import ix.lab06.utils.WeightedGraph;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class LouvainTest {

    @Test
    public void testCommunityDetection1() throws IOException {
        WeightedGraph g = WeightedGraph.parse("test/test_02.txt");
        Louvain louvain = new Louvain(g);
        louvain.communityDetection();

        Map<String, Integer> bestcomm = louvain.bestCommunity();
        
        int comm1 = bestcomm.get("1");
        int comm2 = bestcomm.get("4");
        int comm3 = bestcomm.get("7");
        
        assertNotSame(comm1, comm2);
        assertNotSame(comm1, comm3);
        assertNotSame(comm2, comm3);
        
        assertEquals(bestcomm.get("10").intValue(), comm1);
        assertEquals(bestcomm.get("2").intValue(), comm1);
        assertEquals(bestcomm.get("3").intValue(), comm1);
        assertEquals(bestcomm.get("5").intValue(), comm2);
        assertEquals(bestcomm.get("6").intValue(), comm2);
        assertEquals(bestcomm.get("8").intValue(), comm3);
        assertEquals(bestcomm.get("9").intValue(), comm3);
    }

    @Test
    public void testCommunityDetection2() throws IOException {
        WeightedGraph g = WeightedGraph.parse("test/test_03.txt");
        Louvain louvain = new Louvain(g);
        louvain.communityDetection();

        Map<String, Integer> bestcomm = louvain.bestCommunity();
        
        int comm1 = bestcomm.get("0");
        int comm2 = bestcomm.get("8");
        
        assertNotSame(comm1, comm2);
        
        assertEquals(bestcomm.get("1").intValue(), comm1);
        assertEquals(bestcomm.get("2").intValue(), comm1);
        assertEquals(bestcomm.get("3").intValue(), comm1);
        assertEquals(bestcomm.get("4").intValue(), comm1);
        assertEquals(bestcomm.get("5").intValue(), comm1);
        assertEquals(bestcomm.get("6").intValue(), comm1);
        assertEquals(bestcomm.get("7").intValue(), comm1);
        assertEquals(bestcomm.get("9").intValue(), comm2);
        assertEquals(bestcomm.get("10").intValue(), comm2);
        assertEquals(bestcomm.get("11").intValue(), comm2);
        assertEquals(bestcomm.get("12").intValue(), comm2);
        assertEquals(bestcomm.get("13").intValue(), comm2);
        assertEquals(bestcomm.get("14").intValue(), comm2);
        assertEquals(bestcomm.get("15").intValue(), comm2);
    }
}
