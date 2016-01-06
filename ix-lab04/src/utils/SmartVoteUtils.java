package utils;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.apache.hadoop.thirdparty.guava.common.base.Charsets;
import org.apache.hadoop.thirdparty.guava.common.base.Preconditions;
import org.apache.hadoop.thirdparty.guava.common.collect.ImmutableMap;
import org.apache.hadoop.thirdparty.guava.common.io.Files;
import org.apache.hadoop.thirdparty.guava.common.primitives.Doubles;
import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.Line;

import Jama.Matrix;

/** Helper functions for the smartvote excercise. */
public class SmartVoteUtils {

    private static final String SEPARATOR = "\\|";

    /* Paths to the the files containing the data. */
    private static final String CANDIDATES_PATH = "data/sv-candidates.txt";
    private static final String QUESTIONS_PATH = "data/sv-questions.txt";

    /** Enumeration of all political parties of interest. */
    private static enum Party {
        GREENS,  // Les Verts
        SVP,  // Union democratique du centre
        SP,  // Parti socialiste
        FDP,  // Parti radical-democrate
        GL,  // Vert'liberaux
        CVP,   // Parti democrate-chretien
        OTHER
    }

    /** Mapping between names occuring in the dataset and political parties. **/
    private static final Map<String, Party> NAME_TO_PARTY = ImmutableMap.<String, Party>builder()
            .put("Grüne", Party.GREENS).put("JGrüne", Party.GREENS)
            .put("SVP", Party.SVP).put("JSVP", Party.SVP).put("jsvp", Party.SVP)
            .put("SP", Party.SP)
            .put("FDP", Party.FDP)
            .put("glp", Party.GL).put("jglp", Party.GL)
            .put("CVP", Party.CVP).put("JCVP", Party.CVP)
            .build();

    /** Mapping between political parties and colors used for the scatter plot. */
    private static final Map<Party, Color> PARTY_COLOR = ImmutableMap.<Party, Color>builder()
            .put(Party.GREENS, Color.GREEN)
            .put(Party.SVP, new Color(0, 127, 63))  // Dark green.
            .put(Party.SP, Color.RED)
            .put(Party.FDP, Color.BLUE)
            .put(Party.GL, Color.YELLOW)
            .put(Party.CVP, Color.ORANGE)
            .put(Party.OTHER, Color.LIGHT_GRAY)
            .build();


    /**
     * Reads the smartvote candidates dataset and returns a simple object with two fields:
     *
     * - one contains an N x M matrix where each of the N rows is a candidate, represented by a
     *   (row) vector of M values corresponding to his / her answers to the survey's questions.
     * - the other is a list that contains the political party of each of the N candidates.
     *
     * @return a container for the matrix and the political party affiliations
     */
    public static CandidatesData readCandidates() {
        File file = new File(CANDIDATES_PATH);
        List<String> affiliations = new ArrayList<String>();
        List<double[]> rows = new ArrayList<double[]>();
        try {
            for (String line : Files.readLines(file, Charsets.UTF_8)) {
                String[] fields = line.split(SEPARATOR);
                affiliations.add(fields[0]);
                double[] answers = new double[fields.length - 1];
                for (int i = 0; i < fields.length - 1; ++i) {
                    answers[i] = Double.parseDouble(fields[i+1]);
                }
                rows.add(answers);
            }
        } catch(IOException ioe) {
            throw new RuntimeException(ioe);
        }
        Matrix matrix = new Matrix(rows.toArray(new double[0][0]));
        return new CandidatesData(matrix, affiliations);
    }


    /**
     * Reads the file containing the list of the questions (in English) corresponding the
     * smartvote survey. The question matches with the ID of the row in the N x M data
     * matrix.
     *
     * @return a list with the questions of the survey
     */
    public static List<String> readQuestions() {
        File file = new File(QUESTIONS_PATH);
        try {
            return Files.readLines(file, Charsets.UTF_8);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    /**
     * Given the coordinates (on two dimensions) of each candidate and their party affiliation,
     * displays a scatterplot. The color depends on the party of the candidate.
     *
     * @param dim1  the coordinates along one dimension
     * @param dim2  the coordinates along another dimension
     * @param partyAffiliations  the political affiliation of each candidate
     */
    public static void plotProjection(double[] dim1, double[] dim2, List<String> partyAffiliations) {
        Preconditions.checkArgument(dim1.length == dim2.length && dim1.length == partyAffiliations.size(),
                "the two arrays and the the list of party affiliations must have the same dimension");

        // Initialize the list of coordinates for each party.
        Map<Party, List<Double>> xs = ImmutableMap.<Party, List<Double>>builder()
                .put(Party.GREENS, new ArrayList<Double>()).put(Party.SVP, new ArrayList<Double>())
                .put(Party.SP, new ArrayList<Double>()).put(Party.FDP, new ArrayList<Double>())
                .put(Party.GL, new ArrayList<Double>()).put(Party.CVP, new ArrayList<Double>())
                .put(Party.OTHER,  new ArrayList<Double>()).build();
        Map<Party, List<Double>> ys = ImmutableMap.<Party, List<Double>>builder()
                .put(Party.GREENS, new ArrayList<Double>()).put(Party.SVP, new ArrayList<Double>())
                .put(Party.SP, new ArrayList<Double>()).put(Party.FDP, new ArrayList<Double>())
                .put(Party.GL, new ArrayList<Double>()).put(Party.CVP, new ArrayList<Double>())
                .put(Party.OTHER,  new ArrayList<Double>()).build();

        // Distribute the points across the parties.
        for (int i = 0; i < dim1.length; ++i) {
            String name = partyAffiliations.get(i);
            if (NAME_TO_PARTY.containsKey(name)) {
                Party party = NAME_TO_PARTY.get(name);
                xs.get(party).add(dim1[i]);
                ys.get(party).add(dim2[i]);
            } else {
                xs.get(Party.OTHER).add(dim1[i]);
                ys.get(Party.OTHER).add(dim2[i]);
            }
        }

        // Plot the data.
        Plot2DPanel plot = new Plot2DPanel();
        plot.addLegend("EAST");
        for (Party party : Party.values()) {
            plot.addScatterPlot(party.toString(), PARTY_COLOR.get(party),
                    Doubles.toArray(xs.get(party)),
                    Doubles.toArray(ys.get(party)));
        }

        // Highlight the axes.
        plot.addPlotable(new Line(Color.BLACK,
                new double[]{plot.plotCanvas.base.getMinBounds()[0], 0},
                new double[]{plot.plotCanvas.base.getMaxBounds()[0], 0}));
        plot.addPlotable(new Line(Color.BLACK,
                new double[]{0, plot.plotCanvas.base.getMinBounds()[1]},
                new double[]{0, plot.plotCanvas.base.getMaxBounds()[1]}));

        JFrame frame = new JFrame("Plot");
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * Takes an array of real numbers, and returns the indices of the top three largest
     * elements, in terms of absolute value. Here's an example.
     *
     * input: {0.9, 0, -1, -0.5, 0.3}
     * output: {2, 0, 3}
     *
     * @param array  the array containing the values to be compared
     * @return the index of the three largest elements, in absolute value
     */
    public static Iterable<Integer> topThree(final double[] array) {
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < array.length; ++i) {
            indices.add(i);
        }
        Collections.sort(indices, new Comparator<Integer>() {
            @Override
            public int compare(Integer first, Integer second) {
                return Double.compare(  // Order is reverted!
                        Math.abs(array[second]), Math.abs(array[first]));
            }
        });
        return indices.subList(0, 3);
    }
}
