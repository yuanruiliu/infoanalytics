package ix.utils;

import java.util.Iterator;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;

/**
 * Utility methods for Vector and VectorWritable objects.
 */
public final class VectorUtils {
	
	private static final String FIELD_SEPARATOR = ":";
	private static final String ELEMENT_SEPARATOR = "\t";
	
	private VectorUtils() {}

	/**
	 * Computes the cosine similarity between two given vectors.
	 * @param v1 First vector
	 * @param v2 Second vector
	 * @return The cosine similarity between the two vectors.
	 */
	public static double cosineSimilarity(Vector v1, Vector v2) {
		return v1.dot(v2) / (v1.norm(2.0) * v2.norm(2.0));
	}

	/**
	 * Serializes a Vector to a String (for passing through the Hadoop
	 * configuration).
	 * 
	 * @param vector
	 *            Vector to serialize.
	 * @return String serialization of the VectorWritable
	 */
	public static String serialize(Vector vector) {
		StringBuilder serialized = new StringBuilder();
		
		for (Iterator<Element> it = vector.iterateNonZero(); it.hasNext(); ) {
			Element el = it.next();
			serialized.append(el.index());
			serialized.append(FIELD_SEPARATOR);
			serialized.append(el.get());
			
			if (it.hasNext()) {
				serialized.append(ELEMENT_SEPARATOR);
			}
		}
		
		return serialized.toString();
	}

	/**
	 * Reads a Vector from a String containing a serialized Vector.
	 * 
	 * @param serializedVector
	 * @return The Vector
	 */
	public static Vector deserialize(String serializedVector) {
		Vector vector = new SequentialAccessSparseVector(Integer.MAX_VALUE, 1);
		
		for (String element: serializedVector.split(ELEMENT_SEPARATOR)) {
			String[] parts = element.split(FIELD_SEPARATOR);
			int index = Integer.parseInt(parts[0]);
			double value = Double.parseDouble(parts[1]);
			
			vector.set(index, value);
		}
		
		return vector;
	}
}
