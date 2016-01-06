package ix.lab05.similarity;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.mahout.cf.taste.impl.common.Cache;
import org.apache.mahout.cf.taste.impl.common.Retriever;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;

import ix.utils.UserContribution;
import ix.utils.VectorUtils;

/**
 * The mapper takes one user of the dataset, computes his similarity to the user
 * for which we are making a recommendation, and outputs its contribution
 * (similarity, ratings).
 *
 * Input:
 *
 * user2	{movie1: 5.0, movie2: 3.0, movie3: 1.0}
 *
 * Output:
 *
 * [similarity: 0.8945, ratings: {movie1: 5.0, movie2: 3.0, movie3: 1.0}]
 *
 * Note that we do not have an output key, because all outputs need to be
 * processed by the same reducer, and thus use a NullWritable.
 */
@SuppressWarnings("unused")
public class RecommenderMapper extends
        Mapper<IntWritable, VectorWritable, NullWritable, UserContribution> {

    /**
     * Id of the user for which we are recommending movies. (loaded once during
     * the setup)
     */
    private int myId;

    /**
     * Ratings of the user for which we are recommending movies. (loaded once
     * during the setup)
     */
	private Vector myRatings;

    // we do not need output keys, all outputs should be processed by the same
    // reducer
    private final NullWritable nullKey = NullWritable.get();
    private UserContribution outputValue = new UserContribution();

    /**
     * Map operation: computes the similarity of the given user to the one we
     * recommend to, and outputs its with the ratings of given user.
     *
     * @param otherId
     *            The id of the user for which we compute the contribution
     * @param otherRatings
     *            The ratings of the user for which we compute the contribution
     * @param context
     *            The context of the job.
     */
    @Override
    public void map(IntWritable otherId, VectorWritable otherRatings,
            Context context) throws IOException, InterruptedException {

        // do not recommend a movie to yourself
        if (otherId.get() == this.myId) {
            return;
        }

        //TODO
        
        Vector userVector = otherRatings.get();
      
        double similarity = VectorUtils.cosineSimilarity(myRatings, userVector);
        
        outputValue.set(similarity,userVector);
        
        context.write(this.nullKey, this.outputValue);
    }


    /**
     * Loads the id and the ratings of the user for which we are making a
     * recommendation from the job configuration, and stores them to members of
     * the class.
     */
    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        Configuration conf = context.getConfiguration();

        // read our id
        this.myId = conf.getInt("userId", 0);

        // get our serialized ratings
        String userRatingsSerialized = conf.get("userRatings");
        this.myRatings = VectorUtils.deserialize(userRatingsSerialized);
    }
}
