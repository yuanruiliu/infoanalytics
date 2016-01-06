package ix.lab05.similarity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.thirdparty.guava.common.collect.Iterables;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;

import ix.utils.LabUtils;
import ix.utils.UserContribution;
import ix.utils.VectorUtils;

public class Recommender extends Configured implements Tool {

    /** Number of movies to recommend. */
    private static final int NB_ITEMS = 20;

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Recommender(), args);
        System.exit(exitCode);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 4) {
            String errorMessage = String
                    .format("Usage: %s <netflix matrix> <output path> <path to movie titles> <user id>",
                            getClass().getName());
            System.err.println(errorMessage);
            System.exit(-1);
        }

        String inputPath = args[0];
        String outputPath = args[1];
        String movieTitlesFile = args[2];
        int userId = Integer.parseInt(args[3]);

        // First, run Hadoop job to get the recommendation for the user
        Job job = this.getJob(inputPath, outputPath, userId);
        job.waitForCompletion(true);

        // then, parse results and get the movies with the best rating
        Map<Integer, Double> ratings = parseResults(outputPath, getConf());
        List<Pair<Integer, Double>> topRatings = LabUtils.topK(NB_ITEMS, ratings);

        // finally, print the recommendations
        LabUtils.printRatings(topRatings, movieTitlesFile);

        return 0;
    }

    public Job getJob(String input, String output, int user)
            throws IOException, URISyntaxException {
        Configuration conf = getConf();
        conf.setInt("userId", user);

        String userRatings = getUserRatings(user, conf, input);
        conf.set("userRatings", userRatings);

        Job job = new Job(conf, "Memory-based recommender");
        job.setJarByClass(getClass());
        
        job.setNumReduceTasks(1);

        job.setMapperClass(RecommenderMapper.class);
        job.setReducerClass(RecommenderReducer.class);

        job.setInputFormatClass(SequenceFileInputFormat.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(UserContribution.class);
        job.setOutputValueClass(VectorWritable.class);

        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }

    /**
     * Gets the ratings of the user we want to recommend movies to, in order to
     * pass the to the mappers through the configuration.
     *
     * @param userId ID of the user for which we want recommendations
     * @param conf Job configuration
     * @param inputPath Path to the input files
     * @return Serialized ratings for the current user
     * @throws IOException
     */
    private String getUserRatings(int userId, Configuration conf,
            String inputPath) throws IOException {
        Path filePath = new Path(inputPath);

        Vector ratings = LabUtils.readUserRow(userId, filePath, conf);
        return VectorUtils.serialize(ratings);
    }

    /**
     * Parses the resulting recommendations from the output of the reducer.
     *
     * @param outputDir Directory containing the output of the Hadoop job.
     * @return
     * @throws IOException
     */
    public static Map<Integer, Double> parseResults(String outputDir, Configuration conf)
            throws IOException {
        Path path = new Path(outputDir);

        Pair<IntWritable, VectorWritable> result = Iterables.getOnlyElement(LabUtils.readSequence(path, conf));
        Vector ratingsVector = result.getSecond().get();

        Map<Integer, Double> ratings = new HashMap<Integer, Double>();
        for (Element el: ratingsVector) {
            ratings.put(el.index(), el.get());
        }

        return ratings;
    }

}
