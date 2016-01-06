package ix.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.common.iterator.sequencefile.PathFilters;
import org.apache.mahout.common.iterator.sequencefile.PathType;
import org.apache.mahout.common.iterator.sequencefile.SequenceFileDirIterable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

/** A bunch of utilities for the lab. */
public final class LabUtils {

    /** Class is non-instantiable. */
    private LabUtils() {
    }

    /**
     * Returns an Iterable over key-value pairs of a sequence file. Keys are
     * expected to be IntWritable, and values VectorWritable.
     * 
     * @param path
     *            path to the sequence file
     * @param conf
     *            Hadoop configuration parameters
     * @return an Iterable over the sequence file
     */
    public static SequenceFileDirIterable<IntWritable, VectorWritable> readSequence(
            Path path, Configuration conf) {
        return new SequenceFileDirIterable<IntWritable, VectorWritable>(path,
                PathType.LIST, PathFilters.partFilter(), conf);
    }

    /**
     * Reads a sequence file containing a matrix (stored row by row) into a Map
     * (in order to keep the sparsity).
     * 
     * @param path
     *            path to the sequence file
     * @param conf
     *            Hadoop configuration parameters
     * @return the matrix represented a map
     */
    public static Map<Integer, Vector> readMatrixByRows(Path path,
            Configuration conf) {
        Map<Integer, Vector> matrix = new HashMap<Integer, Vector>();

        for (Pair<IntWritable, VectorWritable> pair : readSequence(path, conf)) {
            int rowIndex = pair.getFirst().get();
            Vector row = pair.getSecond().get().clone();
            matrix.put(rowIndex, row);
        }
        return matrix;
    }

    /**
     * Returns the Vector associated to a particular user in the sequence file.
     * 
     * @param userID
     *            user for which to get the row
     * @param path
     *            path to the sequence file
     * @param conf
     *            Hadoop configuration parameters
     * @return the matrix row for the user
     */
    public static Vector readUserRow(int userID, Path path, Configuration conf) {
        for (Pair<IntWritable, VectorWritable> pair : readSequence(path, conf)) {
            if (pair.getFirst().get() == userID) {
                return pair.getSecond().get().clone();
            }
        }
        throw new RuntimeException(
                String.format("User '%d' not found.", userID));
    }

    /**
     * Parses the netflix "movies_title.txt" file and returns a mapping between
     * movie ID and a string in the format "<TITLE> (<YEAR>)".
     * 
     * @param path
     *            path to the file "movies_title.txt"
     * @return a mapping between movie IDs and titles
     */
    public static Map<Integer, String> parseMovieTitles(String path) {
        FileInputStream input = null;
        try {
            input = new FileInputStream(path);
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe);
        }
        Scanner scanner = new Scanner(input);
        Map<Integer, String> titles = new HashMap<Integer, String>();
        while (scanner.hasNextLine()) {
            String[] values = scanner.nextLine().split(",");
            int movieID = Integer.parseInt(values[0]);
            titles.put(movieID, String.format("%s (%s)", values[2], values[1]));
        }
        return titles;
    }

    /**
     * Nicely formats and prints a list of recommendations.
     * 
     * @param top
     *            the list of recommendations to output
     * @param titlesPath
     *            path to the file "movies_title.txt"
     */
    public static void printRatings(List<Pair<Integer, Double>> movies,
            String titlesPath) {
        Map<Integer, String> titles = parseMovieTitles(titlesPath);
        for (int count = 0; count < movies.size(); ++count) {
            int movieID = movies.get(count).getFirst();
            double rating = movies.get(count).getSecond();

            System.out.println(String.format(
                    "%d - %s (predicted rating: %.2f)", count + 1,
                    titles.get(movieID), rating));
        }
    }

    /**
     * Gets the list of the top k movies, sorted by ratings in descending order.
     * 
     * @param k
     *            The number of movies to get.
     * @param ratings
     *            A map of movie ratings
     * @return The top k movies
     */
    public static List<Pair<Integer, Double>> topK(int k,
            Map<Integer, Double> ratings) {
        // add all ratings to a list
        List<Pair<Integer, Double>> ratingsList = new ArrayList<Pair<Integer, Double>>();
        for (Integer movie : ratings.keySet()) {
            ratingsList
                    .add(new Pair<Integer, Double>(movie, ratings.get(movie)));
        }

        // sort list by descending ratings
        Collections.sort(ratingsList, new Comparator<Pair<Integer, Double>>() {
            @Override
            public int compare(Pair<Integer, Double> o1,
                    Pair<Integer, Double> o2) {
                return o2.getSecond().compareTo(o1.getSecond());
            }
        });

        // return first k items of the list
        return ratingsList.subList(0, k);
    }
}
