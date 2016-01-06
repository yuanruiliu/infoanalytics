package ix.lab02.degdist;

import ix.utils.Edge;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * This mapper takes an edge of Wikipedia graph and extracts its source
 * and destination. For example:
 *
 * ## Input:
 *
 * Article1|Article2	1
 *
 * ## Output:
 *
 * Article1		Article2
 * Article2		Article1
 */
@SuppressWarnings("unused")
public class NeighborsSetMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text outputKey = new Text(); //  edge source article name
    private Text outputValue = new Text(); // edge destination article name

    private Edge edge = new Edge();

    /**
     * Map operation: this function is called for each line of the input file.
     * It simply extracts the source article name as key, and the linked article name as value
     *
     * @param inputKey
     *            Offset of the current line in the input file
     * @param inputValue
     *            Content of the line which is an edge in wikipedia graph
     * @param context
     *            Job context, for writing output, getting configuration, etc.
     */
    public void map(LongWritable inputKey, Text inputValue, Context context) throws IOException, InterruptedException {

        // TODO Write the mapper. Use the attribute 'edge' to parse the line.
    	String line = inputValue.toString();
    	
    	this.edge.parse(line);
    	
    	outputKey.set(new Text(this.edge.sourceArticleName));
    	
    	outputValue.set(new Text(this.edge.destinationArticleName));
    	
    	context.write(outputKey, outputValue);
    	
    	context.write(outputValue, outputKey);

    }

}
