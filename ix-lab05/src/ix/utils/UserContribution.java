package ix.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Writable;
import org.apache.mahout.math.Vector;
import org.apache.mahout.math.Vector.Element;
import org.apache.mahout.math.VectorWritable;

/**
 * Writable objects containing the contribution of one user to the global
 * recommendation.
 * 
 * The contribution consists of his similarity with the user for which we
 * recommend movies, and the ratings he made.
 */
public class UserContribution implements Writable {
	
	/**
	 * Constant for approximate comparison of doubles.
	 */
	private static final double EPSILON = 0.00001;

	private DoubleWritable similarity = new DoubleWritable();
	private VectorWritable ratings = new VectorWritable();

	/**
	 * Sets the similarity and the ratings of a user. 
	 * @param similarity
	 * @param ratings
	 */
	public void set(double similarity, Vector ratings) {
		this.similarity.set(similarity);
		this.ratings.set(ratings);
	}

	/**
	 * Gets the similarity of this user to the user for which we make a recommendation. 
	 * @return
	 */
	public double getSimilarity() {
		return this.similarity.get();
	}

	/**
	 * Gets the ratings of this user.
	 * @return
	 */
	public Vector getRatings() {
		return this.ratings.get();
	}

	@Override
	public void write(DataOutput out) throws IOException {
		this.similarity.write(out);
		this.ratings.write(out);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.similarity.readFields(in);
		this.ratings.readFields(in);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UserContribution) {
			UserContribution that = (UserContribution) obj;

			if (this.similarity.get() != that.getSimilarity()) {
				return false;
			} else {
				Vector thisRatings = this.ratings.get();
				Vector thatRatings = that.ratings.get();

				if (thisRatings.size() != thatRatings.size()) {
					return false;
				} else {
					for (Iterator<Element> thisIt = thisRatings
							.iterateNonZero(); thisIt.hasNext();) {
						Element el = thisIt.next();
						int index = el.index();
						double value = el.get();

						if (thatRatings.get(index) != value && Math.abs(thatRatings.get(index) - value) > EPSILON) {
							return false;
						}
					}

					for (Iterator<Element> thatIt = thatRatings
							.iterateNonZero(); thatIt.hasNext();) {
						Element el = thatIt.next();
						int index = el.index();
						double value = el.get();

						if (thisRatings.get(index) != value && Math.abs(thisRatings.get(index) - value) > EPSILON) {
							return false;
						}
					}

					return true;
				}
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return 31 * (int) this.similarity.get() + this.ratings.get().hashCode();
	}

	@Override
	public String toString() {
		return String.format("[similarity:%f,ratings:%s]",
				this.similarity.get(), this.ratings.get());
	}

}
