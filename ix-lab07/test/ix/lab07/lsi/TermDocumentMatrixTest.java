package ix.lab07.lsi;

import static org.junit.Assert.assertEquals;
import ix.lab07.lsi.TermDocumentMatrix.TermDocumentMatrixMapper;
import ix.lab07.lsi.TermDocumentMatrix.TermDocumentMatrixReducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class TermDocumentMatrixTest {

    private static final double EPSILON = 0.0001;

    @Test
    public void testTermDocumentMatrixMapper() {
        Text inputKey = new Text("feel:9286");
        Text inputValue = new Text("0.01763");

        Text outputKey = new Text("feel");
        Text outputValue = new Text("9286:" + new Double("0.01763"));

        MapDriver.newMapDriver(new TermDocumentMatrixMapper())
                .withInput(inputKey, inputValue)
                .withOutput(outputKey, outputValue)
                .runTest();
    }

    @Test
    public void testTermDocumentMatrixReducer() throws IOException {
        // Input.
        Text inputKey = new Text("feel");
        List<Text> inputValues = Arrays.asList(
                new Text("0:0.01763"), new Text("2:0.23295"));

        Pair<Text, VectorWritable> output = Iterables.getOnlyElement(
                ReduceDriver.newReduceDriver(new TermDocumentMatrixReducer())
                        .withInput(inputKey, inputValues)
                        .run());
        assertEquals(inputKey, output.getFirst());
        Vector vec = output.getSecond().get();
        assertEquals(0.01763, vec.get(0), EPSILON);
        assertEquals(0.00000, vec.get(1), EPSILON);
        assertEquals(0.23295, vec.get(2), EPSILON);
    }

}
