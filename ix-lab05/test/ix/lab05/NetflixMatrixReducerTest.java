package ix.lab05;

import static ix.lab05.processing.NetflixMatrix.TAB;
import static org.junit.Assert.assertEquals;
import ix.lab05.processing.NetflixMatrixReducer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.Test;

import com.google.common.collect.Iterables;

public class NetflixMatrixReducerTest {

    private static final double EPSILON = 0.0001;

    @Test
    public void test() throws IOException {
        IntWritable inputKey = new IntWritable(745);

        List<Text> inputValues = Arrays.asList(
                new Text("439"+TAB+"4.0"),
                new Text("262"+TAB+"1.0"),
                new Text("0"+TAB+"2.0"),
                new Text("177"+TAB+"5.0"));

        Vector ratings = new SequentialAccessSparseVector(500, 1);
        ratings.set(439, 4.0);
        ratings.set(262, 1.0);
        ratings.set(0, 2.0);
        ratings.set(177, 5.0);

        Pair<IntWritable, VectorWritable> pair = Iterables.getOnlyElement(
                new ReduceDriver<IntWritable, Text, IntWritable, VectorWritable>()
                .withReducer(new NetflixMatrixReducer())
                .withInput(inputKey, inputValues)
                .run());

        assertEquals(inputKey.get(), pair.getFirst().get());
        for (int i = 0; i < ratings.size(); ++i) {
            assertEquals(ratings.get(i), pair.getSecond().get().get(i), EPSILON);
        }
    }
}
