package ix.lab03.extraction;

import java.io.IOException;

import ix.utils.TextArrayOutputFormat;
import ix.utils.TextArrayWritable;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * This job outputs the co-appearance list of actors.
 * - These co-appearances happened in the period [startingYear, endingYear]
 * - The input is a file with movie casts.
 */
public class CoActorGraph {

    public static Job getJob(String inputPath, String outputPath,
            int startingYear, int endingYear) throws IOException {

        // Create a Hadoop job.
        Job job = new Job();
        job.setJarByClass(CoActorGraph.class);
        job.setJobName("Co-actors graph");

        // Set mapper / reducer classes.
        job.setMapperClass(CoActorGraphMapper.class);
        job.setReducerClass(CoActorGraphReducer.class);
        job.setCombinerClass(CoActorGraphReducer.class);

        // Send parameters to all nodes in the cluster.
        job.getConfiguration().setInt("startingYear", startingYear);
        job.getConfiguration().setInt("endingYear", endingYear);

        // Input / output settings.
        job.setInputFormatClass(KeyValueTextInputFormat.class);
        job.setOutputFormatClass(TextArrayOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(TextArrayWritable.class);

        // Define input and output folders.
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        return job;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println(String.format("Usage: %s <input path> <output path> "
                    + "<starting year> <ending year>", CoActorGraph.class.getName()));
            System.exit(-1);
        }

        int start = Integer.parseInt(args[2]);
        int end = Integer.parseInt(args[3]);
        Job job = CoActorGraph.getJob(args[0], args[1], start, end);
        job.waitForCompletion(true);
    }

}
