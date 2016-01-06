package ix.lab07.lsi;

import ix.lab07.utils.Constants;
import ix.lab07.utils.TermDocumentPair;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;

import com.google.common.base.Joiner;

/**
 * Builds the term-document matrix as a sequence file mapping terms
 * to vectors of documents.
 *
 * NOTE: Why do we not do it the other way around, i.e. mapping the documents
 * to vectors of words? It's a technicality linked to the fact that
 * documents are enumerated (i.e. indexed by an integer) while terms aren't.
 */
public class TermDocumentMatrix extends Configured implements Tool {

    private static final String SEPARATOR = ":";


    /**
     * The Mapper parses a line containing the weight for a term-document pair,
     * and reformats it into a key-value pair matching the term to the document
     * and the term's weight.
     *
     * Input: {key=(<term>,<docID>), val=<weight>}
     * Output: {key=<term>, val=<docID>:<weight>}
     */
    public static class TermDocumentMatrixMapper extends Mapper<Text, Text, Text, Text> {

        private Text outputKey = new Text();
        private Text outputValue = new Text();

        @Override
        public void map(Text inputKey, Text inputValue, Context context)
                throws IOException, InterruptedException {
            TermDocumentPair pair = TermDocumentPair.fromText(inputKey);
            double weight = Double.parseDouble(inputValue.toString());

            this.outputKey.set(pair.getTerm());
            this.outputValue.set(Joiner.on(SEPARATOR).join(pair.getDocument(), weight));
            context.write(this.outputKey, this.outputValue);
        }
    }


    /**
     * The Reducer simply creates a (sparse) vector out of the collection of
     * (docID, weight) pairs for a given vector.
     *
     * Input: {key=<term>, vals=[<docID1>:<weight1>, <docID2>:<weight2>, ...]}
     * Output: {key=<term>, val=Vector(<docID1> => <weight1>, ...)}
     */
    public static class TermDocumentMatrixReducer extends Reducer<Text, Text, Text, VectorWritable> {

        private VectorWritable outputValue = new VectorWritable();

        @Override
        public void reduce(Text inputKey, Iterable<Text> inputValues, Context context)
                throws IOException, InterruptedException {
            Vector vector = new SequentialAccessSparseVector(Constants.NB_IMDB_MOVIES, 1);

            for (Text value : inputValues) {
                String[] parts = value.toString().split(SEPARATOR);
                int docID = Integer.parseInt(parts[0]);
                double weight = Double.parseDouble(parts[1]);
                vector.set(docID, weight);
            }

            this.outputValue.set(vector);
            context.write(inputKey, outputValue);
        }
    }


    public static Job getJob(Configuration conf, String input, String output) throws IOException {
        // Create the Hadoop job.
        Job job = new Job(conf, "Term-document matrix as a sequence file");
        job.setJarByClass(TermDocumentMatrix.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        // Configure the mapper.
        job.setMapperClass(TermDocumentMatrixMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        // Configure the reducer.
        job.setReducerClass(TermDocumentMatrixReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(VectorWritable.class);
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK);

        // Define input and output folders.
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }


    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format(
                    "Usage: %s <input path> <output path>", this.getClass().getName()));
            return 0;
        }

        Job job = TermDocumentMatrix.getJob(this.getConf(), args[0], args[1]);
        job.waitForCompletion(true);
        return 0;
    }


    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new TermDocumentMatrix(), args);
        System.exit(exitCode);
    }
}
