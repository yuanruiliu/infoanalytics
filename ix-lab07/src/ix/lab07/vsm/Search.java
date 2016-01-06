package ix.lab07.vsm;

import ix.lab07.utils.DocumentTokenization;
import ix.lab07.utils.SearchResults;
import ix.lab07.utils.TermDocumentPair;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
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
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.io.CharStreams;

/**
 * Given a query string, computes the score of each document across the corpus using
 * the vector space model with a cosine similarity measure.
 */
public class Search extends Configured implements Tool  {

    public static final String SEPARATOR = ":";
    private static final int NB_DOCS = 20;


    /**
     * The Mapper reads term-document pairs and their associated TF-IDF weight, and
     * reformats it into a key-value pair matching the document to the term and its
     * weight
     *
     * Input: {key=(<term>,<docID>), val=<weight>}
     * Output: {key=<docID>, val=<term>:<weight>}
     */
    public static class SearchMapper extends Mapper<Text, Text, IntWritable, Text> {

        private IntWritable outputKey = new IntWritable();
        private Text outputValue = new Text();

        @Override
        public void map(Text inputKey, Text inputValue, Context context)
                throws IOException, InterruptedException {
            TermDocumentPair pair = TermDocumentPair.fromText(inputKey);
            double weight = Double.parseDouble(inputValue.toString());

            this.outputKey.set(pair.getDocument());
            this.outputValue.set(Joiner.on(SEPARATOR).join(pair.getTerm(), weight));
            context.write(this.outputKey, this.outputValue);
        }
    }


    /**
     * The Reducer computes a (simplified) cosine similarity between the document
     * (given as a collection of terms) and the query. The query is always
     * available in each reducer, as a Map<String, Double> between terms and weights.
     *
     * Input: {key=<docID>, vals=[<term1>:<weight1>, <term2>:<weight2>, ...]}
     * Output: {key=<docID>, val=<score>}
     */
    @SuppressWarnings("unused")
    public static class SearchReducer extends Reducer<IntWritable, Text, IntWritable, DoubleWritable> {

        private DoubleWritable outputValue = new DoubleWritable();

        /**
         * This is the query vector. It is populated with the *real* query
         * when the Reducer is setup.
         * */
        private Map<String, Double> query = new HashMap<String, Double>();

        @Override
        public void reduce(IntWritable inputKey, Iterable<Text> inputValues, Context context)
                throws IOException, InterruptedException {
            /* NOTE: to compute the score, it is necessary to normalize the dot
             * product between document and query by the L2 norm of the document vector.
             *
             * However, you don't need to consider the norm of the query vector (as it
             * will just be the same constant factor for each score).
             */
            
            //TODO
        	double val1=0;
            for (Text value : inputValues) {
                String[] parts = value.toString().split(SEPARATOR);
                String word = parts[0];
                double weight = Double.parseDouble(parts[1]);

                val1+=Math.pow(weight, 2);
                
                for(Map.Entry<String, Double> entry: query.entrySet()){
                	
                }
            }

            // ...
        }

        @Override
        public void setup(Context context) {
            String tokens = context.getConfiguration().get("queryTokens");
            if (tokens != null) {
                this.query = getTokenMap(Splitter.on(SEPARATOR).split(tokens));
            }
        }
    }


    public static Job getJob(Configuration conf, String input, String output, String query) throws IOException {
        // Put the query in the configuration. Needs to be set before calling Job constructor.
        String tokens = Joiner.on(SEPARATOR).join(DocumentTokenization.stream(query));
        conf.set("queryTokens", tokens);

        // Create the Hadoop job.
        Job job = new Job(conf, "Query the document corpus");
        job.setJarByClass(Search.class);
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        // Configure the mapper.
        job.setMapperClass(SearchMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);

        // Configure the reducer.
        job.setReducerClass(SearchReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(DoubleWritable.class);

        // Define input and output folders.
        FileInputFormat.addInputPath(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        return job;
    }


    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.println(String.format(
                    "Usage: %s <wordweights path> <output path> <query>", this.getClass().getName()));
            return 0;
        }
        String query = getQuery(args[2]);
        Job job = getJob(this.getConf(), args[0], args[1], query);
        job.waitForCompletion(true);

        // Display the results.
        System.out.println(String.format("Results for query '%s':", args[2]));
        SearchResults.parse(args[1]).displayTopK(NB_DOCS);
        return 0;
    }


    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Search(), args);
        System.exit(exitCode);
    }

    public static String getQuery(String arg) throws IOException {
        if (!arg.equals("-")) {
            return arg;
        }
        // Read from stdin.
        return CharStreams.toString(new InputStreamReader(System.in));
    }

    public static Map<String, Double> getTokenMap(Iterable<String> tokens) {
        Map<String, Double> tokenMap = new HashMap<String, Double>();
        for (String token : tokens) {
            if (tokenMap.containsKey(token)) {
                tokenMap.put(token, tokenMap.get(token) + 1.0);
            } else {
                tokenMap.put(token, 1.0);
            }
        }
        return tokenMap;
    }
}
