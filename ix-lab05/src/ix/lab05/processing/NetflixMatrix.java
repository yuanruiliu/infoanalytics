package ix.lab05.processing;

import java.io.IOException;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.math.VectorWritable;

/**
 * This job creates a compact matrix representation of the ratings. The output is a
 * sequence file, where the key is the user ID, and the value is a (sparse vector)
 * containing the rating for each movie.
 */
public class NetflixMatrix extends Configured implements Tool {

    public static final String TAB = "\t";

    public Job getJob(String input, String output) throws IOException {
        // Create the Hadoop job.
        Job job = new Job(getConf());
        job.setJarByClass(NetflixMatrix.class);
        job.setJobName("Netflix dataset matrix");

        // Configure the mapper.
        job.setMapperClass(NetflixMatrixMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        // Configure the reducer.
        job.setReducerClass(NetflixMatrixReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(VectorWritable.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);

        // Define input and output folders.
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new NetflixMatrix(), args);
        System.exit(res);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format(
                    "Usage: %s <input path> <output path>", getClass().getName()));
            return 0;
        }

        Job job = this.getJob(args[0], args[1]);
        job.waitForCompletion(true);
        return 0;
    }


}
