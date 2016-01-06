package ix.lab01.wordcount;

import ix.utils.Tweet;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * Map operation of the WordCount Hadoop job: for each line of the input file,
 * we separate each word, and output one key/value for each of them.
 * 
 * Example:
 * 
 * Input:
 * hello world hadoop world
 * 
 * Output:
 * 	hello	1
 *  world	1
 *  hadoop	1
 *  world	1
 * 
 */
public class WordCountMapper extends Mapper<LongWritable, // input key: line offset
											Text, // input value: line content
											Text, // output key: word
											IntWritable> { // output value: 1

	// Output keys will be words 
	private Text outputKey = new Text();

	// As all values emitted will be 1, we define it once and for all
	private final IntWritable outputValue = new IntWritable(1);

	// To separate a line into words, we split on any of the following characters
	private final String separators = " ,;:\".\t\n\r\f'";
	
	// Tweet object that will be parsed from each line
	private Tweet tweet = new Tweet();
	
	/**
	 * Map operation: this function is called for each line of the input file.
	 * It splits the line into words, using a list of separators
	 * 
	 * @param inputKey Offset of the current line in the input file
	 * @param inputValue Content of the line
	 * @param context Job context, for writing output, getting configuration, etc.
	 */
	public void map(LongWritable inputKey, Text inputValue, Context context)
			throws IOException, InterruptedException {
		// we first get the line content
		String line = inputValue.toString();
		
		// skip comment lines (that start with a '#')
		if (line.charAt(0) == '#') {
			return;
		}

		// parse tweet from the input line
		this.tweet.parse(line);
		
		// separate line into words, splitting on spaces and special characters 
		StringTokenizer wordIterator = new StringTokenizer(this.tweet.text.toLowerCase(),
				this.separators);
		
		// iterate over words
		while (wordIterator.hasMoreTokens()) {
			// get next word
			String word = wordIterator.nextToken();
			
			// set word as output key 
			outputKey.set(word);
			
			// write output key/value to the context
			context.write(outputKey, outputValue);
		}
	}

}
