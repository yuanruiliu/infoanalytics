package ix.lab02.degdist;

import ix.lab02.degdist.NeighborsSetMapper;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the NeighborsSetMapper works correctly
 */
public class NeighborsSetMapperTest {

	@Test
	public void outputTest() {
		LongWritable inputKey = new LongWritable(1);
		Text inputValue = new Text("Article1|Article2	1");

		Text outputKey1 = new Text("Article1");
		Text outputValue1 = new Text("Article2");
		
		Text outputKey2 = new Text("Article2");
		Text outputValue2 = new Text("Article1");


		new MapDriver<LongWritable, Text, Text, Text>()
				.withMapper(new NeighborsSetMapper())
				.withInput(inputKey, inputValue)
				.withOutput(outputKey1, outputValue1)
				.withOutput(outputKey2, outputValue2) 				
				.runTest();
	}

}
