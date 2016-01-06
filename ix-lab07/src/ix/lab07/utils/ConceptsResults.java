package ix.lab07.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.FileLineIterable;

/** Utility class used to parse and display the output on the Concepts job. */
public class ConceptsResults {

    private static final String FILE_PATTERN = "part-r-\\d{5}";
    private static final String TAB = "\t";
    private static final String COLON = ":";

    private static final Comparator<Pair<String, Double>> VALUE_COMPARATOR =
            new Comparator<Pair<String, Double>>() {
        @Override
        public int compare(Pair<String, Double> fst, Pair<String, Double> snd) {
            return fst.getSecond().compareTo(snd.getSecond());
        }
    };

    private Map<Integer, List<Pair<String, Double>>> words;

    private ConceptsResults(Map<Integer, List<Pair<String, Double>>> words) {
        this.words = words;
    }


    /** Display the extremal words for the first few dimensions of the latent space. */
    public void display() {
        for (Integer key : this.words.keySet()) {
            List<Pair<String, Double>> list = this.words.get(key);

            Collections.sort(list, VALUE_COMPARATOR);
            int middle = list.size() / 2;

            System.out.println(String.format(
                    "\n\n-- dimension %d - terms with lowest values:", key + 1));
            for (Pair<String, Double> pair : list.subList(0, middle)) {
                System.out.print(pair.getFirst() + " ");
            }

            System.out.println(String.format(
                    "\n\n-- dimension %d - terms with highest values:", key + 1));
            for (Pair<String, Double> pair : list.subList(middle, list.size())) {
                System.out.print(pair.getFirst() + " ");
            }
        }
    }


    /**
     * Parses the results of a Concepts MapReduce job and populates a
     * ConceptsResults instance.
     *
     * @param path the path to the output of the Concepts MapReduce job
     * @return a ConceptResult instance containing the parsed data.
     */
    public static ConceptsResults parse(String path)
            throws NumberFormatException, IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        Map<Integer, List<Pair<String, Double>>> words =
                new TreeMap<Integer, List<Pair<String, Double>>>();
        for (FileStatus file : fs.listStatus(new Path(path))) {
            if (file.getPath().getName().matches(FILE_PATTERN)) {
                FSDataInputStream stream = fs.open(file.getPath());
                for (String line : new FileLineIterable(stream)) {
                    // Get key & val.
                    String[] keyVal = line.split(TAB);
                    int dim = Integer.parseInt(keyVal[0]);
                    String val = keyVal[1];

                    // Get word & val for component.
                    String[] wordScore = val.split(COLON);
                    String word = wordScore[0];
                    double score = Double.parseDouble(wordScore[1]);

                    // Add it to the map.
                    if (!words.containsKey(dim)) {
                        words.put(dim, new ArrayList<Pair<String, Double>>());
                    }
                    words.get(dim).add(new Pair<String, Double>(word, score));
                }
            }
        }
        return new ConceptsResults(words);
    }

}
