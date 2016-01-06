package ix.lab02.giantcomp;

import ix.utils.EdgeRemovalResults;
import ix.utils.WikiGraph;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


/**
 * This class provides utilities related to the giant component of a graph, as well as
 * a method to test the resistance of the GC to edge removals.
 */
public class GiantComponent {

    /**
     * Computes the size of the largest component of a given graph.
     *
     * @param graph  The graph.
     */
    public static int gcSize(SimpleGraph<String, DefaultEdge> graph) {
        ConnectivityInspector<String, DefaultEdge> inspector =
                new ConnectivityInspector<String, DefaultEdge>(graph);

        // If the graph is connected, simply return the size of the graph.
        if (inspector.isGraphConnected()) {
            return graph.vertexSet().size();
        }

        // Else, get the largest connected component.
        int maxSize = 0;
        for (Set<String> component : inspector.connectedSets()) {
            if (component.size() > maxSize) {
                maxSize = component.size();
            }
        }
        return maxSize;
    }


    /**
     * Computes the number of common neighbors of source and target vertices using
     * an auxiliary neighborhood cache structure.
     *
     * @param nIndex  neighborhood cache
     * @param source  source vertex
     * @param target  target vertex
     */
    public static int numCommonNeighbours(NeighborIndex<String, DefaultEdge> nIndex,
            String source, String target) {
        Set<String> intersection = new HashSet<String>(nIndex.neighborsOf(source));
        intersection.retainAll(nIndex.neighborsOf(target));
        return intersection.size();
    }


    /**
     * Performs a edge removal experiment on a graph. It removes edges according to a
     * given strategy, and saves the successive sizes of the giant component in a text
     * file.
     *
     * @param strategy  the strategy to use to remove the edges
     * @param input  the path to the graph
     * @param output  the path to the file where the results will be written
     */
    public static void edgeRemovalExperiment(EdgeRemovalStrategy strategy, String input, String output) {
        SimpleGraph<String, DefaultEdge> graph = WikiGraph.load(input);
        EdgeRemovalResults results = strategy.apply(graph);
        results.saveToFile(output);
    }


    /** Loads a graph from the given path, and prints the size of the giant component. */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(String.format("Usage: %s <input file>", GiantComponent.class.getName()));
            System.exit(-1);
        }

        SimpleGraph<String, DefaultEdge> graph = WikiGraph.load(args[0]);
        int size = gcSize(graph);
        System.out.println(String.format("Size of the giant component in %s: %d", args[0], size));
    }
}
