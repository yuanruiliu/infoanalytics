package ix.lab07.vsm;

import ix.lab07.utils.DocumentTokenization;
import ix.lab07.utils.TermDocumentPair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
import org.apache.hadoop.thirdparty.guava.common.base.Joiner;

/**
 * Sums the number of occurrences of all the terms of a document, i.e.
 * computes the document length. Appends this length to each
 * term-document pair.
 */
public class DocumentLength {

    private static final String SEPARATOR = ":";


    /**
     * The Mapper parses a line containing the count for a term-document pair,
     * and reformats it into a key-value pair matching the document to the term
     * and its count.
     *
     * The idea is that the Reducer will then have a collection of all terms (and
     * their counts) for a given document..
     *
     * Input: {key=(<term>,<docID>), val=<count>}
     * Output: {key=<docID>, val=<term>:<count>}
     */
    @SuppressWarnings("unused")
    public static class DocumentLengthMapper extends Mapper<Text, Text, IntWritable, Text> {

        private IntWritable outputKey = new IntWritable();
        private Text outputValue = new Text();

        @Override
        public void map(Text inputKey, Text inputValue, Context context)
                throws IOException, InterruptedException {
            TermDocumentPair pair = TermDocumentPair.fromText(inputKey);
            int count = Integer.parseInt(inputValue.toString());
            
            //TODO
            outputKey.set(pair.getDocument());
            Text text = new Text(pair.getTerm()+":"+count);
            outputValue.set(text);
            context.write(outputKey, outputValue);
        }
    }


    /**
     * The Reducer iterates over all the terms in a document and sums up the
     * number of occurrences to obtain the total document length. It then iterates
     * a second time over the data and outputs key-value pairs in their original
     * format (pre-Mapper), but this time appending the document length to the
     * value.
     *
     * Input: {key=<docID>, vals=[<term1>:<count1>, <term2>:<count2>, ...]}
     * Output: {key=(<term>,<docID>), val=<count>:<docLength>} for each term
     */
    public static class DocumentLengthReducer
            extends Reducer<IntWritable, Text, TermDocumentPair, Text> {

        private TermDocumentPair outputKey = new TermDocumentPair();
        private Text outputValue = new Text();

        @Override
        public void reduce(IntWritable inputKey, Iterable<Text> inputValues, Context context)
                throws IOException, InterruptedException {
            int docID = inputKey.get();
            Map<String, Integer> wordCount = new HashMap<String, Integer>();

            int length = 0;
            for (Text value : inputValues) {
                String[] parts = value.toString().split(SEPARATOR);
                String word = parts[0];
                int count = Integer.parseInt(parts[1]);
                wordCount.put(word, count);
                length += count;
            }

            // Second pass over the data - simply append the sum.
            for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                this.outputKey.set(entry.getKey(), docID);

                String val = Joiner.on(SEPARATOR).join(entry.getValue(), length);
                this.outputValue.set(val);
                context.write(this.outputKey, this.outputValue);
            }
        }
    }


    public static Job getJob(Configuration conf, Path input, Path output) throws IOException {
        Job job = new Job(conf, "Count words in documents");
        job.setJarByClass(DocumentLength.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        job.setMapperClass(DocumentLengthMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(DocumentLengthReducer.class);
        job.setOutputKeyClass(TermDocumentPair.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);

        return job;
    }
}
