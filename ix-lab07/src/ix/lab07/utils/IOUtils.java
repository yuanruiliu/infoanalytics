package ix.lab07.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class IOUtils {

    /**
     * Saves a 2D array to a text file, with one column per line, and elements
     * separated by a space.
     * 
     * @param data
     * @param fileName
     * @throws IOException
     */
    public static void write2DArray(double data[][], String fileName)
            throws IOException {
        File file = new File(fileName);

        // if file doesn't exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < data[0].length; i++) {
            for (int j = 0; j < data.length; j++) {
                if (j != 0) {
                    bw.write(" ");
                }
                bw.write(Double.toString(data[j][i]));
            }
            bw.write("\n");
        }

        bw.close();
        fw.close();
    }

    /**
     * Saves a 1D array to a text file, with each element separated by a space.
     * 
     * @param data
     * @param fileName
     * @throws IOException
     */
    public static void write1DArray(List<Double> data, String fileName)
            throws IOException {
        File file = new File(fileName);

        // if file doesn't exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);

        for (int i = 0; i < data.size(); i++) {
            bw.write(Double.toString(data.get(i)) + "\n");
        }

        bw.close();
        fw.close();
    }
}
