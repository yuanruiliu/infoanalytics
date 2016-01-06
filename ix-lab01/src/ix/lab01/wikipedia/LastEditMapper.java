package ix.lab01.wikipedia;

import ix.utils.WikiEdit;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;

/**
 * This mapper simply extracts the article name, timestamp and list of outgoing
 * links of each edit.
 * 
 * Example:
 * 
 * Input:
 * 
 * REVISION 12 3438130 Anarchism 2007-05-03T23:56:28Z Phil_Sandifer 60895 | CATEGORY | MAIN link1 link2 link2 link1 link3
 * 
 * Output:
 * 
 * Anarchism	2007-05-03 23:56:28 | link1, link2, link3
 * 
 */
@SuppressWarnings("unused")
public class LastEditMapper extends Mapper<LongWritable, Text, Text, Text> {

	private Text outputKey = new Text(); // article name
	private Text outputValue = new Text(); // concatenation of timestamp and outgoing links

	private WikiEdit edit = new WikiEdit();

	/**
	 * Map operation: this function is called for each line of the input file.
	 * It simply extracts the article name as key, and the timestamp and
	 * outgoing links as values
	 * 
	 * Hint: use StringUtils.join(separator, list) to concatenate a list of
	 * String objects together with a separator
	 * 
	 * @param inputKey
	 *            Offset of the current line in the input file
	 * @param inputValue
	 *            Content of the line
	 * @param context
	 *            Job context, for writing output, getting configuration, etc.
	 */
	public void map(LongWritable inputKey, Text inputValue, Context context)
			throws IOException, InterruptedException {
		// get line content
		String line = inputValue.toString();

		// parse edit
		this.edit.parse(line);

		//TODO
	}

}
