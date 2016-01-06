package ix.lab01.wordcount;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * This job counts the number of occurrences of each word in the given input file.
 */
public class WordCount {

	public static void main(String[] args) throws Exception {
		// check if both input file and output folder were given as arguments
		if (args.length != 2) {
			System.err.println("Usage: ix.lab01.wordcount.WordCount <input path> <output path>");
			System.exit(-1);
		}
		
		// create Hadoop job
		Job job = new Job();
		job.setJarByClass(WordCount.class);
		job.setJobName("Word count");

		// set mapper/reducer classes
		job.setMapperClass(WordCountMapper.class);
		job.setReducerClass(WordCountReducer.class);
		job.setCombinerClass(WordCountReducer.class);
		
		// define output types: here, output will be "<word>	<nbOccurrences>"
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		// define input and output folders
	    FileInputFormat.addInputPath(job, new Path(args[0]));
	    FileOutputFormat.setOutputPath(job, new Path(args[1]));

	    // launch job with verbose output and wait until it finishes
		job.waitForCompletion(true);
	}

}
