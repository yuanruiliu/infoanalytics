package ix.lab02.degdist;

import ix.lab02.degdist.NeighborsSetReducer;
import ix.utils.TextArrayWritable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

/**
 * This class contains tests to check that the NeighborsSetReducer works
 * correctly
 */
public class NeighborsSetReducerTest {

	@Test
	public void latestTest() {
		Text inputKey = new Text("Article1");
		List<Text> inputValues = Arrays.asList(new Text("Article2"),
				new Text("Article3"), new Text("Article2"));

		List<String> uniqueNeighbors = new ArrayList<String>();
		uniqueNeighbors.add("Article2");
		uniqueNeighbors.add("Article3");
		
		TextArrayWritable outputValue = new TextArrayWritable();
		outputValue.setStringCollection(uniqueNeighbors);

		new ReduceDriver<Text, Text, Text, TextArrayWritable>()
				.withReducer(new NeighborsSetReducer())
				.withInput(inputKey, inputValues)
				.withOutput(inputKey, outputValue)
				.runTest();
	}
	
}
