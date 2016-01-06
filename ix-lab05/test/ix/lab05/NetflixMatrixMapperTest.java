package ix.lab05;

import static ix.lab05.processing.NetflixMatrix.TAB;
import ix.lab05.processing.NetflixMatrixMapper;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;


public class NetflixMatrixMapperTest {

    @Test
    public void test() {
        LongWritable inputKey = new LongWritable(1);
        Text inputValue = new Text("745"+TAB+"4398"+TAB+"4.0");

        IntWritable outputKey = new IntWritable(745);
        Text outputValue = new Text("4398"+TAB+"4.0");;

        new MapDriver<LongWritable, Text, IntWritable, Text>()
        .withMapper(new NetflixMatrixMapper())
        .withInput(inputKey, inputValue)
        .withOutput(outputKey, outputValue)
        .runTest();
    }
}
