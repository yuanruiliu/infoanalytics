package ix.lab07.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;

public class Corpus {
    /**
     * Documents of the corpus.
     * 
     * Each document is represented as a set of words, and the number of
     * occurrences of each word. Thanks to the bag-of-word assumption made by
     * pLSI, we do not need to keep the ordering of the words, just their
     * counts.
     */
    private List<Map<Integer, Integer>> documents = new ArrayList<Map<Integer, Integer>>();

    /**
     * Total length (with repetitions) of each document.
     */
    private List<Integer> documentLenghts = new ArrayList<Integer>();

    /**
     * Vocabulary of the corpus, that maps each term to an id.
     */
    private Map<String, Integer> vocabulary = new TreeMap<String, Integer>();

    /**
     * Reverse vocabulary of the corpus, that maps each id to a term.
     */
    private Map<Integer, String> reverseVocabulary = new TreeMap<Integer, String>();

    /**
     * For each word, set of the documents that contain this word.
     */
    private Map<Integer, Set<Integer>> documentsContainingTerm = new TreeMap<Integer, Set<Integer>>();

    /**
     * Stores the position of each word in the set of words of the document
     * (useful to get the index of a word in the sparse array of a document).
     */
    private Map<Integer, Map<Integer, Integer>> documentWordPosition = new TreeMap<Integer, Map<Integer, Integer>>();

    /**
     * Creates a corpus from the content of the given file.
     * 
     * Each line of the file contains a document id, followed by its content.
     * The content is tokenized, and the words are mapped to an ID in the corpus
     * dictionary.
     * 
     * @param fileName
     * @throws IOException
     */
    public Corpus(String fileName) throws IOException {
        File dataFile = new File(fileName);
        int documentId = 0; // id of the document in the corpus

        // read data file line by line
        for (String line : FileUtils.readLines(dataFile)) {
            Map<Integer, Integer> document = new TreeMap<Integer, Integer>();
            int documentLength = 0;
            boolean isFirst = true;

            for (String term : DocumentTokenization.stream(line)) {
                // the first term of the line contains the document id
                if (isFirst) {
                    isFirst = false;
                } else {
                    int termId = getTermId(term);

                    // increment the number of occurrences of this term
                    int termCount = 1;
                    if (document.containsKey(termId)) {
                        termCount = document.get(termId) + 1;
                    }

                    document.put(termId, termCount);
                    documentsContainingTerm.get(termId).add(documentId);

                    ++documentLength;
                }
            }

            // now, save the position of each word in the document
            Map<Integer, Integer> wordPos = new TreeMap<Integer, Integer>();
            int w_doc = 0;
            for (int w_corpus : document.keySet()) {
                wordPos.put(w_corpus, w_doc);
                ++w_doc;
            }

            documents.add(document);
            documentLenghts.add(documentLength);
            documentWordPosition.put(documentId, wordPos);

            ++documentId;
        }
    }

    /**
     * Gets the id of the given term in the vocabulary. If the term does not
     * exist yet, it is added.
     * 
     * @param term
     * @return
     */
    private int getTermId(String term) {
        int termId = 0;
        if (!vocabulary.containsKey(term)) {
            termId = vocabulary.size();
            vocabulary.put(term, termId);
            reverseVocabulary.put(termId, term);
            documentsContainingTerm.put(termId, new TreeSet<Integer>());
        } else {
            termId = vocabulary.get(term);
        }
        return termId;
    }

    /**
     * Gets the set of document ids that contain the given term.
     * 
     * @param termdId
     * @return
     */
    public Set<Integer> getDocumentsContaining(int termdId) {
        return documentsContainingTerm.get(termdId);
    }

    /**
     * Gets the position of the given term in the list of words of the given
     * document. This is useful when mapping from the term id (global to the
     * corpus) to its id in the document (local).
     * 
     * @param termId
     * @param documentId
     * @return
     */
    public int getWordPositionInDocument(int termId, int documentId) {
        return documentWordPosition.get(documentId).get(termId);
    }

    /**
     * Gets the word and counts of the given document.
     * 
     * @param documentId
     * @return
     */
    public Map<Integer, Integer> getDocument(int documentId) {
        return documents.get(documentId);
    }

    /**
     * Gets the total number of words (with repetition) in the given document.
     * 
     * @param documentId
     * @return
     */
    public int getDocumentLength(int documentId) {
        return this.documentLenghts.get(documentId);
    }

    /**
     * Gets the number of distinct words in the given document.
     * 
     * @param documentId
     * @return
     */
    public int getNbWordsInDocument(int documentId) {
        return this.documents.get(documentId).keySet().size();
    }

    /**
     * Gets the number of words in the vocabulary.
     * 
     * @return
     */
    public int getVocabularySize() {
        return this.vocabulary.size();
    }

    /**
     * Gets the number of documents in the corpus.
     * 
     * @return
     */
    public int size() {
        return this.documents.size();
    }
    
    public void saveVocabulary(String fileName) throws IOException {
        File file = new File(fileName);

        // if file doesn't exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        for (String term: reverseVocabulary.values()) {
            bw.write(term + "\n");
        }

        bw.close();
        fw.close();
    }
}