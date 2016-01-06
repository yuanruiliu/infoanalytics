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
	public TextArrayWritable setStringCollection(Collection<String> texts) {
		// convert to an array of Text
		Text[] textArray = new Text[texts.size()];
		
		int i = 0;
		for (String element: texts) {
			textArray[i] = new Text(element);
			++i;
		}
		
		// set array
		this.set(textArray);
		
		return this;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (Writable el: this.get()) {
			hashCode += el.toString().hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TextArrayWritable) {
			TextArrayWritable other = (TextArrayWritable)obj;
			Writable[] myEls = this.get();
			Writable[] otherEls = other.get();
			
			if (otherEls.length != myEls.length) {
				return false;
			} else {
				for (int i = 0; i < myEls.length; ++i) {
					if (!myEls[i].equals(otherEls[i])) {
						return false;
					}
				}
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Writable el: this.get()) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(el.toString());
		}
		return sb.toString();
	}
	
	
}
