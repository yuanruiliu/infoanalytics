package ix.lab06.community;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import ix.lab06.utils.WeightedGraph;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

public class AssignCommunitiesTest {

    @Test
    public void testAssignCommunities() throws IOException {
        WeightedGraph gwl = WeightedGraph.parse("test/test_03.txt");
        Status s = new Status(gwl);
        s.assignCommunities();
        Map<String, Integer> nodeCommunities = s.getNodesCommunity();

        int comm1 = nodeCommunities.get("0");
        int comm2 = nodeCommunities.get("3");
        int comm3 = nodeCommunities.get("8");
        int comm4 = nodeCommunities.get("11");
        
        assertNotSame(comm1, comm2);
        assertNotSame(comm1, comm3);
        assertNotSame(comm1, comm4);
        assertNotSame(comm2, comm3);
        assertNotSame(comm2, comm4);
        assertNotSame(comm3, comm4);

        assertEquals(nodeCommunities.get("1").intValue(), comm1);
        assertEquals(nodeCommunities.get("2").intValue(), comm1);
        assertEquals(nodeCommunities.get("4").intValue(), comm1);
        assertEquals(nodeCommunities.get("5").intValue(), comm1);
        assertEquals(nodeCommunities.get("6").intValue(), comm2);
        assertEquals(nodeCommunities.get("7").intValue(), comm2);
        assertEquals(nodeCommunities.get("9").intValue(), comm3);
        assertEquals(nodeCommunities.get("10").intValue(), comm3);
        assertEquals(nodeCommunities.get("12").intValue(), comm3);
        assertEquals(nodeCommunities.get("14").intValue(), comm3);
        assertEquals(nodeCommunities.get("15").intValue(), comm3);
        assertEquals(nodeCommunities.get("13").intValue(), comm4);
    }
}
