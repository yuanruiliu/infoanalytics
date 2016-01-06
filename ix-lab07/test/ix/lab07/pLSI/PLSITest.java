package ix.lab07.pLSI;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;

import org.junit.Test;

public class PLSITest extends PLSI {
    
    public final static double EPSILON = 1e-7;

    public PLSITest() throws IOException {
        super("test/testCorpus.txt", null, 2, 0);
    }
    
    @Test
    public void testInit() {
        checkNormalization();
    }
    
    @Test
    public void testEstep() {
        int vocabularySize = corpus.getVocabularySize();
        int nbDocuments = corpus.size();

        this.eStep();
        
        for (int d = 0; d < nbDocuments; ++d) {
            Set<Integer> documentWords = corpus.getDocument(d).keySet();
            for (int w = 0; w < vocabularySize; ++w) {
                // skip words that are not in the document
                if (!documentWords.contains(w)) {
                    continue;
                }
                int w_doc = corpus.getWordPositionInDocument(w, d);
                
                double sum = 0;
                for (int z = 0; z < nbTopics; ++z) {
                    sum += Pz_dw[z][d][w_doc];
                }
                assertEquals(1, sum, EPSILON);
            }
        }
    }
    
    @Test
    public void testMStep() {
        this.eStep();
        this.mStep();
        this.checkNormalization();
    }
    
    @Test
    public void testIter() {
        this.eStep();
        this.mStep();
        this.eStep();
        this.mStep();
        this.checkNormalization();
    }
    
    public void checkNormalization() {
        int vocabularySize = corpus.getVocabularySize();
        int nbDocuments = corpus.size();
        double sum;
        
        // check that probabilities sum to 0
        for (int z = 0; z < nbTopics; ++z) {
            sum = 0;
            for (int w = 0; w < vocabularySize; ++w) {
                sum += Math.abs(Pw_z[w][z]);
            }
            assertEquals(1, sum, EPSILON);
        }
        
        for (int d = 0; d < nbDocuments; ++d) {
            sum = 0;
            for (int z = 0; z < nbTopics; ++z) {
                sum += Math.abs(Pz_d[z][d]);
            }
            assertEquals(1, sum, EPSILON);
        }
        
        sum = 0;
        for (int d = 0; d < nbDocuments; ++d) {
            sum += Math.abs(Pd[d]);
        }
        assertEquals(1, sum, EPSILON);
    }

}
