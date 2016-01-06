package ix.lab02.degdist;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the DegreeDistributionReducerTest works
 * correctly
 */
public class DegreeDistributionReducerTest {

	@Test
	public void latestTest() {
		IntWritable inputKey = new IntWritable(1);
		List<IntWritable> inputValues = Arrays.asList(new IntWritable(2),
				new IntWritable(1), new IntWritable(1));

		IntWritable outputValue = new IntWritable(4);

		new ReduceDriver<IntWritable, IntWritable, IntWritable, IntWritable>()
				.withReducer(new DegreeDistributionReducer())
				.withInput(inputKey, inputValues)
				.withOutput(inputKey, outputValue)
				.runTest();
	}
	
}
