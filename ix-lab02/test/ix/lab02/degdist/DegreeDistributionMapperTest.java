package ix.lab02.degdist;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the DegreeDistributionMapper works correctly
 */
public class DegreeDistributionMapperTest {

	@Test
	public void outputTest() {
		Text inputKey = new Text("Article1");
		Text inputValue = new Text("Article2, Article3");

		IntWritable outputKey = new IntWritable(2);
		IntWritable outputValue = new IntWritable(1);

		new MapDriver<Text, Text, IntWritable, IntWritable>()
				.withMapper(new DegreeDistributionMapper())
				.withInput(inputKey, inputValue)
				.withOutput(outputKey, outputValue).runTest();
	}

}
