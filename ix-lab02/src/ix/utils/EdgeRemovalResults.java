package ix.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for saving a list of results to a text file.
 */
public class EdgeRemovalResults {

    private List<Integer> list = new ArrayList<Integer>();

    public void add(int val) {
        this.list.add(val);
    }

    /**
     * Saves a list of results to a text file.
     * @param fileName The name of the file
     */
    public void saveToFile(String fileName) {
        try{
            FileWriter writer = new FileWriter(fileName);
            BufferedWriter buffer = new BufferedWriter(writer);
            for (Integer val: this.list){
                buffer.write(val + "\n");
            }
            buffer.close();
            writer.close();
      } catch (IOException e){
            throw new RuntimeException(e);
      }
    }

}
