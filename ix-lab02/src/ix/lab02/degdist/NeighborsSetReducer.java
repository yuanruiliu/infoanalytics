package ix.lab02.degdist;

import ix.utils.TextArrayWritable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * Reduce operation of the NeighbSet job. The reducer gathers all the neighbors
 * of an article outputs its set of linked articles. For example:
 *
 * ## Input:
 * Article1 (Article2, Article3, Article2)
 *
 * ## Output:
 * Article1 Article2, Article3
 */
@SuppressWarnings("unused")
public class NeighborsSetReducer extends Reducer<Text, Text, Text, TextArrayWritable> {

    private TextArrayWritable outputValue = new TextArrayWritable();
	private List<String> list = new ArrayList<String>();

    /**
     * Each reduce operation takes an article and its list of linked articles,
     * and extract the set of unique linked articles.
     *
     * Hint: use the setStringCollection() method of {@link TextArrayWritable}
     * once you have obtained the set of linked articles.
     *
     * @param inputKey The name of the articles
     * @param inputValue A list of linked articles (possibly with duplicates)
     */
    public void reduce(Text sourceArticle, Iterable<Text> coeditedArticles, Context context)
            throws IOException, InterruptedException {
    	
    	this.list.clear();
    	
    	for (Text value:coeditedArticles){
    		
    		if((!value.equals(sourceArticle))&&(!(this.list.contains(value.toString()))))
    			this.list.add(value.toString());
    	}
    	
    	this.outputValue.setStringCollection(this.list);
    	
    	context.write(sourceArticle,this.outputValue);
    }

}
