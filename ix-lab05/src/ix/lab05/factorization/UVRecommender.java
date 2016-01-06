package ix.lab05.factorization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.Vector;

import ix.utils.LabUtils;

/**
 * Implementation of a recommender system based on the output of a UV
 * factorization of the ratings matrix.
 */
public class UVRecommender {

    /** Number of movies to recommend. */
    private static final int NB_ITEMS = 20;

    private Path dir;
    private Configuration conf;

    protected Map<Integer, Vector> userCache;
    protected Map<Integer, Vector> itemFeatures;


    /** For testing purposes. */
    protected UVRecommender() { }


    /**
     * Initializes the instance with the output of the UV factorization. It
     * expects the layout and format outputted by Mahout's parallelALS.
     *
     * @param dir path to the UV factorization output
     */
    public UVRecommender(String dir) {
        this.dir = new Path(dir);
        this.conf = new Configuration();
        this.userCache = new HashMap<Integer, Vector>();

        this.itemFeatures = LabUtils.readMatrixByRows(
                new Path(this.dir, "M"), this.conf);
    }


    /**
     * Returns the features associated to a user.
     *
     * @param userID user for which to return the features.
     * @return a Vector containing the features
     */
    private Vector userFeatures(int userID) {
        if (!this.userCache.containsKey(userID)) {
            this.userCache.put(userID, LabUtils.readUserRow(
                    userID, new Path(this.dir, "U"), this.conf));
        }
        return this.userCache.get(userID);
    }


    /**
     * Predict the ratings of *all* the movies for a particular user.
     *
     * @param userID the user for which to compute the predicted ratings.
     * @return a mapping from movie ID to predicted rating.
     */
    public Map<Integer, Double> predictRatings(int userID) {
        Vector userFeats = userFeatures(userID);

        Map<Integer, Double> ratings = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, Vector> entry : this.itemFeatures.entrySet()) {
            ratings.put(entry.getKey(),
                    this.predictRating(userFeats, entry.getValue()));
        }
        return ratings;
    }


    /**
     * Convenience wrapper that fetches the vectors associated to the user
     * and the movie.
     */
    public double predictRating(int userID, int movieID) {
        Vector userFeats = this.userFeatures(userID);
        return this.predictRating(userFeats, this.itemFeatures.get(movieID));
    }


    /**
     * Predict a rating given the features of the user and those of the item.
     *
     * @param userFeats representation of the user in the feature space
     * @param itemFeatures representation of the item in the feature space
     * @return the predicted rating
     */
    private double predictRating(Vector userFeats, Vector itemFeatures) {
    	//TODO
    	return userFeats.dot(itemFeatures);
        
    }
    

    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println(String.format(
                    "Usage: %s userID /path/to/factorization /path/to/movie_titles.txt",
                    UVRecommender.class.getName()));
            return;
        }
        int userID = Integer.parseInt(args[0]);
        String dir = args[1];
        String titlesPath = args[2];

        UVRecommender rec = new UVRecommender(dir);
        Map<Integer, Double> ratings = rec.predictRatings(userID);
        
        List<Pair<Integer, Double>> top = LabUtils.topK(NB_ITEMS, ratings);
        LabUtils.printRatings(top, titlesPath);
    }

}
