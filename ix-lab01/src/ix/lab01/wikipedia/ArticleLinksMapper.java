package ix.lab01.wikipedia;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * This mapper takes the most recent edit of an article, extracts all links from this article to others, and outputs individual edges of weight 1.
 * 
 * Example:
 * 
 * Input:
 * 
 * Anarchism 	2004-05-03 23:56:28 | 16th_century, 1789, 1793
 * 
 * Output:
 * 
 * Anarchism|16th_century	1
 * Anarchism|1789			1
 * Anarchism|1793			1
 * 
 */
@SuppressWarnings("unused")
public class ArticleLinksMapper extends Mapper<Text, Text, Text, IntWritable> {

	private Text outputKey = new Text(); // edge id, e.g. "article1|article2"
	private final IntWritable outputValue = new IntWritable(1);  // edge weight (always 1)

	/**
	 * Map operation: this function is called for each each most recent edit.
	 * 
	 * It iterates over the outgoing links of the article, and outputs one edge of weight 1 for each.
	 * 
	 * @param inputKey
	 *            The article name
	 * @param inputValue
	 *            Its most recent edit
	 * @param context
	 *            Job context, for writing output, getting configuration, etc.
	 */
	public void map(Text inputKey, Text inputValue, Context context) throws IOException, InterruptedException {
		//TODO
	}

}
