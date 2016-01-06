package ix.utils;

import java.io.File;
import java.io.IOException;

import org.apache.hadoop.thirdparty.guava.common.base.Charsets;
import org.apache.hadoop.thirdparty.guava.common.io.Files;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class GraphUtils {

    /**
     * @param FileName, a file representing a co-appearance list
     * @return An undirected graphs representing the co-appearance of actors
     */
    public static SimpleGraph<String, DefaultEdge> load(String fileName) {
        SimpleGraph<String, DefaultEdge> graph =
                new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        File file = new File(fileName);
        try {
            for (String line : Files.readLines(file, Charsets.UTF_8)) {
                String[] parts = line.split("\t");

                String actor = parts[0];
                String[] coActors = parts[1].split(", ");

                for (String coActor : coActors) {
                    Graphs.addEdgeWithVertices(graph, actor, coActor);
                }
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return graph;
    }

}
