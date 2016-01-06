package ix.lab06.clustering;

import ix.lab06.utils.Covariance2d;
import ix.lab06.utils.DataUtils;
import ix.lab06.utils.Point2d;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;

public class GaussianMixtureModel {

    /** Data points (n,2) */
    private Point2d[] data;

    /** number of components */
    private int k;

    /** Mixture coefficients (k) */
    private double[] pi;

    /** Means (k,2) */
    private Point2d[] mus;

    /** Covariances (k,2,2) */
    private Covariance2d[] sigmas;

    /** Responsibilities (n,k) */
    private double[][] gamma;

    public GaussianMixtureModel(int k, Point2d[] data) {
        if (k < 2) {
            throw new IllegalArgumentException(
                    "The number of clusters k must be greater than 1");
        }
        this.k = k;
        this.data = data;
        this.pi = new double[k];
        this.mus = new Point2d[k];
        this.sigmas = new Covariance2d[k];
        this.gamma = new double[data.length][k];
    }

    public void initialize() {
        Random rand = new Random();

        // pick centers randomly
        ArrayList<Integer> index = new ArrayList<Integer>();
        for (int p = 0; p < this.data.length; p++) {
            index.add(p);
        }
        Collections.shuffle(index);

        // initialize each component
        double coefficientsSum = 0;
        for (int c = 0; c < this.k; c++) {
            // set data point as mean
            Point2d center = this.data[index.get(c)];
            this.mus[c] = new Point2d(center);

            // choose coefficient randomly
            this.pi[c] = rand.nextDouble();
            coefficientsSum = coefficientsSum + pi[c];

            // start with identity covariance matrix
            this.sigmas[c] = new Covariance2d(1, 1, 0);
        }

        // normalize mixture coefficients
        for (int i = 0; i < k; i++) {
            this.pi[i] /= coefficientsSum;
        }
    }

    /**
     * Calculate the responsibilities gamma of each component for each data
     * point.
     */
    public void eStep() {
        //TODO
        // Hint: look at the MultivariateNormalDistribution class used in logLikelihood
    }

    /**
     * Update the mean, covariance and mixture coefficient of each component
     * based on the responsibilities of each point.
     */
    public void mStep() {
        //TODO
    }

    public void run(int iter) {
        for (int i = 0; i < iter; i++) {
            this.eStep();
            this.mStep();
            System.out.format("ll: %f \n", this.logLikelihood());
        }
    }

    /**
     * Compute the log-likelihood of the data points, given the component
     * parameters.
     * 
     * @return The total log-likelihood.
     */
    double logLikelihood() {
        // initialize the density functions for each component
        MultivariateNormalDistribution[] pdfs = new MultivariateNormalDistribution[this.k];
        for (int c = 0; c < this.k; c++) {
            pdfs[c] = new MultivariateNormalDistribution(this.mus[c].toArray(),
                    this.sigmas[c].toArray());
        }

        double logLikelihood = 0;
        // compute the sum of the likelihood of each data point
        for (int p = 0; p < this.data.length; p++) {
            double pointLikelihood = 0;
            for (int c = 0; c < this.k; c++) {
                pointLikelihood += this.pi[c]
                        * (pdfs[c].density(this.data[p].toArray()));
            }
            logLikelihood += Math.log10(pointLikelihood);
        }
        return logLikelihood;
    }

    void print() {
        for (int c = 0; c < this.k; c++) {
            System.out.println("------------");

            System.out.format("Cluster %d:\n", c);
            System.out.format("Mean: %s\n", this.mus[c]);

            System.out.println("Covariance matrix:");
            System.out.println(this.sigmas[c]);
            System.out.format("Mixture coefficient: %f\n", this.pi[c]);
        }
    }

    public void plot() {
        List<Point2d[]> points = new ArrayList<Point2d[]>();
        List<Color> colors = new ArrayList<Color>();
                
        for (int p = 0; p < this.data.length; ++p) {
            Color c = new Color((float)this.gamma[p][0], (float)this.gamma[p][1], 0);
            points.add(new Point2d[] { this.data[p] });
            colors.add(c);
        }
        
        DataUtils.scatterPlot(points, colors);
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            System.err.format(
                    "Usage: %s /path/to/data/points.txt nb_clusters nb_iter\n",
                    GaussianMixtureModel.class.getName());
            System.exit(-1);
        }
        String fileName = args[0];
        int nbClusters = Integer.parseInt(args[1]);
        int nbIter = Integer.parseInt(args[2]);

        Point2d[] data = DataUtils.readFromFile(fileName);

        GaussianMixtureModel gmm = new GaussianMixtureModel(nbClusters, data);
        gmm.initialize();
        gmm.run(nbIter);
        gmm.print();
        gmm.plot();
    }

}
