package ix.lab07.vsm;

import ix.lab07.utils.TermDocumentPair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ix.lab07.vsm.TfIdf;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.thirdparty.guava.common.base.Joiner;

public class WordWeight {

    private static final String SEPARATOR = ":";


    /**
     * The Mapper parses a line containing the count and the length for a
     * term-document pair, and reformats it into a key-value pair matching the
     * term to its document, its count and the document length.
     *
     * Input: {key=(<term>,<docID>), val=<count>:<docLength>}
     * Output: {key=<term>, val=<docID>:<count>:<docLength>}
     */
    public static class WordWeightMapper extends Mapper<Text, Text, Text, Text> {

        private Text outputKey = new Text();
        private Text outputValue = new Text();

        @Override
        public void map(Text inputKey, Text inputValue, Context context)
                throws IOException, InterruptedException {
            TermDocumentPair pair = TermDocumentPair.fromText(inputKey);
            String[] parts = inputValue.toString().split(SEPARATOR);
            int count = Integer.parseInt(parts[0]);
            int docLength = Integer.parseInt(parts[1]);

            outputKey.set(pair.getTerm());
            String val = Joiner.on(SEPARATOR).join(pair.getDocument(), count, docLength);
            outputValue.set(val);
            context.write(outputKey, outputValue);
        }
    }


    /**
     * The Reducer first counts the number of documents containing a term, and
     * then outputs the TF-IDF weight for each term-document pair.
     *
     * It needs to do two passes over the input values: the first to simply count
     * the number of documents conaining the term, and the second to compute and
     * output the TF-IDF weight for each document.
     *
     * Input: {key=<term>, vals=[<docID1>:<count1>:<docLength1>, <docID2>:<count2>:<docLength2>, ...]}
     * Output: {key=(<term>,<docID>), val=<weight>} for each document
     */
    @SuppressWarnings("unused")
    public static class WordWeightReducer extends Reducer<Text, Text, TermDocumentPair, DoubleWritable> {

        private TermDocumentPair outputKey = new TermDocumentPair();
        private DoubleWritable outputValue = new DoubleWritable();

        @Override
        public void reduce(Text inputKey, Iterable<Text> inputValues, Context context)
                throws IOException, InterruptedException {
            
            // Note: you will need to iterate over inputValues twice. It is your job
            // to cache the values during the first iteration.
        	List<String> wordWeight = new ArrayList<String>();
            int nDoc = 0;
        	for (Text value: inputValues) {
    			
        		nDoc++;

        		wordWeight.add(inputKey.toString()+":"+value.toString());
    		}
        	for (String entry : wordWeight) {
    			String[] str = entry.split(SEPARATOR);
    			
    			double idf =  TfIdf.inverseDocFrequency(nDoc);
    			
    			double tf = TfIdf.termFrequency(Integer.parseInt(str[2]), Integer.parseInt(str[3]));
    			outputKey.set(str[0], Integer.parseInt(str[1]));
        		outputValue.set(tf*idf);
    			context.write(this.outputKey, this.outputValue);
    		}
        }
    }


    public static Job getJob(Configuration conf, Path input, Path output) throws IOException {
        Job job = new Job(conf, "Count frequency of words");
        job.setJarByClass(WordWeight.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        job.setMapperClass(WordWeightMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(WordWeightReducer.class);
        job.setOutputKeyClass(TermDocumentPair.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job, input);
        FileOutputFormat.setOutputPath(job, output);

        return job;
    }

}
