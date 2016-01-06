package ix.lab03.extraction;

import ix.lab03.extraction.CoActorGraphReducer;
import ix.utils.TextArrayWritable;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the MovieCastReducer works correctly
 */
public class CoActorGraphReducerTest {

    @Test
    public void latestTest() {
        Text inputKey = new Text("Keanu Reeves");
        TextArrayWritable cast1 = new TextArrayWritable().setStringCollection(Arrays.asList("Carrie-Anne Moss", "Laurence Fishburne"));
        TextArrayWritable cast2 = new TextArrayWritable().setStringCollection(Arrays.asList("Laurence Fishburne", "Hugo Weaving"));
        List<TextArrayWritable> inputValue = Arrays.asList(cast1, cast2);

        TextArrayWritable outputValue = new TextArrayWritable().setStringCollection(Arrays.asList("Carrie-Anne Moss", "Hugo Weaving", "Laurence Fishburne"));

        new ReduceDriver<Text, TextArrayWritable, Text, TextArrayWritable>()
                .withReducer(new CoActorGraphReducer())
                .withInput(inputKey, inputValue)
                .withOutput(inputKey, outputValue)
                .runTest();
    }

}
