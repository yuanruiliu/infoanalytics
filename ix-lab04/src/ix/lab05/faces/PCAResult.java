package ix.lab05.faces;

import Jama.Matrix;

/** Simple container class to store the results of a principal component analysis. */
public class PCAResult {
    public final Matrix rotation;
    public final double[] values;

    public PCAResult(Matrix rotation, double[] values) {
        this.rotation = rotation;
        this.values = values;
    }
}
