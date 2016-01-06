package ix.lab01.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reduce operation of the WordCount job. The reducer gathers all values that were emitted
 * with the same key, i.e. separate counts from each mapper.
 * 
 * It simply sums all the individual counts to get the total number of ocurrences for
 * this word.
 * 
 * Example:
 * 
 * Input:
 * hello	1,1,2
 * 
 * Output:
 * hello	4
 *
 */
public class WordCountReducer extends Reducer<Text, // input key: the word
											  IntWritable, // input values: counts from each mapper
											  Text, // output key: the word
											  IntWritable> { // output value: the sum of counts
	
	private IntWritable outputValue = new IntWritable();

	/**
	 * The reduce operation simply sums all individual counts.
	 * 
	 * @param inputKey The word
	 * @param inputValues The list of counts
	 */
	public void reduce(Text inputKey, Iterable<IntWritable> inputValues, Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		for (IntWritable value: inputValues) {
			sum += value.get();
		}
		
		this.outputValue.set(sum);
		
		context.write(inputKey, this.outputValue);
	}

}
