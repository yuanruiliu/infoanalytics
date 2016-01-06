package ix.lab03.extraction;

import ix.utils.TextArrayWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

@SuppressWarnings("unused")
public class CoActorGraphReducer extends Reducer<Text, // input key: actor name
                                              TextArrayWritable, // input values: list of co-actors in one movie
                                              Text, // output key: actor name
                                              TextArrayWritable> { // output value: list of co-actors for all movies

    private TextArrayWritable outputValue = new TextArrayWritable();
    private List<String> list = new ArrayList<String>();
    
    /**
     * The reduce operation simply creates a set with the co-actors of all movies.
     *
     * @param inputKey  the actor name
     * @param inputValues  an iterable of list of co-actors
     */
    public void reduce(Text inputKey, Iterable<TextArrayWritable> inputValues, Context context)
            throws IOException, InterruptedException {

        //TODO Assemble all co-actors
    	this.list.clear();
    	
    	for (TextArrayWritable value:inputValues){
    		
    		List<String> aList = new ArrayList<String>(Arrays.asList(value.toString().split(",")));
    		Iterator<String> iterator = aList.iterator();
    		while(iterator.hasNext()){
    			String temp = iterator.next();
    			if (Character.isWhitespace(temp.charAt(0))) 
    				  temp=temp.substring(1);
    			
    			if(!(this.list.contains(temp)))
    				this.list.add(temp);
    		}
    	}
    	
    	Collections.sort(list);
    	
    	this.outputValue.setStringCollection(this.list);
    	
    	context.write(inputKey,this.outputValue);

    }

}
