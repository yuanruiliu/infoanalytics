package ix.lab05.factorization;

import ix.lab05.processing.RMSE;

import java.io.FileNotFoundException;
import java.util.Map;


/**
 * Compute the RMSE performance of the recommender system with respect
 * to a particular user.
 */
public final class Evaluator {

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            System.err.println(String.format(
                    "Usage: %s /path/to/factorization /path/to/groundtruth", RMSE.class.getName()));
            return;
        }
        String factorizationDir = args[0];
        String groundTruthPath = args[1];

        UVRecommender rec = new UVRecommender(factorizationDir);
        Map<Integer, Double> recommendations = rec.predictRatings(RMSE.USER_ID);
        
        double error = RMSE.evaluate(groundTruthPath, recommendations);
        System.out.println(String.format("RMSE for user %d: %f", RMSE.USER_ID, error));
    }

}
