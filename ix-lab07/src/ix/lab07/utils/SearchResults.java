package ix.lab07.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.iterator.FileLineIterable;

/**
 * Small utility class used to nicely format and display search results, given
 * a bunch of (<docID>, <score>) pairs. In particular, it matches the <docID> to
 * its (human-interpretable) title.
 */
public class SearchResults {

    private static final String TITLES_PATH = "/ix/imdb-titles.txt";
    private static final String TAB = "\t";

    private final Map<Integer, Double> scores;
    private final FileSystem fs;


    /**
     * Constructor taking a Map of document IDs to scores.
     *
     * @param scores the scores of the documents
     * @param conf a Configuration instance used to fetch stuff on the HDFS cluster.
     */
    public SearchResults(Map<Integer, Double> scores, Configuration conf)
            throws IOException {
        this.scores = scores;
        this.fs = FileSystem.get(conf);
    }


    /**
     * Formats and displays the top K documents ordered by decreasing score.
     *
     * @param k the number of documents to display.
     */
    public void displayTopK(int k) throws IOException {
        Map<Integer, String> titles = getTitles(fs);
        List<Integer> docs = new ArrayList<Integer>(this.scores.keySet());
        Collections.sort(docs, new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                // From best to worst.
                return -1 * scores.get(first).compareTo(scores.get(second));
            }
        });
        for (int i = 0; i < Math.min(k, docs.size()); ++i) {
            int docID = docs.get(i);
            System.out.println(String.format("%02d. %s (score: %.5f)",
                    i+1, titles.get(docID), this.scores.get(docID)));
        }
    }


    /**
     * Returns a mapping between document IDs and movie titles by parsing the relevant
     * file located on the HDFS cluster.
     *
     * @param fs and FileSystem instance used to lookup the titles file
     * @return a Map between document IDs and movie titles
     */
    private static Map<Integer, String> getTitles(FileSystem fs) throws IOException {
        FSDataInputStream stream = fs.open(new Path(TITLES_PATH));
        Map<Integer, String> titles = new HashMap<Integer, String>();
        for (String line : new FileLineIterable(stream)) {
            String[] tokens = line.split(TAB);
            int docID = Integer.parseInt(tokens[0]);
            titles.put(docID, tokens[1]);
        }
        return titles;
    }


    /**
     * Populates a SearchResults instance based on files containing the document
     * scores, typically resulting from the output of a MapReduce job.
     *
     * @param resultsPath the path to the files containing the scores
     * @return an SearchResults instance containing the parsed data
     */
    public static SearchResults parse(String resultsPath) throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        Map<Integer, Double> scores = new HashMap<Integer, Double>();
        for (FileStatus file : fs.listStatus(new Path(resultsPath))) {
            if (file.getPath().getName().matches("part-r-\\d{5}")) {
                FSDataInputStream stream = fs.open(file.getPath());
                for (String line : new FileLineIterable(stream)) {
                    String[] tokens = line.split(TAB);
                    int docID = Integer.parseInt(tokens[0]);
                    double score = Double.parseDouble(tokens[1]);
                    scores.put(docID, score);
                }
            }
        }
        return new SearchResults(scores, conf);
    }

}
