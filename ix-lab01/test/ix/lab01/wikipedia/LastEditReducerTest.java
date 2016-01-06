package ix.lab01.wikipedia;

import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Test;

/**
 * This class contains tests to check that the LastEditReducer works correctly
 */
public class LastEditReducerTest {

	@Test
	public void latestTest() {
		Text inputKey = new Text("AccessibleComputing");
		List<Text> inputValues = Arrays.asList(new Text(
				"2001-01-21 02:12:21 | link1, link2"), new Text(
				"2001-01-21 03:12:21 | link2, link3"), new Text(
				"2001-01-20 03:12:21 | link1, link3"));

		Text outputValue = new Text("2001-01-21 03:12:21 | link2, link3");

		new ReduceDriver<Text, Text, Text, Text>()
				.withReducer(new LastEditReducer())
				.withInput(inputKey, inputValues)
				.withOutput(new Pair<Text, Text>(inputKey, outputValue))
				.runTest();
	}

}
