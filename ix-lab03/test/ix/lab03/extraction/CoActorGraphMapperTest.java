package ix.lab03.extraction;

import ix.lab03.extraction.CoActorGraphMapper;
import ix.utils.TextArrayWritable;

import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the CoActorMapper works correctly
 */
public class CoActorGraphMapperTest {

    Configuration conf = new Configuration();

    @Test
    public void withLinksTest() {
        // first set config
        conf.set("startingYear", "1999");
        conf.set("endingYear", "1999");
        conf.set("castThreshold", "1");

        // define input key/values
        Text inputKey = new Text("The matrix (1999)");
        Text inputValue = new Text("Carrie-Anne Moss, Keanu Reeves, Laurence Fishburne");

        // expected output keys/values
        Text outputKey1 = new Text("Carrie-Anne Moss");
        TextArrayWritable outputValue1 = new TextArrayWritable().setStringCollection(Arrays.asList("Keanu Reeves", "Laurence Fishburne"));

        Text outputKey2 = new Text("Keanu Reeves");
        TextArrayWritable outputValue2 = new TextArrayWritable().setStringCollection(Arrays.asList("Carrie-Anne Moss", "Laurence Fishburne"));

        Text outputKey3 = new Text("Laurence Fishburne");
        TextArrayWritable outputValue3 = new TextArrayWritable().setStringCollection(Arrays.asList("Carrie-Anne Moss", "Keanu Reeves"));

        // create map driver and set the configuration
        MapDriver<Text, Text, Text, TextArrayWritable> md = new MapDriver<Text, Text, Text, TextArrayWritable>();
        md.setConfiguration(conf);

        // run test
        md.withMapper(new CoActorGraphMapper())
            .withInput(inputKey, inputValue)
            .withOutput(outputKey1, outputValue1)
            .withOutput(outputKey2, outputValue2)
            .withOutput(outputKey3, outputValue3).runTest();
    }

}
