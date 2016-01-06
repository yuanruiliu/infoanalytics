package ix.lab05;

import static org.junit.Assert.assertEquals;
import ix.lab05.similarity.RecommenderReducer;
import ix.utils.UserContribution;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.Test;

/**
 * This class contains tests to check that the RecommenderReducer works correctly
 */
public class RecommenderReducerTest {
	
	private final double EPSILON = 0.000001; 
	
	@Test
	public void reducerTest() throws IOException {
		double user1Similarity = 0.8;
		Vector user1Ratings = new SequentialAccessSparseVector(3);
		user1Ratings.set(1, 5.0);
		user1Ratings.set(2, 3.0);

		UserContribution user1Contribution = new UserContribution();
		user1Contribution.set(user1Similarity, user1Ratings);

		double user2Similarity = 0.4;
		Vector user2Ratings = new SequentialAccessSparseVector(3);
		user2Ratings.set(1, 3.0);
		user2Ratings.set(2, 1.0);

		UserContribution user2Contribution = new UserContribution();
		user2Contribution.set(user2Similarity, user2Ratings);

		Vector expecetedRatings = new SequentialAccessSparseVector(3);
		expecetedRatings.set(1, 13 / 3.0);
		expecetedRatings.set(2, 7 / 3.0);
		
		List<UserContribution> inputValues = new LinkedList<UserContribution>();
		inputValues.add(user1Contribution);
		inputValues.add(user2Contribution);
		
		List<Pair<NullWritable, VectorWritable>> outputs = new ReduceDriver<NullWritable, UserContribution, NullWritable, VectorWritable>()
				.withReducer(new RecommenderReducer())
				.withInput(NullWritable.get(), inputValues)
				.run();
		
		assertEquals(1, outputs.size());
		
		Vector actualRatings = outputs.get(0).getSecond().get();
		
		assertEquals(expecetedRatings.get(1), actualRatings.get(1), EPSILON);
		assertEquals(expecetedRatings.get(2), actualRatings.get(2), EPSILON);
	}

	@Test
	public void normalizationTest() throws IOException {
		double user1Similarity = 0.8;
		Vector user1Ratings = new SequentialAccessSparseVector(3);
		user1Ratings.set(2, 3.0);

		UserContribution user1Contribution = new UserContribution();
		user1Contribution.set(user1Similarity, user1Ratings);

		double user2Similarity = 0.4;
		Vector user2Ratings = new SequentialAccessSparseVector(3);
		user2Ratings.set(1, 3.0);
		user2Ratings.set(2, 1.0);

		UserContribution user2Contribution = new UserContribution();
		user2Contribution.set(user2Similarity, user2Ratings);

		Vector expectedRatings = new SequentialAccessSparseVector(3);
		expectedRatings.set(1, 3.0);
		expectedRatings.set(2, 7 / 3.0);
		
		List<UserContribution> inputValues = new LinkedList<UserContribution>();
		inputValues.add(user1Contribution);
		inputValues.add(user2Contribution);
		
		List<Pair<NullWritable, VectorWritable>> outputs = new ReduceDriver<NullWritable, UserContribution, NullWritable, VectorWritable>()
				.withReducer(new RecommenderReducer())
				.withInput(NullWritable.get(), inputValues)
				.run();
		
		assertEquals(1, outputs.size());
		
		Vector actualRatings = outputs.get(0).getSecond().get();
		
		assertEquals("Rating 1", expectedRatings.get(1), actualRatings.get(1), EPSILON);
		assertEquals("Rating 2", expectedRatings.get(2), actualRatings.get(2), EPSILON);
	}

}

