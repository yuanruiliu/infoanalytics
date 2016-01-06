package ix.lab07.vsm;

import ix.lab07.utils.DocumentTokenization;
import ix.lab07.utils.TermDocumentPair;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Extracts the terms from the IMDB movie plots dataset and counts the
 * number of occurrences of each term in each document (= movie).
 */
public class WordCount {


    /**
     * The Mapper parses a line from the movie plots dataset and outputs
     * (<term>,<docID>) pairs for each of the terms on the line.
     *
     * Input: {key=<docID>, val=<line>}
     * Output: {key=(<term>,<docID>), val=1} for each term on the line.
     */
    public static class WordCountMapper extends Mapper<Text, Text, TermDocumentPair, IntWritable> {

        private static final IntWritable ONE = new IntWritable(1);

        // Holds a pair (<term>,<docID>).
        private TermDocumentPair outputKey = new TermDocumentPair();

        @Override
        public void map(Text inputKey, Text inputValue, Context context)
                throws IOException, InterruptedException {
            int docID = Integer.parseInt(inputKey.toString());
            String line = inputValue.toString();

            for (String token : DocumentTokenization.stream(line)) {
                outputKey.set(token, docID);
                context.write(outputKey, ONE);
            }
        }
    }


    /**
     * The Reducer simply aggregates the (<term>,<docID>) pairs by summing their
     * count.
     *
     * Input: {key=(<term>,<docID>), vals=[<count1>, <count2>, ...]}
     * Output: {key=(<term>,<docID>), val=sum(<count1>, <count2>, ...)}
     */
    @SuppressWarnings("unused")
    public static class WordCountReducer
            extends Reducer<TermDocumentPair, IntWritable, TermDocumentPair, IntWritable> {

        private IntWritable outputValue = new IntWritable();

        @Override
        public void reduce(TermDocumentPair inputKey, Iterable<IntWritable> inputValues, Context context)
                throws IOException, InterruptedException {
            
            //TODO
        	int sum = 0;
    		for (IntWritable value: inputValues) {
    			sum += value.get();
    		}
    		
    		this.outputValue.set(sum);
    		
    		context.write(inputKey, this.outputValue);
            
        }
    }


    public static Job getJob(Configuration conf, Path input, Path output) throws IOException {
        Job job = new Job(conf, "Count frequency of words");
        job.setJarByClass(WordCount.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        job.setMapperClass(WordCountMapper.class);
        job.setCombinerClass(WordCountReducer.class);
        job.setReducerClass(WordCountReducer.class);
        job.setOutputKeyClass(TermDocumentPair.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);

        return job;
    }

}
