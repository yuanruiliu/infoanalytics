package ix.lab03.extraction;

import ix.lab03.extraction.MovieCastMapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

/** Unit tests for MovieCastMapper. */
public class MovieCastMapperTest {

    @Test
    public void withLinksTest() {
        LongWritable inputKey = new LongWritable(1);
        Text inputValue = new Text("Keanu Reeves,The matrix (1999)");

        Text outputKey = new Text("The matrix (1999)");
        Text outputValue = new Text("Keanu Reeves");

        new MapDriver<LongWritable, Text, Text, Text>()
                .withMapper(new MovieCastMapper())
                .withInput(inputKey, inputValue)
                .withOutput(outputKey, outputValue).runTest();
    }

}
