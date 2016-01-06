package ix.lab07.lsi;

import ix.lab07.utils.ConceptsResults;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import com.google.common.collect.Iterables;
import com.google.common.collect.MinMaxPriorityQueue;

/**
 * Explores the concepts represented by the first few dimensions of the latent
 * space uncovered by the SVD. The concepts are examined by looking at the terms which
 * have extremal values along a particular dimension.
 *
 * Implemented as a MapReduce job.
 */
public class Concepts extends Configured implements Tool {

    private static final String SEPARATOR = ":";
    public static final int NB_CONCEPTS = 5;
    public static final int NB_TERMS = 50;


    /**
     * The Mapper sends, for each term, the values along the first few dimensions. The key
     * is the dimension, and the value is a pair (term, component).
     *
     * Input: {key=<term>, val=Vector(<comp1>, <comp2>, ...)}
     * Output: {key=<dim>, val=<term>:<comp[dim]>} for dim in 0...NB_CONCEPTS-1
     */
    public static class ConceptsMapper extends Mapper<Text, VectorWritable, IntWritable, Text> {

        private IntWritable outputKey = new IntWritable();
        private Text outputValue = new Text();

        @Override
        public void map(Text inputKey, VectorWritable inputValue, Context context)
                throws IOException, InterruptedException {
            Vector vec = inputValue.get();
            String term = inputKey.toString();

            for (int i = 0; i < NB_CONCEPTS; ++i) {
                this.outputKey.set(i);
                this.outputValue.set(term + SEPARATOR + vec.get(i));
                context.write(this.outputKey, this.outputValue);
            }
        }
    }


    /**
     * The Reducer inspects all the values it receives and keeps the NB_TERMS highest
     * and lowest ones.
     *
     * Input: {key=<dim>, vals=[<term1>:<comp1[dim]>, <term2>:<comp2[dim]>, ...]}
     * Output: {key=<dim>, val=<term1>:<comp1[dim]>} for the highest and lowest values
     */
    public static class ConceptsReducer extends Reducer<IntWritable, Text, IntWritable, Text> {

        private static final Comparator<Pair<String, Double>> VALUE_COMPARATOR =
                new Comparator<Pair<String, Double>>() {
            @Override
            public int compare(Pair<String, Double> fst, Pair<String, Double> snd) {
                return fst.getSecond().compareTo(snd.getSecond());
            }
        };

        private Text outputValue = new Text();

        @Override
        public void reduce(IntWritable inputKey, Iterable<Text> inputValues, Context context)
                throws IOException, InterruptedException {

            // Terms with the lowest values.
            MinMaxPriorityQueue<Pair<String, Double>> lowest = MinMaxPriorityQueue
                    .orderedBy(VALUE_COMPARATOR)
                    .maximumSize(NB_TERMS).create();

            // Terms with the highest values.
            MinMaxPriorityQueue<Pair<String, Double>> highest = MinMaxPriorityQueue
                    .orderedBy(Collections.reverseOrder(VALUE_COMPARATOR))
                    .maximumSize(NB_TERMS).create();

            for (Text value : inputValues) {
                String[] parts = value.toString().split(SEPARATOR);
                double val = Double.parseDouble(parts[1]);
                Pair<String, Double> pair = new Pair<String, Double>(parts[0], val);

                lowest.add(pair);
                highest.add(pair);
            }

            for (Pair<String, Double> pair : Iterables.concat(lowest, highest)) {
                this.outputValue.set(pair.getFirst() + SEPARATOR + pair.getSecond());
                context.write(inputKey, this.outputValue);
            }
        }
    }


    public static Job getJob(Configuration conf, String input, String output) throws IOException {
        // Create the Hadoop job.
        Job job = new Job(conf, "Identify Concepts");
        job.setJarByClass(Concepts.class);
        job.setInputFormatClass(SequenceFileInputFormat.class);

        // Configure the mapper & reducer.
        job.setMapperClass(ConceptsMapper.class);
        job.setReducerClass(ConceptsReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        // Define input and output folders.
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }


    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format(
                    "Usage: %s <path to U> <output path>", this.getClass().getName()));
            return 0;
        }
        Job job = Concepts.getJob(this.getConf(), args[0], args[1]);
        job.waitForCompletion(true);

        // Display the results.
        ConceptsResults.parse(args[1]).display();
        return 0;
    }


    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Concepts(), args);
        System.exit(exitCode);
    }



}
