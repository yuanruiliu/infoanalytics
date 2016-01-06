package ix.lab05;

import ix.lab05.similarity.RecommenderMapper;
import ix.utils.UserContribution;
import ix.utils.VectorUtils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.VectorWritable;
import org.junit.Test;

/**
 * This class contains tests to check that the RecommenderMapper works correctly
 */
public class RecommenderMapperTest {
	
	Configuration conf = new Configuration();
	
	@Test
	public void mapperTest() {
		// prepare user configuration
		Vector myRatings = new SequentialAccessSparseVector(Integer.MAX_VALUE, 1);
		myRatings.set(1, 3.0);
		myRatings.set(2, 4.0);
		
		int myId = 2;
		String myRatingsSerialized = VectorUtils.serialize(myRatings);
		
		conf.setInt("userId", myId);
		conf.set("userRatings", myRatingsSerialized);
		
		// set other user's ratings
		Vector otherRatings = new SequentialAccessSparseVector(Integer.MAX_VALUE, 1);
		otherRatings.set(1, 5.0);
		otherRatings.set(2, 2.0);
		otherRatings.set(3, 1.0);
		
		// create map driver and set the configuration
		MapDriver<IntWritable, VectorWritable, NullWritable, UserContribution> md = new MapDriver<IntWritable, VectorWritable, NullWritable, UserContribution>();
		md.setConfiguration(conf);
		
		IntWritable inputKey = new IntWritable(1);
		VectorWritable inputValue = new VectorWritable(otherRatings);
		
		double similarity = (15 + 8.0) / (5 * Math.sqrt(30));
		
		UserContribution outputValue = new UserContribution();
		outputValue.set(similarity, otherRatings);
		
		// run test
		md.withMapper(new RecommenderMapper())
			.withInput(inputKey, inputValue)
			.withOutput(NullWritable.get(), outputValue).runTest();
	}

}
