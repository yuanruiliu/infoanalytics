package ix.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;;

/** Utility class that read a Wikipedia graph. */
public class WikiGraph {

    /** Creates a JGraphT graph from a file containing links between Wikipedia articles. */
    public static SimpleGraph<String, DefaultEdge> parse(File wikiFile) {
        SimpleGraph<String, DefaultEdge> graph =
                new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);
        Scanner input = null;
        try {
            input = new Scanner(wikiFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Edge edge = new Edge();
        while (input.hasNext()) {
            String nextLine = input.nextLine();
            edge.parse(nextLine);
            if (!edge.sourceArticleName.equals(edge.destinationArticleName)) {
                Graphs.addEdgeWithVertices(graph,
                        edge.sourceArticleName, edge.destinationArticleName);
            }
        }

        input.close();
        return graph;
    }

    /** Thin wrapper around parse() that takes the path as a String. */
    public static SimpleGraph<String, DefaultEdge> load(String path) {
        File file = new File(path);
        return parse(file);
    }

}
