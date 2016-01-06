package ix.lab06.utils;

/**
 * Class representing a 2D covariance matrix.
 */
public class Covariance2d {
    private double varX;
    private double varY;
    private double covXY;

    /**
     * Initialize the covariance matrix from the variance of each variable,
     * as well as their covariance.
     * @param varX Variance of X
     * @param varY Variance of Y
     * @param covXY Covariance of X and Y
     */
    public Covariance2d(double varX, double varY, double covXY) {
        this.set(varX, varY, covXY);
    }

    public void set(double varX, double varY, double covXY) {
        this.varX = varX;
        this.varY = varY;
        this.covXY = covXY;
    }

    public double[][] toArray() {
        return new double[][] { new double[] { varX, covXY },
                new double[] { covXY, varY } };
    }

    @Override
    public String toString() {
        return String.format("((%f,  %f), (%f, %f)", this.varX, this.covXY, this.covXY, this.varY);
    }
}
