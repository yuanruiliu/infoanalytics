package ix.lab01.wikipedia;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the LastEditMapper works correctly
 */
public class LastEditMapperTest {

	@Test
	public void withLinksTest() {
		LongWritable inputKey = new LongWritable(1);
		Text inputValue = new Text(
				"REVISION 12 3438130 Anarchism 2007-05-03T23:56:28Z Phil_Sandifer 60895 | CATEGORY | MAIN link1 link2 link2 link1 link3");

		Text outputKey = new Text("Anarchism");
		Text outputValue = new Text("2007-05-03 23:56:28 | link1, link2, link3");

		new MapDriver<LongWritable, Text, Text, Text>()
				.withMapper(new LastEditMapper())
				.withInput(inputKey, inputValue)
				.withOutput(outputKey, outputValue).runTest();
	}

}
