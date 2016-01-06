package ix.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test cases for the Edge class
 */
public class EdgeTest {

    /**
     * Test method for {@link ix.utils.Edge#parse(java.lang.String)}.
     */
    @Test
    public void testParse() {
        String eTest = "art1|art2	3";

        Edge edge1 = new Edge();
        edge1.parse(eTest);

        assertEquals(new Long(3), edge1.weight);
        assertEquals("art2", edge1.destinationArticleName);
        assertEquals("art1", edge1.sourceArticleName);
    }

    @Test
    public void parseUtf8Test() {
        String line = "Extra-judicial_killings|Nguyá»n_VÄn_LÃ©m	1";

        Edge e = new Edge();
        e.parse(line);

        assertEquals("Extra-judicial_killings", e.sourceArticleName);
        assertEquals("Nguyá»n_VÄn_LÃ©m", e.destinationArticleName);
        assertEquals(new Long(1), e.weight);
    }

}
