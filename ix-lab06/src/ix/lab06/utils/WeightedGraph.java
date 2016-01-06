package ix.lab06.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class WeightedGraph {
    protected Map<String, Map<String, Long>> adjacencyList = new TreeMap<String, Map<String, Long>>();

    public void addNode(String u) {
        if (!this.adjacencyList.containsKey(u)) {
            this.adjacencyList.put(u, new TreeMap<String, Long>());
        }
    }

    private void addEdgeWeight(String u, String v, long weight) {
        Long w = this.adjacencyList.get(u).get(v);
        long currentWeight = (w != null) ? w : 0;
        this.adjacencyList.get(u).put(v, currentWeight + weight);
    }

    public void addEdge(String u, String v, long weight) {
        this.addNode(u);
        this.addEdgeWeight(u, v, weight);
        if (!u.equals(v)) {
            this.addNode(v);
            this.addEdgeWeight(v, u, weight);
        }
    }

    /**
     * Gets the set of nodes of the graph.
     * @return Set of nodes.
     */
    public Set<String> getNodes() {
        return this.adjacencyList.keySet();
    }
    
    /**
     * Gets the weighted out degree of u.
     * @param u Node to get the degree of.
     * @return The weighted degree of u.
     */
    public long getNodeDegree(String u) {
        long weight = 0;
        for (Long w: this.adjacencyList.get(u).values()) {
            weight += w;
        }
        return weight;
    }
    
    /**
     * Get the weight of the self loop of the given node, if existing.
     * If not, 0 is returned.
     * @param u Node to get the self loop of.
     * @return The weight of the self loop of u if it exsits, 0 otherwise.
     */
    public long getSelfLoopWeight(String u) {
        if (this.adjacencyList.get(u).containsKey(u)) {
            return this.adjacencyList.get(u).get(u);
        } else {
            return 0;
        }
    }

    /**
     * Gets the edges leaving from u, with their weight.
     * @param u Node from which the edges leave.
     * @return Map of edges
     */
    public Map<String, Long> getEdgesFrom(String u) {
        return this.adjacencyList.get(u);
    }

    /**
     * Get the total weight of the graph.
     * @return Total weight
     */
    public long getTotalWeight() {
        long totalWeight = 0;
        for (Entry<String, Map<String, Long>> e : this.adjacencyList.entrySet()) {
            String u = e.getKey();
            
            for (Entry<String, Long> edge : e.getValue().entrySet()) {
                String v = edge.getKey();
                long weight = edge.getValue();
                
                totalWeight += weight;
                
                // count self loops twice, as we divide by 2 in the end
                if (u.equals(v)) {
                    totalWeight += weight;
                }
            }
        }
        return totalWeight / 2;
    }

    /**
     * Parse a weighted graph from a text file, containing an edge list. Each
     * line of the file should be of the for "u    v" or "u    v   4", i.e.,
     * source id, destination id, edge weight. If no weight is provided, a
     * weight of 1 is set.
     * 
     * @param fileName
     *            Path to the file to load from.
     * @return A weighted graph.
     * @throws IOException
     */
    public static WeightedGraph parse(String fileName) throws IOException {
        String line;
        WeightedGraph graph = new WeightedGraph();

        BufferedReader br = new BufferedReader(new FileReader(fileName));
        while ((line = br.readLine()) != null) {
            StringTokenizer wordIterator = new StringTokenizer(line, " |\t\n");

            String u = wordIterator.nextToken();
            String v = wordIterator.nextToken();
            long weight = (wordIterator.hasMoreTokens()) ? Long
                    .parseLong(wordIterator.nextToken()) : 1;

            graph.addEdge(u, v, weight);
        }
        br.close();

        return graph;
    }
}
