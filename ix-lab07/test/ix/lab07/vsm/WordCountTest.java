package ix.lab07.vsm;

import ix.lab07.utils.DocumentTokenization;
import ix.lab07.utils.TermDocumentPair;
import ix.lab07.vsm.WordCount.WordCountMapper;
import ix.lab07.vsm.WordCount.WordCountReducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

public class WordCountTest {

    private static final String LINE = "mission to prove that eating great food "
            + "doesn't have to break the bank. In";

    @Test
    public void testWordCountMapper() {
        Text inputKey = new Text("009286");
        Text inputValue = new Text(LINE);
        IntWritable one = new IntWritable(1);

        MapDriver<Text, Text, TermDocumentPair, IntWritable> driver =
                MapDriver.newMapDriver(new WordCountMapper()).withInput(inputKey, inputValue);

        for (String token : DocumentTokenization.stream(LINE)) {
            driver.addOutput(new Pair<TermDocumentPair, IntWritable>(
                    new TermDocumentPair(token, 9286), one));
        }
        driver.runTest();
    }

    @Test
    public void testWordCountReducer() throws IOException {
        TermDocumentPair inputKey = new TermDocumentPair("feel", 9286);
        List<IntWritable> inputValues = Arrays.asList(
                new IntWritable(1), new IntWritable(3));

        ReduceDriver.newReduceDriver(new WordCountReducer())
                .withInput(inputKey, inputValues)
                .withOutput(inputKey, new IntWritable(1 + 3))
                .runTest();
    }
}
