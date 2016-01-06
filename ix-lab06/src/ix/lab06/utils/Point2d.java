package ix.lab06.utils;

/**
 * Class representing a point in a 2D space.
 */
public class Point2d {
    private double x;
    private double y;

    public Point2d(double x, double y) {
        this.set(x, y);
    }

    public Point2d(Point2d p) {
        this.set(p.getX(), p.getY());
    }
    
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public double[] toArray() {
        return new double[] { x, y };
    }

    /**
     * Computes the euclidian distance from this point to that point.
     * @param that The other point.
     * @return The distance from this point to that point.
     */
    public double distanceTo(Point2d that) {
        return Math.sqrt(Math.pow(this.getX() - that.getX(), 2)
                + Math.pow(this.getY() - that.getY(), 2));
    }
    
    @Override
    public String toString() {
        return String.format("(%f, %f)", this.x, this.y);
    }
    
}
