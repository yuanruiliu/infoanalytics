package ix.lab02.degdist;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * This job takes the list of linked articles for each article as input,
 * and computes the degree distribution.
 */
public class DegreeDistribution {

    public static Job getJob(String input, String output) throws IOException {

        // Create a Hadoop job.
        Job job = new Job();
        job.setJarByClass(DegreeDistribution.class);
        job.setJobName("Wikipedia degree distribution");

        // Read the input file as key / value pairs (key is the article name,
        // value is the concatenated list of article names.)
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        // Set mapper / reducer classes.
        job.setMapperClass(DegreeDistributionMapper.class);
        job.setReducerClass(DegreeDistributionReducer.class);
        job.setCombinerClass(DegreeDistributionReducer.class);

        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <input path> <output path>",
                    DegreeDistribution.class.getName()));
            System.exit(-1);
        }

        Job job = DegreeDistribution.getJob(args[0], args[1]);
        job.waitForCompletion(true);
    }

}
