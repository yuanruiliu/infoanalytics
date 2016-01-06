package utils;

import java.util.Scanner;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import Jama.Matrix;

/** Utility functions shared across the lab. */
public class Common {


    /* Matrix manipulation. */

    /**
     * Computes the mean of a dataset represented by an N x M matrix. It returns
     * an array of M values containing, for each column, the mean over the N rows.
     *
     * @param data an N x M matrix used to compute the mean.
     * @return an array of M elements containing the mean for each column.
     */
    public static double[] getColMean(Matrix data) {
        // Takes the mean over the *rows*.
        double[] mean = new double[data.getColumnDimension()];
        // Compute the sum of the values in each column.
        for (double[] row : data.getArray()) {
            for (int i = 0; i < row.length; ++i) {
                mean[i] += row[i];
            }
        }
        // Normalize to get the average.
        double coeff = 1.0 / data.getRowDimension();
        for (int i = 0; i < mean.length; ++i) {
            mean[i] *= coeff;
        }
        return mean;
    }

    /**
     * Returns a centered version of a dataset represented by an N x M matrix.
     * This amounts to substracting the mean off of each row. The resulting
     * centered matrix is such that the sum of its rows is the zero vector.
     *
     * @param matrix the dataset to be centered.
     * @return the centered version of the input matrix.
     */
    public static Matrix center(Matrix data) {
        double[] mean = getColMean(data);
        // Create the matrix of deltas (to be added to the original matrix).
        double[][] deltas = new double[data.getRowDimension()][];
        for (int i = 0; i < deltas.length; ++i) {
            deltas[i] = mean;
        }
        return data.minus(new Matrix(deltas));
    }


    /* Plotting functions. */

    private static void scatterPlot(double[] data, String legend, String scale) {
        Plot2DPanel plot = new Plot2DPanel();
        plot.addScatterPlot(legend, data);
        plot.setAxisScale(1, scale);

        JFrame frame = new JFrame("Plot");
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /** Simple linear plot of an array of doubles. */
    public static void linPlot(double[] values) {
        scatterPlot(values, "Values", "lin");
    }

    /** Plot with log scale for the Y axis. */
    public static void logPlot(double[] values) {
        scatterPlot(values, "Values", "log");
    }


    /* Getting user input. */

    private static Scanner prompt(String message) {
        Scanner reader = new Scanner(System.in);
        System.out.print(message);
        return reader;
    }

    /** Read a string from stdin. */
    public static String getString(String message) {
        return prompt(message).nextLine();
    }

    /** Read an integer from stdin. */
    public static int getInt(String message) {
        return prompt(message).nextInt();
    }
}
