package ix.utils;

import java.util.Collection;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * Custom type of Writable that allows to write arrays of Text objects to text output files.
 */
public class TextArrayWritable extends ArrayWritable {
	public TextArrayWritable() {
		super(Text.class);
	}
	
	/**
	 * Sets the writable content from a Collection of String objects.
	 * 
	 * @param texts A Collection of String objects.
	 */
	public void setStringCollection(Collection<String> texts) {
		// convert to an array of Text
		Text[] textArray = new Text[texts.size()];
		
		int i = 0;
		for (String element: texts) {
			textArray[i] = new Text(element);
			++i;
		}
		
		// set array
		this.set(textArray);
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		
		for (Writable w: this.get()) {
			hashCode *= w.hashCode();
		}
		
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TextArrayWritable) {
			TextArrayWritable other = (TextArrayWritable) obj;
			
			Writable[] thisArray = this.get(), otherArray = other.get();
			
			if (thisArray.length != otherArray.length) {
				return false;
			} else {
				for (int i = 0; i < thisArray.length; ++i) {
					if (!thisArray[i].equals(otherArray[i])) {
						return false;
					}
				}
				
				return true;
			}
			
		} else {
			return false;
		}
	}
	
	
}
