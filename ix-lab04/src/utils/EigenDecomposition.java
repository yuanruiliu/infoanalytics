package utils;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

/** Re-implementation of the eigendecomposition on top of Jama's SVD. */
public class EigenDecomposition {

    public final double[] eigenvalues;
    public final Matrix eigenvectors;

    public EigenDecomposition(Matrix matrix) {
        SingularValueDecomposition svd = matrix.svd();
        this.eigenvalues = svd.getSingularValues();
        this.eigenvectors = svd.getU();
    }

}
