package ix.lab03.extraction;

import java.io.IOException;

import ix.utils.TextArrayOutputFormat;
import ix.utils.TextArrayWritable;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/** This job outputs the cast of each movie. */
public class MovieCast {

    public static Job getJob(String inputPath, String outputPath) throws IOException {

        // Create a Hadoop job.
        Job job = new Job();
        job.setJarByClass(MovieCast.class);
        job.setJobName("Movie cast");

        // Set mapper / reducer classes.
        job.setMapperClass(MovieCastMapper.class);
        job.setReducerClass(MovieCastReducer.class);

        // Input / output settings.
        job.setOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputValueClass(TextArrayWritable.class);
        job.setOutputFormatClass(TextArrayOutputFormat.class);

        // Define input and output folders.
        FileInputFormat.addInputPath(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        return job;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <input path> <output path>",
                    MovieCast.class.getName()));
            System.exit(-1);
        }

        Job job = MovieCast.getJob(args[0], args[1]);
        job.waitForCompletion(true);
    }

}
