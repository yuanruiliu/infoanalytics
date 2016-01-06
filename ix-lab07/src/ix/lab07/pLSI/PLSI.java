package ix.lab07.pLSI;

import ix.lab07.utils.Corpus;
import ix.lab07.utils.IOUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class PLSI {

    /**
     * Corpus of document on which we run pLSI
     */
    Corpus corpus;

    /**
     * Number of topics
     */
    int nbTopics;

    /**
     * Probability of each document. This probability is fixed for a corpus and
     * does not change across EM iterations.
     */
    double[] Pd;

    /**
     * Probability of a word given a topic. The first dimension represents the
     * words, the second the topics.
     * 
     * Dimensions: nbWordsInDictionary x nbTopics
     */
    double[][] Pw_z;

    /**
     * Probability of a topic given a document. The first dimension represents
     * the topics, the second the documents.
     * 
     * Dimensions: nbTopics x nbDocuments
     */
    double[][] Pz_d;

    /**
     * Probability of a topic, given the document and a word. The first
     * dimension represents the topics, the second the documents, and the third
     * the words (this way, we save space by only considering the words that
     * appear in each document d).
     * 
     * Dimensions: nbTopics x nbDocuments x nbWordsInDocument
     */
    double[][][] Pz_dw;

    public PLSI(String corpusFileName, String outputFolder, int nbTopics,
            int nbIterations) throws IOException {
        this.nbTopics = nbTopics;
        this.corpus = new Corpus(corpusFileName);

        List<Double> logLikelihoods = new ArrayList<Double>();

        this.initialize();
        for (int i = 0; i < nbIterations; ++i) {
            this.eStep();
            this.mStep();
            double ll = this.logLikelihood();
            System.out.format("Iteration [%d/%d]: log-likelihood = %f", i + 1, nbIterations, ll);
            logLikelihoods.add(ll);
        }

        if (outputFolder != null) {
            this.saveResults(outputFolder, logLikelihoods);
            this.corpus.saveVocabulary(outputFolder + "/vocabulary.txt");
        }
    }

    /**
     * Initialize the various probabilities used by EM to random values.
     */
    protected void initialize() {
        int nbDocuments = corpus.size();
        int vocabularySize = corpus.getVocabularySize();
        Random r = new Random();

        Pd = new double[nbDocuments];
        Pw_z = new double[vocabularySize][nbTopics];
        Pz_d = new double[nbTopics][nbDocuments];
        Pz_dw = new double[nbTopics][nbDocuments][];

        // initialize Pw_z
        for (int z = 0; z < nbTopics; ++z) {
            double sum = 0;
            // generate random values
            for (int w = 0; w < vocabularySize; ++w) {
                Pw_z[w][z] = r.nextDouble();
                sum += Pw_z[w][z];
            }
            // normalize probabilities of each word for the topic
            for (int w = 0; w < vocabularySize; ++w) {
                Pw_z[w][z] /= sum;
            }
        }

        // initialize Pz_d
        for (int d = 0; d < nbDocuments; ++d) {
            double sum = 0;
            // generate random values
            for (int z = 0; z < nbTopics; ++z) {
                Pz_d[z][d] = r.nextDouble();
                sum += Pz_d[z][d];
            }
            // normalize probabilities of each topic for the document
            for (int z = 0; z < nbTopics; ++z) {
                Pz_d[z][d] /= sum;
            }
        }

        // init Pd and Pz_dw
        int corpusLength = 0;
        for (int d = 0; d < nbDocuments; ++d) {
            int docLength = corpus.getDocumentLength(d);
            int nbDistinctWordsInDoc = corpus.getNbWordsInDocument(d);
            corpusLength += docLength;

            Pd[d] = docLength;

            for (int z = 0; z < nbTopics; ++z) {
                Pz_dw[z][d] = new double[nbDistinctWordsInDoc];
            }
        }

        // normalize Pd
        for (int d = 0; d < nbDocuments; ++d) {
            Pd[d] /= corpusLength;
        }
    }

    /**
     * Computes the probability of each topic z generating word w in document d.
     */
    @SuppressWarnings("unused")
    protected void eStep() {
        int nbDocuments = corpus.size();

        for (int d = 0; d < nbDocuments; ++d) {
            for (Entry<Integer, Integer> e : corpus.getDocument(d).entrySet()) {
                int w_corpus = e.getKey();
                int w_doc = corpus.getWordPositionInDocument(w_corpus, d);

                // Note the difference between w_corpus and w_doc above: the
                // first gives the global ID of the word, while the second gives
                // the position of this word in the document. This position is
                // useful for the sparse storage of Pz_dw.

                // TODO
            }
        }
    }

    /**
     * Computes the topic distribution for each document, and the word
     * distribution for each topic.
     */
    @SuppressWarnings("unused")
    protected void mStep() {
        int vocabularySize = corpus.getVocabularySize();
        int nbDocuments = corpus.size();

        // Again, you will need the "local" id of words in each document, to use
        // Pz_dw. Use the corpus.getWordPositionInDocument method, similarly to
        // the e-step.

        // TODO
    }

    /**
     * Computes the log-likelihood of the corpus with the current probabilities.
     * 
     * @return
     */
    public double logLikelihood() {
        double ll = 0;
        int nbDocuments = corpus.size();

        for (int d = 0; d < nbDocuments; ++d) {
            for (Entry<Integer, Integer> e : corpus.getDocument(d).entrySet()) {
                int w_corpus = e.getKey();
                int n_d_w = e.getValue();

                double sumOverTopics = 0;
                for (int z = 0; z < nbTopics; ++z) {
                    sumOverTopics += Pw_z[w_corpus][z] * Pz_d[z][d];
                }
                ll += n_d_w * Math.log(Pd[d] * sumOverTopics);
            }
        }

        return ll;
    }

    /**
     * Save the resulting probabilities and log-likelihoods to the given output
     * folder.
     * 
     * @param outputFolder
     * @param logLikelihoods
     * @throws IOException
     */
    public void saveResults(String outputFolder, List<Double> logLikelihoods)
            throws IOException {
        IOUtils.write2DArray(Pz_d, outputFolder + "/Pz_d.txt");
        IOUtils.write2DArray(Pw_z, outputFolder + "/Pw_z.txt");
        IOUtils.write1DArray(logLikelihoods, outputFolder
                + "/loglikelihood.txt");
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.format("Usage: %s <corpus file> <output directory>",
                    PLSI.class.getName());
            System.exit(-1);
        }

        String corpusFileName = args[0];
        String outputFolder = args[1];

        int nbTopics = 5;
        int nbIterations = 500;

        new PLSI(corpusFileName, outputFolder, nbTopics, nbIterations);
    }
}
