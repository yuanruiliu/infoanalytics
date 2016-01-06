package ix.lab07.vsm;

import ix.lab07.utils.Constants;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * Driver for the 3 MapReduce jobs that compute the TF-IDF weights for
 * each (term, document) pair in the IMDB movie plots dataset.
 *
 * This is what you will run once you've implemented the WordCount,
 * DocumentLength and WordWeight jobs.
 */
public class TfIdf extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format(
                    "Usage: %s <input path> <output path>", this.getClass().getName()));
            return 0;
        }

        Path input = new Path(args[0]);
        Path output = new Path(args[1]);

        // First job: count the frequency of each word in each document.
        Path firstOutput = new Path(output, "wordcount");
        Job firstJob = WordCount.getJob(this.getConf(), input, firstOutput);
        firstJob.waitForCompletion(true);

        // Second job: count the total number of words per document.
        Path secondOutput = new Path(output, "doclength");
        Job secondJob = DocumentLength.getJob(this.getConf(), firstOutput, secondOutput);
        secondJob.waitForCompletion(true);

        // Third job: count the document frequencies and compute the TF-IDF weights.
        Path thirdOutput = new Path(output, "wordweights");
        Job thirdJob = WordWeight.getJob(this.getConf(), secondOutput, thirdOutput);
        thirdJob.waitForCompletion(true);

        return 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new TfIdf(), args);
        System.exit(exitCode);
    }


    /**
     * Computes the TF weight for a term, given the number of occurrences
     * in the document and the document's total length.
     *
     * @param count the number of times the term appears in the document
     * @param docLength the total number of terms in the document
     * @return the TF weight for the term
     */
    public static double termFrequency(int count, int docLength) {
        // WARNING beware of integer divisions...
        
        //TODO
    	//term frequency= #occurrences of t in d/length of d
        return (double)count/(double)docLength;
    }


    /**
     * Computes the IDF weight for a term, given the number of document it appears
     * in. The total number of documents is hard coded, and needs not to be provided.
     *
     * @param docsWithWords the number of documents containing the word.
     * @return the IDF weight for the term
     */
    @SuppressWarnings("unused")
    public static double inverseDocFrequency(int docsWithWords) {
        // WARNING beware of integer divisions...
        int nbDocs = Constants.NB_IMDB_MOVIES;
        
        //TODO
        //inverse document frequency = log(total # of documents / # of documents containing t)
        
        return Math.log((double)Constants.NB_IMDB_MOVIES/(double)docsWithWords);
    }

}
