package ix.lab01.wikipedia;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the ArticleLinksMapper works correctly
 */
public class ArticleLinksMapperTest {

	@Test
	public void withLinksTest() {
		Text inputKey = new Text("Anarchism");
		Text inputValue = new Text(
				"2004-05-03 23:56:28 | 16th_century, 1789, 1793");

		IntWritable outputValue = new IntWritable(1);

		new MapDriver<Text, Text, Text, IntWritable>()
				.withMapper(new ArticleLinksMapper())
				.withInput(inputKey, inputValue)
				.withOutput(new Text("Anarchism|16th_century"), outputValue)
				.withOutput(new Text("Anarchism|1789"), outputValue)
				.withOutput(new Text("Anarchism|1793"), outputValue).runTest();
	}

}
