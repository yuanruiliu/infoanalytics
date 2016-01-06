package ix.lab03.extraction;

import ix.lab03.extraction.MovieCastReducer;
import ix.utils.TextArrayWritable;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

/** Unit tests for MovieCastReducer. */
public class MovieCastReducerTest {

    @Test
    public void latestTest() {
        Text inputKey = new Text("The matrix (1999)");
        List<Text> inputValues = Arrays.asList(new Text("Keanu Reeves"), new Text(
                "Laurence Fishburne"), new Text("Carrie-Anne Moss"), new Text(
                "Keanu Reeves"));

        List<String> castSet = Arrays.asList("Carrie-Anne Moss",
                "Keanu Reeves", "Laurence Fishburne");
        TextArrayWritable outputValue = new TextArrayWritable();
        outputValue.setStringCollection(castSet);

        new ReduceDriver<Text, Text, Text, TextArrayWritable>()
                .withReducer(new MovieCastReducer())
                .withInput(inputKey, inputValues)
                .withOutput(inputKey, outputValue)
                .runTest();
    }

}
