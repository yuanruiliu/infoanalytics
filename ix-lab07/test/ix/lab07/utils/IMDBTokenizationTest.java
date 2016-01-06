package ix.lab07.utils;

import static org.junit.Assert.assertEquals;
import ix.lab07.utils.DocumentTokenization;

import org.junit.Test;

import com.google.common.collect.Iterables;

public class IMDBTokenizationTest {

    private static final String LINE = "all, out  MYSELF Wall, Jerico, : < > @ FEELING, "
            + "feelings, going, seems, abstract dear 234 X45 destroy cutting-edge tech don't";
    private static final String[] EXPECTED_TOKENS = new String[] {
        "myself", "wall", "jerico", "feel", "feel", "go", "seem", "abstract",
        "dear", "234", "x45", "destroi", "cut", "edg", "tech", "don", "t"
    };


    @Test
    public void testTokenization() {
        String[] tokens = Iterables.toArray(DocumentTokenization.stream(LINE), String.class);
        assertEquals(EXPECTED_TOKENS.length, tokens.length);
        for (int i = 0; i < tokens.length; ++i) {
            assertEquals(EXPECTED_TOKENS[i], tokens[i]);
        }
    }

}
