package ix.lab01.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the WordCountMapper works correctly
 */
public class WordCountMapperTest {

	@Test
	public void withLinksTest() {
		LongWritable inputKey = new LongWritable(1);
		Text inputValue = new Text("1 | 1 | user | ? | 2013-01-17 10:37:25 | ? | ? | ? | ? | web | Hello World, hello Hadoop");

		new MapDriver<LongWritable, Text, Text, IntWritable>()
				.withMapper(new WordCountMapper())
				.withInput(inputKey, inputValue)
				.withOutput(new Text("hello"), new IntWritable(1))
				.withOutput(new Text("world"), new IntWritable(1))
				.withOutput(new Text("hello"), new IntWritable(1))
				.withOutput(new Text("hadoop"), new IntWritable(1))
				.runTest();
	}

}
