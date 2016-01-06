package ix.lab05.similarity;

import ix.lab05.processing.RMSE;

import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;


/**
 * Compute the RMSE performance of the recommender system with respect
 * to a particular user.
 */
public final class Evaluator {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println(String.format(
                    "Usage: %s /path/to/similarity/results /path/to/groundtruth", Evaluator.class.getName()));
            return;
        }
        String resultsPath = args[0];
        String groundTruthPath = args[1];
        Configuration conf = new Configuration();

		Map<Integer, Double> recommendations = Recommender.parseResults(resultsPath, conf); 
        
        double error = RMSE.evaluate(groundTruthPath, recommendations);
        System.out.println(String.format("RMSE for user %d: %f", RMSE.USER_ID, error));
    }

}
