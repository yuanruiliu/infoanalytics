package ix.lab07.vsm;

import static org.junit.Assert.assertEquals;
import ix.lab07.utils.DocumentTokenization;
import ix.lab07.vsm.Search;
import ix.lab07.vsm.Search.SearchMapper;
import ix.lab07.vsm.Search.SearchReducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

public class SearchTest {

    private static final double EPSILON = 0.0001;

    @Test
    public void testSearchMapper() {
        Text inputKey = new Text("feel:9286");
        Text inputValue = new Text("0.017234");

        IntWritable outputKey = new IntWritable(9286);
        Text outputValue = new Text("feel:0.017234");

        MapDriver.newMapDriver(new SearchMapper())
                .withInput(inputKey, inputValue)
                .withOutput(outputKey, outputValue)
                .runTest();
    }

    @Test
    public void testSearchReducer() throws IOException {
        // Input.
        IntWritable inputKey = new IntWritable(9286);
        List<Text> inputValues = Arrays.asList(
                new Text("feel:0.123"), new Text("john:0.0456"), new Text("hue:0.9"));

        // Query string.
        String tokens = Joiner.on(Search.SEPARATOR).join(
                DocumentTokenization.stream("john feels the feeling"));
        Configuration conf = new Configuration();
        conf.set("queryTokens", tokens);

        // (0.0456 + 2 * 0.123) / sqrt(0.123^2 + 0.0456^2 + 0.9^2)
        double score = 0.3206122133;

        Pair<IntWritable, DoubleWritable> output = Iterables.getOnlyElement(
                ReduceDriver.newReduceDriver(new SearchReducer())
                        .withConfiguration(conf)
                        .withInput(inputKey, inputValues)
                        .run());
        assertEquals(inputKey, output.getFirst());
        assertEquals(score, output.getSecond().get(), EPSILON);
    }

}
