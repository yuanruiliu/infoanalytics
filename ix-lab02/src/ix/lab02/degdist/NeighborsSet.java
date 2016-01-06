package ix.lab02.degdist;

import java.io.IOException;

import ix.utils.TextArrayOutputFormat;
import ix.utils.TextArrayWritable;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * This job takes the Wikipedia graph and outputs the list of neighbors for each
 * article-node in the input file.
 */
public class NeighborsSet {

    public static Job getJob(String input, String output) throws IOException {
        // Create a Hadoop job.
        Job job = new Job();
        job.setJarByClass(NeighborsSet.class);
        job.setJobName("Article neighbors");

        // Set mapper / reducer classes.
        job.setMapperClass(NeighborsSetMapper.class);
        job.setReducerClass(NeighborsSetReducer.class);

        // define output types: here, output will be "<article>	<set neighbor articles>"
        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputValueClass(TextArrayWritable.class);
        job.setOutputFormatClass(TextArrayOutputFormat.class);

        // Define input and output folders.
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <input path> <output path>",
                    NeighborsSet.class.getName()));
            System.exit(-1);
        }

        Job job = NeighborsSet.getJob(args[0], args[1]);
        job.waitForCompletion(true);
    }
}
