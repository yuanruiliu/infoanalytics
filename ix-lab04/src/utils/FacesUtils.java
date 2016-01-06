package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import Jama.Matrix;

/** Helper functions for the "Faces" exercise. */
public class FacesUtils {

    private static final String SEPARATOR = "\\|";

    /** Path to the faces dataset. */
    private static final String PATH = "data/faces.txt";

    /** Number of faces for which a graphic version is available. */
    private static final int THRESHOLD = 1000;


    /**
     * Reads the faces dataset and returns an N x M matrix where each of the
     * N rows is a face, represented by a (row) vector of M values.
     *
     * @return an N x M matrix representing the dataset
     */
    public static Matrix readFacesData() {
        FileInputStream input = null;
        try {
            input = new FileInputStream(PATH);
        } catch (FileNotFoundException fnfe) {
            throw new RuntimeException(fnfe);
        }
        Scanner scanner = new Scanner(input);
        List<double[]> rows = new ArrayList<double[]>();
        while (scanner.hasNextLine()) {
            String[] values = scanner.nextLine().split(SEPARATOR);
            double[] row = new double[values.length];
            for (int i = 0; i < values.length; ++i) {
                row[i] = Double.parseDouble(values[i]);
            }
            rows.add(row);
        }
        scanner.close();
        return new Matrix(rows.toArray(new double[0][0]));
    }


    /**
     * Prints the values in a particular row of the data matrix.
     *
     * @param data the dataset, as an N x M matrix
     * @param id the row number (0-indexed)
     */
    public static void printRow(Matrix data, int id) {
        assert id >= 0 && id < data.getRowDimension();

        double[] values = data.getArray()[id];
        System.out.println(String.format("Values for row %d:", id));
        for (int i = 0; i < data.getColumnDimension(); ++i) {
            System.out.println(String.format("  [dim %d] %.3f", i+1, values[i]));
        }
    }


    /** Small helper class to sort the data items by component values. */
    private static class Entry implements Comparable<Entry> {
        public final int id;
        public final double val;

        public Entry(int id, double val) {
            this.id = id;
            this.val = val;
        }

        @Override
        public int compareTo(Entry that) {
            return this.val < that.val ? -1 : this.val > that.val ? 1 : 0;
        }
    }


    /**
     * Prints which of the N items (see note) in the dataset (represented by an
     * N x M matrix) have the smallest, respectively the largest values along a
     * particular dimension.
     *
     * Note: actually, only faces for which there is a graphic representation are
     * considered (i.e. not *all* N items).
     *
     * @param data the dataset, as an N x M matrix
     * @param dim the dimension along which to compare the values, between 1 and M
     * @param nbElems the number of largest / smallest items to print
     */
    public static void printExtremes(Matrix data, int dim, int nbElems) {
        assert dim > 0 && dim <= data.getColumnDimension();

        TreeSet<Entry> elems = new TreeSet<Entry>();
        for (int i = 0; i < THRESHOLD; ++i) {
            elems.add(new Entry(i, data.get(i, dim-1)));
        }
        for (String dir : new String[]{"smallest", "largest"}) {
            System.out.println(
                    String.format("Items with %s values on dimension %d:", dir, dim));
            Iterator<Entry> it = "smallest".equals(dir)
                    ? elems.iterator()  // Ascending order.
                    : elems.descendingIterator();  // Descending order.
            for (int i = 0; i < nbElems; ++i) {
                Entry curr = it.next();
                System.out.println(String.format(
                        "  %04d (value: %.3f)", curr.id, curr.val));
            }
        }
    }
}
