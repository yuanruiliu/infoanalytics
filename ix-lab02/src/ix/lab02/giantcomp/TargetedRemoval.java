package ix.lab02.giantcomp;

import java.util.List;
import java.util.Map;

import ix.utils.EdgeRemovalResults;

import org.apache.hadoop.thirdparty.guava.common.base.Functions;
import org.apache.hadoop.thirdparty.guava.common.collect.Ordering;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


/** Remove edges in a clever way and observe how the size of the giant component evolves. */
public class TargetedRemoval implements EdgeRemovalStrategy {

    /**
     * Removes 100 edges in a clever way until the giant component reaches 20%
     * of its initial size.
     *
     * @param graph  the graph
     **/
    @Override
    public EdgeRemovalResults apply(SimpleGraph<String, DefaultEdge> graph) {
        EdgeRemovalResults results = new EdgeRemovalResults();
        int initSize = GiantComponent.gcSize(graph);
        results.add(initSize);

        // TODO Complete this method.

        // You might find the following helpful:
        // - the class org.jgrapht.alg.NeighborIndex
        // - the function orderedEdges()
        org.jgrapht.alg.NeighborIndex<V, E>;

        return results;
    }

    /**
     * Helper function that takes a mapping between edges and strength values (of
     * any comparable type) and returns the edges sorted by increasing values.
     *
     * @param strengths  a Map between edges and their strength
     * @return  a list of edges, sorted according to their strength (weakest first.)
     */
    public static <E extends Comparable<E>> List<DefaultEdge> orderedEdges(Map<DefaultEdge, E> strengths) {
        return Ordering.natural().onResultOf(Functions.forMap(strengths))
                .immutableSortedCopy(strengths.keySet());
    }

    /** Run the edge removal experiment. */
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <input file> <result file>",
                    TargetedRemoval.class.getName()));
            System.exit(-1);
        }
        GiantComponent.edgeRemovalExperiment(new TargetedRemoval(), args[0], args[1]);
        System.out.println(String.format("Done. Results stored in '%s'.", args[1]));
    }
}
