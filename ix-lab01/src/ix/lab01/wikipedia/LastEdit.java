package ix.lab01.wikipedia;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * This job takes the raw Wikipedia edits dataset as input, and extracts the
 * last edit of each article
 * 
 */
public class LastEdit {

	public static Job getJob(String input, String output) throws IOException {
		Job job = new Job();
		job.setJarByClass(LastEdit.class);
		job.setJobName("Wikipedia last edit extraction");

		job.setMapperClass(LastEditMapper.class);
		job.setReducerClass(LastEditReducer.class);
		job.setCombinerClass(LastEditReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		return job;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: ix.lab01.wikipedia.LastEdit <input path> <output path>");
			System.exit(-1);
		}

		Job job = LastEdit.getJob(args[0], args[1]);

		job.waitForCompletion(true);
	}

}
