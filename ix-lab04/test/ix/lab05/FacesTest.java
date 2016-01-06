package ix.lab05;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ix.lab05.faces.Faces;
import ix.lab05.faces.PCAResult;

import java.util.Random;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import utils.NotYetImplementedException;
import Jama.Matrix;

public class FacesTest {

    private static final Matrix ONES = new Matrix(
            new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}});
    private static final Matrix DATA1 = new Matrix(
            new double[][]{{1.0, -1.0}, {-1.0, 1.0}, {1.0, 1.0}, {-1.0, -1.0}});
    private static final Matrix DATA2 = new Matrix(
            new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, -1.0}});
    private static final double EPSILON = 10E-5;


    /*
     * This rule makes it such that the tests on functions that throw a
     * NotYetImplementedException are ignored.
     */
    @Rule
    public TestRule hasToBeImplemented = new TestRule() {
        @Override
        public Statement apply(final Statement base, final Description desc) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        base.evaluate();
                    } catch (NotYetImplementedException e) {}
                }
            };
        }
    };

    @Test
    public void testVarianceOnes() {
        for (double var : Faces.variance(ONES)) {
            assertTrue(Math.abs(var) < EPSILON);
        }
    }

    @Test
    public void testNonNullVariance() {
        for (double var : Faces.variance(DATA1)) {
            assertTrue(Math.abs(var) < 1 + EPSILON);
        }
    }

    @Test
    public void testPcaRandom() {
        Random rand = new Random();
        for (int iter = 0; iter < 10; ++iter) {
            double[][] data = new double[5][3];
            for (int i = 0; i < 5; ++i) {
                for (int j = 0; j < 3; ++j) {
                    data[i][j] = 10 * rand.nextDouble();
                }
            }
            PCAResult pca = Faces.pca(new Matrix(data));
            assertEquals(pca.values.length, 3);
            // Components must be sorted in descending order.
            for (int i = 0; i < pca.values.length - 1; ++i) {
                assertTrue(pca.values[i] >= pca.values[i+1]);
            }
            // Rotation matrix is orthogonal
            Matrix id = pca.rotation.transpose().times(pca.rotation);
            assertTrue(id.minus(Matrix.identity(3, 3)).normF() < EPSILON);
        }
    }

    @Test
    public void testPcaKnown() {
        Matrix rotation = new Matrix(
                new double[][]{{0.56460328, -0.82536242}, {-0.82536242, -0.56460328}});
        double[] values = new double[]{5.59035644, 1.29853245};

        PCAResult pca = Faces.pca(DATA2);
        for (int i = 0; i < values.length; ++i) {
            assertTrue(Math.abs(pca.values[i] - values[i]) < EPSILON);
        }
        Matrix diff = pca.rotation.minus(rotation);
        assertTrue(diff.normF() < EPSILON);
    }

    @Test
    public void testProject() {
        Matrix truth = new Matrix(
                new double[][]{{-1.40432738, 1.46252375},
                               {-1.92584566, -1.31740766},
                               {3.33017303, -0.14511609}});
        Matrix diff = Faces.project(DATA2).minus(truth);
        assertTrue(diff.normF() < EPSILON);
    }
}
