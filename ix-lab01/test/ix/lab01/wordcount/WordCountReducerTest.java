package ix.lab01.wordcount;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the WordCountReducer works correctly
 */
public class WordCountReducerTest {

	@Test
	public void latestTest() {
		Text inputKey = new Text("hadoop");
		List<IntWritable> inputValues = Arrays.asList(new IntWritable(1),
				new IntWritable(2),
				new IntWritable(5),
				new IntWritable(1));

		IntWritable outputValue = new IntWritable(9);

		new ReduceDriver<Text, IntWritable, Text, IntWritable>()
				.withReducer(new WordCountReducer())
				.withInput(inputKey, inputValues)
				.withOutput(inputKey, outputValue)
				.runTest();
	}

}
