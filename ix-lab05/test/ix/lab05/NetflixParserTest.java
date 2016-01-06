package ix.lab05;

import static org.junit.Assert.assertTrue;
import ix.lab05.processing.NetflixParser;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class NetflixParserTest {

    private static final String[] LINES = {
        "1234:",
        "2084443,5,2002-10-04",
        "2543544,2,2003-04-22",
        "1744889,4,2003-08-21",
        "1242432,2,2005-08-25",
        "25382,3,2004-08-24",
        "39956,4,2003-11-22"
    };

    private static final String PARSED =
            "2084443\t1234\t5\n"
            + "2543544\t1234\t2\n"
            + "1744889\t1234\t4\n"
            + "1242432\t1234\t2\n"
            + "25382\t1234\t3\n"
            + "39956\t1234\t4\n";

    @Test
    public void testParser() throws IOException {
        List<String> lines = Arrays.asList(LINES);
        StringWriter sw = new StringWriter();
        NetflixParser.processFile(lines, sw);
        assertTrue(PARSED.equals(sw.toString()));
    }

}
