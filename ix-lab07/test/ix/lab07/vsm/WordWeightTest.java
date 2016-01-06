package ix.lab07.vsm;

import ix.lab07.utils.TermDocumentPair;
import ix.lab07.vsm.TfIdf;
import ix.lab07.vsm.WordWeight.WordWeightMapper;
import ix.lab07.vsm.WordWeight.WordWeightReducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

public class WordWeightTest {

    @Test
    public void testWordWeightMapper() {
        Text inputKey = new Text("feel:9286");
        Text inputValue = new Text("625:630");

        Text outputKey = new Text("feel");
        Text outputValue = new Text("9286:625:630");

        MapDriver.newMapDriver(new WordWeightMapper())
                .withInput(inputKey, inputValue)
                .withOutput(outputKey, outputValue)
                .runTest();
    }

    @Test
    public void testWordWeightReducer() throws IOException {
        // Input.
        Text inputKey = new Text("feel");
        List<Text> inputValues = Arrays.asList(
                new Text("9286:625:630"), new Text("7329:53:7239"));

        // Expected output.
        double idf = TfIdf.inverseDocFrequency(2);
        TermDocumentPair outputKey1 = new TermDocumentPair("feel", 9286);
        DoubleWritable outputVal1 = new DoubleWritable(TfIdf.termFrequency(625, 630) * idf);
        TermDocumentPair outputKey2 = new TermDocumentPair("feel", 7329);
        DoubleWritable outputVal2 = new DoubleWritable(TfIdf.termFrequency(53, 7239) * idf);

        ReduceDriver.newReduceDriver(new WordWeightReducer())
                .withInput(inputKey, inputValues)
                .withOutput(outputKey1, outputVal1)
                .withOutput(outputKey2, outputVal2)
                .runTest();
    }

}
