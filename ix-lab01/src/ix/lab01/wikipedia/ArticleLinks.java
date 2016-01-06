package ix.lab01.wikipedia;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * This job takes the output of the LastEdit job to extract all links between
 * articles.
 * 
 */
public class ArticleLinks {

	public static Job getJob(String input, String output) throws IOException {
		Job job = new Job();
		job.setJarByClass(ArticleLinks.class);
		job.setJobName("Wikipedia article links extraction");

		job.setMapperClass(ArticleLinksMapper.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);

		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		return job;
	}

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: ix.lab01.wikipedia.ArticleLinks <input path> <output path>");
			System.exit(-1);
		}

		Job job = ArticleLinks.getJob(args[0], args[1]);

		job.waitForCompletion(true);
	}

}
