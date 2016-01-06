package ix.lab02.giantcomp;

import ix.utils.EdgeRemovalResults;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/** Simple interface for a strategy to remove edges from a graph. */
public interface EdgeRemovalStrategy {

    public EdgeRemovalResults apply(SimpleGraph<String, DefaultEdge> graph);

}
