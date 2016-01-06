package ix.lab06.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class DataUtils {

    /**
     * Read content of fileName into a list of Points.
     * 
     * @param fileName
     * @return An array of points.
     * @throws IOException
     */
    public static Point2d[] readFromFile(String fileName) throws IOException {
        BufferedReader file = new BufferedReader(new FileReader(fileName));
        List<Point2d> points = new ArrayList<Point2d>();

        String line;
        while ((line = file.readLine()) != null) {
            String[] coords = line.split(",");
            double x = Double.parseDouble(coords[0]);
            double y = Double.parseDouble(coords[1]);
            points.add(new Point2d(x, y));
        }
        file.close();// close the file

        return points.toArray(new Point2d[0]);
    }

    /**
     * Shows a scatter plot of the data points given.
     * 
     * Each set of point in the list is shown in a different color, given in the
     * second argument.
     * 
     * @param points
     *            A list of sets of points to plot, each set in its own color.
     * @param colors
     *            A list of color to assign to each set of points.
     */
    public static void scatterPlot(List<Point2d[]> points, List<Color> colors) {
        Plot2DPanel plot = new Plot2DPanel();

        for (int i = 0; i < points.size(); i++) {
            Point2d[] clusterPoints = points.get(i);
            Color clusterColor = colors.get(i);

            double[] x = new double[clusterPoints.length];
            double[] y = new double[clusterPoints.length];
            for (int p = 0; p < clusterPoints.length; p++) {
                x[p] = clusterPoints[p].getX();
                y[p] = clusterPoints[p].getY();
            }
            plot.addScatterPlot("", clusterColor, x, y);
        }

        JFrame frame = new JFrame("ScatterPlot");
        frame.setContentPane(plot);
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Write the given communities assignment to the given file.
     * 
     * @param node2com
     *            The communities assignment.
     * @param fileName
     *            The destination file name.
     */
    public static void writeNodeCommunities(Map<String, Integer> node2com,
            String fileName) {
        try {
            File file = new File(fileName);
            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (String key : node2com.keySet()) {
                bw.write(key + "\t");
                bw.write(Integer.toString(node2com.get(key)) + "\n");
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
