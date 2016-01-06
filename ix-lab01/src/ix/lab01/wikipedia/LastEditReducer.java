package ix.lab01.wikipedia;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reduce operation of the LastEdit job. The reducer gathers all edits of one
 * article, and simply keeps the most recent one.
 * 
 * It simply sums all the individual counts to get the total number of
 * occurrences for this word.
 * 
 * Example:
 * 
 * Input: AccessibleComputing	"2001-01-21 02:12:21 | link1, link2", "2008-06-29 12:12:21 | link3"
 * 
 * Output: AccessibleComputing	"2008-06-29 12:12:21 | link3"
 * 
 */
@SuppressWarnings("unused")
public class LastEditReducer extends Reducer<Text, Text, Text, Text> {

	private Text outputValue = new Text(); // the most recent edit

	/**
	 * The reduce operation simply keeps the most recent edit.
	 * 
	 * Hint: each value is of the form "YYYY-MM-DD hh:mm:ss | link1, link2". You
	 * can thus directly compare the whole value instead of extracting the
	 * timestamp first.
	 * 
	 * @param inputKey
	 *            The article name
	 * @param inputValues
	 *            The list of edits
	 */
	public void reduce(Text inputKey, Iterable<Text> inputValues,
			Context context) throws IOException, InterruptedException {
		//TODO
	}

}
