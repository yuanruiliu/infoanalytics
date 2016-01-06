package ix.lab06.clustering;

import static org.junit.Assert.assertEquals;
import ix.lab06.utils.Point2d;

import org.junit.Test;

public class KmeansTest extends Kmeans {

    public KmeansTest() {
        super(2);

        Point2d[] data = new Point2d[4];
        data[0] = new Point2d(1, 10);
        data[1] = new Point2d(2, 20);
        data[2] = new Point2d(3, 31);
        data[3] = new Point2d(6, 51);
        this.setData(data);

        // init centers
        this.centers[0] = new Point2d(0, 0);
        this.centers[1] = new Point2d(0, 0);

        // manually set assignment
        this.assignments = new int[] { 1, 0, 0, 1 };
    }

    @Test
    public void testMstep() {
        this.mStep();

        float epsilon = 0.00001f;
        
        // check that centers are correct
        assertEquals(2.5, this.centers[0].getX(), epsilon);
        assertEquals(25.5, this.centers[0].getY(), epsilon);
        assertEquals(3.5, this.centers[1].getX(), epsilon);
        assertEquals(30.5, this.centers[1].getY(), epsilon);
    }
}
