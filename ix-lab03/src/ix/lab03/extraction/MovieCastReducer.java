package ix.lab03.extraction;

import ix.utils.TextArrayWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

@SuppressWarnings("unused")
public class MovieCastReducer extends Reducer<Text, // input key: movie name
                                              Text, // input values: list of actors
                                              Text, // output key: movie name
                                              TextArrayWritable> { // output value: the cast represented as a set of actors

    private TextArrayWritable outputValue = new TextArrayWritable();
    private List<String> list = new ArrayList<String>();
    /**
     * The reduce operation collects all the actors into a set and simply
     * outputs the cast of a movie.
     *
     * @param inputKey Movie title
     * @param inputValues The list of actors
     */
    public void reduce(Text inputKey, Iterable<Text> inputValues, Context context)
            throws IOException, InterruptedException {

        //TODO
        // HINT: Use the method setStringCollection to set the output value.
    	this.list.clear();
    	
    	for (Text value:inputValues){
    		
    		if(!(this.list.contains(value.toString())))
    			this.list.add(value.toString());
    	}
    	
    	Collections.sort(list);
    	
    	this.outputValue.setStringCollection(this.list);
    	
    	context.write(inputKey,this.outputValue);

    }

}
