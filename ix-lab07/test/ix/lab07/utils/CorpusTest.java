package ix.lab07.utils;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

public class CorpusTest {
    
    @Test
    public void testLoad() throws IOException {
        Corpus c = new Corpus("test/testCorpus.txt");
        
        assertEquals(4, c.size());
        assertEquals(3, c.getVocabularySize());
        
        assertEquals(4, c.getDocumentLength(0));
        assertEquals(2, c.getDocumentLength(1));
        assertEquals(2, c.getDocumentLength(2));
        
        assertEquals(3, c.getNbWordsInDocument(0));
        assertEquals(2, c.getNbWordsInDocument(1));
        assertEquals(2, c.getNbWordsInDocument(2));
        
        Set<Integer> set1 = new TreeSet<Integer>();
        set1.add(0);
        set1.add(2);
        set1.add(3);
        assertEquals(set1, c.getDocumentsContaining(0));
        Set<Integer> set2 = new TreeSet<Integer>();
        set2.add(0);
        set2.add(1);
        set2.add(3);
        assertEquals(set2, c.getDocumentsContaining(1));
        Set<Integer> set3 = new TreeSet<Integer>();
        set3.add(0);
        set3.add(1);
        set3.add(2);
        assertEquals(set3, c.getDocumentsContaining(2));
        
        assertEquals(1, c.getDocument(0).get(0).intValue());
        assertEquals(2, c.getDocument(0).get(1).intValue());
        assertEquals(1, c.getDocument(0).get(2).intValue());
    }

}
