package ix.lab05;

import static org.junit.Assert.assertEquals;
import ix.utils.VectorUtils;

import java.io.IOException;

import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.junit.Test;

public class VectorUtilsTest {

	@Test
	public void testSerialization() throws IOException {
		Vector v = new SequentialAccessSparseVector(Integer.MAX_VALUE, 1);
		v.set(3, 8);
		v.set(10, Math.PI);

		String serialization = VectorUtils.serialize(v);
		Vector deserialized = VectorUtils.deserialize(serialization);

		assertEquals(0, deserialized.get(1), 0.0001);
		assertEquals(8, deserialized.get(3), 0.0001);
		assertEquals(0, deserialized.get(8), 0.0001);
		assertEquals(Math.PI, deserialized.get(10), 0.0001);
	}
}
