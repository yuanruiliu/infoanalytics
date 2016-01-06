package ix.lab07.vsm;

import ix.lab07.utils.TermDocumentPair;
import ix.lab07.vsm.DocumentLength.DocumentLengthMapper;
import ix.lab07.vsm.DocumentLength.DocumentLengthReducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

public class DocumentLengthTest {

    @Test
    public void testDocumentLengthMapper() {
        Text inputKey = new Text("feel:9286");
        Text inputValue = new Text("625");

        IntWritable outputKey = new IntWritable(9286);
        Text outputValue = new Text("feel:625");

        MapDriver.newMapDriver(new DocumentLengthMapper())
                .withInput(inputKey, inputValue)
                .withOutput(outputKey, outputValue)
                .runTest();
    }

    @Test
    public void testDocumentLengthReducer() throws IOException {
        // Input.
        IntWritable inputKey = new IntWritable(9286);
        List<Text> inputValues = Arrays.asList(
                new Text("feel:625"), new Text("john:5"));

        // Expected output.
        TermDocumentPair outputKey1 = new TermDocumentPair("feel", 9286);
        Text outputVal1 = new Text("625:630");
        TermDocumentPair outputKey2 = new TermDocumentPair("john", 9286);
        Text outputVal2 = new Text("5:630");

        ReduceDriver.newReduceDriver(new DocumentLengthReducer())
                .withInput(inputKey, inputValues)
                .withOutput(outputKey1, outputVal1)
                .withOutput(outputKey2, outputVal2)
                .runTest();
    }

}
