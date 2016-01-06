package ix.lab07.vsm;

import static ix.lab07.vsm.TfIdf.inverseDocFrequency;
import static ix.lab07.vsm.TfIdf.termFrequency;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TfIdfTest {

    private static final double EPSILON = 0.0001;

    @Test
    public void testTermFrequency() {
        assertEquals(1, termFrequency(1,1), EPSILON);
        assertEquals(0.5, termFrequency(1,2), EPSILON);
        assertEquals(0.01124138764, termFrequency(93, 8273), EPSILON);
    }

    @Test
    public void testInverseDocumentFrequency() {
        // This is a bit tricky as the IDF is defined up to constant scaling  (as
        // the logarithm base does not matter).
        // Therefore we just check that the ratio between IDF weights is correct.
        double idf1 = inverseDocFrequency(1);
        double idf2 = inverseDocFrequency(1234);
        assertEquals(2.403347, idf1 / idf2, EPSILON);
    }
}
