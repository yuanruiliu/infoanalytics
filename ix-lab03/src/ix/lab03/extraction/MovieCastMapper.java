package ix.lab03.extraction;

import ix.utils.IMDbEntry;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

@SuppressWarnings("unused")
public class MovieCastMapper extends Mapper<LongWritable, // input key: line offset
                                            Text, // input value: line content
                                            Text, // output key: movie title
                                            Text> { // output value: actor name

    private Text outputKey = new Text();
    private Text outputValue = new Text();

    private IMDbEntry imdbEntry = new IMDbEntry();

    /**
     * Example:
     * Input - value="Keanu Reeves,The matrix (1999)"
     * Output - key="The matrix (1999)", value="Keanu Reeves"
     */
    public void map(LongWritable inputKey, Text inputValue, Context context)
            throws IOException, InterruptedException {
        // we first get the line content
        String line = inputValue.toString();

        if (this.imdbEntry.parse(line)) {
            //TODO
        	this.outputKey.set(this.imdbEntry.getTitle());
        	
        	this.outputValue.set(this.imdbEntry.actor);
        		
        	context.write(this.outputKey, this.outputValue);
        }
    }

}
