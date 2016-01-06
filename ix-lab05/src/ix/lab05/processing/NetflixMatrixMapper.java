package ix.lab05.processing;

import static ix.lab05.processing.NetflixMatrix.TAB;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

@SuppressWarnings("unused")
public class NetflixMatrixMapper extends
        Mapper<LongWritable, Text, IntWritable, Text> {

    private IntWritable outputKey = new IntWritable(); // userID
    private final Text outputValue = new Text(); // "movieID<TAB>rating"

    /**
     * Takes a string of the form "userID<TAB>movieID<TAB>rating>" and maps it
     * to the key userID with the value "movieID<TAB>rating".
     */
    @Override
    public void map(LongWritable inputKey, Text inputValue, Context context)
            throws IOException, InterruptedException {

        String[] tokens = inputValue.toString().split(TAB);

        // TODO
        outputKey.set(Integer.parseInt(tokens[0]));
        outputValue.set(tokens[1]+TAB+tokens[2]);
        context.write(outputKey, outputValue);
    }

}
