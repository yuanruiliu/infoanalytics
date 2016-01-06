package ix.lab07.lsi;

import ix.lab07.utils.DocumentTokenization;
import ix.lab07.utils.SearchResults;
import ix.lab07.vsm.Search;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

/**
 * Given a query string, computes a score for each document in the corpus using the
 * latent space model. The query is first expressed as a linear combination of the
 * representation of its terms in the latent space. Then, the cosine similarity is
 * computed with each document's representation in the latent space.
 *
 * NOTE: files (SVD matrices) are read from HDFS but, unlike previously, all the
 * computations are performed locally - there is no MapReduce job involved.
 */
public class LSISearch {

    private static final int NB_DOCS = 20;

    private Configuration conf = new Configuration();
    private Path svdPath;


    /**
     * @param svdPath path to the SVD decomposition as done by Mahout's `ssvd`
     *     command.
     */
    public LSISearch(String svdPath) {
        this.svdPath = new Path(svdPath);
    }

    /**
     * Performs a search on the document corpus given a query string, using the
     * latent space uncovered by the truncated SVD.
     *
     * @param query the query string
     * @return a SearchResults object that can be used to display the top results.
     */
    public SearchResults search(String query) throws IOException {
        // The query is first parsed into a Map of terms to weights.
        Map<String, Double> tokenMap = Search.getTokenMap(DocumentTokenization.stream(query));

        /* First step: query as a linear combination of the tokens in
         * the latent space.
         */
        Vector queryVector = null;

        SequenceFileDirIterable<Text, VectorWritable> terms = readSequence(
                new Path(this.svdPath, "U"), this.conf); // Terms are in the matrix U.
        for(Pair<Text, VectorWritable> pair : terms) {
            String term = pair.getFirst().toString();
            Vector vec = pair.getSecond().get();
            if (queryVector == null) {
                queryVector = new DenseVector(new double[vec.size()]);
            }

            if (tokenMap.containsKey(term)) {
                queryVector = queryVector.plus(vec.times(tokenMap.get(term)));
            }
        }

        /* Second step: compute the similarity of the query vector
         * with the latent space embedding of all the documents.
         */
        Map<Integer, Double> scores = new HashMap<Integer, Double>();

        SequenceFileDirIterable<IntWritable, VectorWritable> docs = readSequence(
                new Path(this.svdPath, "V"), this.conf);  // Documents are in the matrix V.
        for (Pair<IntWritable, VectorWritable> pair : docs) {
            int docID = pair.getFirst().get();
            Vector vec = pair.getSecond().get();
            double score = queryVector.dot(vec.normalize());

            scores.put(docID, score);
        }

        return new SearchResults(scores, this.conf);
    }


    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format(
                    "Usage: %s <path to svd> <query>", LSISearch.class));
            return;
        }
        LSISearch lsi = new LSISearch(args[0]);
        SearchResults results = lsi.search(Search.getQuery(args[1]));
        results.displayTopK(NB_DOCS);
    }


    /**
     * Reads a sequence file split in multiple parts in a folder.
     *
     * @param path path to the sequence file
     * @param conf Hadoop configuration parameters
     * @return an Iterable over the sequence file
     */
    public static <E extends Writable> SequenceFileDirIterable<E, VectorWritable> readSequence(
            Path path, Configuration conf) {
        return new SequenceFileDirIterable<E, VectorWritable>(
                path, PathType.LIST, PathFilters.logsCRCFilter(), conf);
    }
}
