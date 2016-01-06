package ix.lab03.extraction;

import ix.utils.TextArrayWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

@SuppressWarnings("unused")
public class CoActorGraphMapper extends Mapper<Text, // input key: movie title
                                          Text, // input value: actors list
                                          Text, // output key: actor name
                                          TextArrayWritable> { // output value: list of his co-actors in this movie

    private static final Pattern YEAR_PATTERN = Pattern.compile(".+ \\((\\d{4})\\)");

    private Text outputKey = new Text();
    private TextArrayWritable outputValue = new TextArrayWritable();

    private int startingYear;
    private int endingYear;

    public void map(Text inputKey, Text inputValue, Context context)
            throws IOException, InterruptedException {

        // Starting year of the period of interest.
        this.startingYear = context.getConfiguration().getInt("startingYear", -1);
        // Ending year of the period of interest
        this.endingYear = context.getConfiguration().getInt("endingYear", -1);

        Matcher m = YEAR_PATTERN.matcher(inputKey.toString());
        if (!m.matches()) {
            throw new RuntimeException(String.format("Failed to process key ''", inputKey.toString()));
        }

        int movieYear = Integer.parseInt(m.group(1));
        
        //TODO Extract all pairs of actors that played in the movie, if the movie was in the years of interest
        if(movieYear<=this.endingYear&&movieYear>=this.startingYear){
        	String line = inputValue.toString();
        
        	List<String> list = new ArrayList<String>(Arrays.asList(line.split(", "))); 
        
        	for(int i=0;i<list.size();i++){	
        	
        		List<String> temp=null;
        	
        		this.outputKey.set(list.get(i));
          	
        		if(i==0)	temp=list.subList(1, list.size());
        		else if(i==list.size())	temp=list.subList(0, i-1);
        		else{
        			temp=list.subList(0, i-1);
          		
        			temp.addAll(list.subList(i+1, list.size()));
        		}
          	
        		this.outputValue.setStringCollection(temp);
        	
        		context.write(this.outputKey, this.outputValue);    	
        	}
        }
        
    }

}
