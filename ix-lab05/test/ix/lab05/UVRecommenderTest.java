package ix.lab05;

import static org.junit.Assert.assertEquals;
import ix.lab05.factorization.UVRecommender;

import java.util.HashMap;
import java.util.Map;

import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;

public class UVRecommenderTest {

    public static final Map<Integer, Vector> USERS = new HashMap<Integer, Vector>();
    public static final Map<Integer, Vector> ITEMS = new HashMap<Integer, Vector>();

    static {
        USERS.put(1, new DenseVector(new double[] {1.0, 2.0, 3.0}));
        USERS.put(2, new DenseVector(new double[] {0.0, -0.3, 0.7}));
        USERS.put(3, new DenseVector(new double[] {-2.0, 0.37, 0.0}));
        ITEMS.put(1, new DenseVector(new double[] {1.2, -1.02, 0.48}));
        ITEMS.put(2, new DenseVector(new double[] {-0.3, 1.0, 1.0}));
        ITEMS.put(3, new DenseVector(new double[] {0.1, 0.2, 0.3}));
    }

    public static final double[] RATINGS = new double[]
            {0.6, 4.7, 1.4, 0.642, 0.4, 0.15, -2.7774, 0.97, -0.126};
    public static final int[] TOP2 = new int[] {2, 3, 1, 2, 2, 3};

    public static class MockRecommender extends UVRecommender {

        public MockRecommender setUserCache(Map<Integer, Vector> userCache) {
            this.userCache = userCache;
            return this;
        }

        public MockRecommender setItemFeatures(Map<Integer, Vector> itemFeatures) {
            this.itemFeatures = itemFeatures;
            return this;
        }

        public static UVRecommender getDefault() {
            return new MockRecommender().setUserCache(USERS).setItemFeatures(ITEMS);
        }
    }

    @Test
    public void testPredictions() {
        UVRecommender rec = MockRecommender.getDefault();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                double pred = rec.predictRating(i + 1, j + 1);
                assertEquals(RATINGS[3*i + j], pred, 0.0001);
            }
        }
    }

}
