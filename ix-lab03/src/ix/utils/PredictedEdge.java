package ix.utils;

public class PredictedEdge implements Comparable<PredictedEdge> {

    public final String src;
    public final String dst;
    public final double score;

    public PredictedEdge(String src, String dst, double score) {
        this.src = src;
        this.dst = dst;
        this.score = score;
    }

    @Override
    public int compareTo(PredictedEdge that) {
        // Note that we invert the natural order here...
        return Double.compare(that.score, this.score);
    }
}
