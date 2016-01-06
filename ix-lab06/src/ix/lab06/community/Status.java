package ix.lab06.community;

import ix.lab06.utils.WeightedGraph;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Class representing the status of an iteration of the Louvain method. It
 * contains various information about the different communities, and their
 * nodes.
 */
public class Status {
    /**
     * Maximum number of iterations over the nodes in the first step of Louvain
     * method.
     */
    @SuppressWarnings("unused")
    private final int PASS_MAX = 100;

    /**
     * Graph.
     */
    private WeightedGraph graph;

    /**
     * In this HashMap: Key: node & Value: community of that node.
     */
    private Map<String, Integer> nodesCommunity = new TreeMap<String, Integer>();

    /**
     * Sum of the degrees of the nodes inside each community.
     */
    private Map<Integer, Long> communitiesDegrees = new TreeMap<Integer, Long>();

    /**
     * Sum of the weights of the edges which are inside each community. An edge
     * is inside a community if both of its vertices belong to that community.
     */
    private Map<Integer, Long> communitiesInternalWeights = new TreeMap<Integer, Long>();

    /**
     * Initialize elements of class Status. Assign each node to a single
     * community.
     * 
     * @param gwl
     */
    public Status(WeightedGraph graph) {
        // assign each node to its own community
        int communityId = 0;
        Map<String, Integer> nodeCommunities = new TreeMap<String, Integer>();
        for (String u : graph.getNodes()) {
            nodeCommunities.put(u, communityId++);
        }
        this.graph = graph;
        this.nodesCommunity = nodeCommunities;
        this.computeCommunityStats();
    }

    /**
     * Initialize elements of class Status. Assign each node to the community
     * specified in the nodeCommunities.
     * 
     * @param gwl
     * @param nodeCommunities
     */
    public Status(WeightedGraph graph, Map<String, Integer> nodeCommunities) {
        this.graph = graph;
        this.nodesCommunity = nodeCommunities;
        this.computeCommunityStats();
    }

    /**
     * Computes the sum of degrees and internal weight of each community. These
     * quantities are useful for the computation of the modularity of the graph.
     */
    protected void computeCommunityStats() {
        this.communitiesDegrees.clear();
        this.communitiesInternalWeights.clear();

        for (String u : graph.getNodes()) {
            // get node community
            int nodeCommunity = this.nodesCommunity.get(u);

            // add node degree to total community degree
            long communityDegree = (communitiesDegrees
                    .containsKey(nodeCommunity)) ? communitiesDegrees
                    .get(nodeCommunity) : 0;
            communitiesDegrees.put(nodeCommunity,
                    communityDegree + graph.getNodeDegree(u));

            // for each neighbor, add edge weight to inner community weight if
            // neighbor is in the same community
            long innerWeight = (communitiesInternalWeights
                    .containsKey(nodeCommunity)) ? communitiesInternalWeights
                    .get(nodeCommunity) : 0;
            for (Entry<String, Long> edge : graph.getEdgesFrom(u).entrySet()) {
                String v = edge.getKey();
                long weight = edge.getValue();

                int neighborCommunity = this.nodesCommunity.get(v);
                if (neighborCommunity == nodeCommunity) {
                    innerWeight += weight;
                    // add self loops twice, as we divide by two later
                    if (u.equals(v)) {
                        innerWeight += weight;
                    }
                }
            }
            communitiesInternalWeights.put(nodeCommunity, innerWeight);
        }

        // As we added each edge of a community twice, we divide the inner
        // weights by 2
        for (Entry<Integer, Long> e : communitiesInternalWeights.entrySet()) {
            communitiesInternalWeights.put(e.getKey(), e.getValue() / 2);
        }
    }

    /**
     * Compute the modularity of the graph given the current community
     * assignments
     */
    public double modularity() {
        double modularity = 0;
        // TODO
        // Hint: make use of communitiesDegrees and communitiesInternalWeights!
        return modularity;
    }

    /**
     * Computes the sum of the weight of all edges going from the given node to
     * each neighboring community. The community to which the node belongs
     * should also be included, but self loops should not.
     * 
     * @param node
     *            The node
     * @return A map containing the id of all neighbor communities as key, and
     *         the sum of the weight of all edges going from the node to each of
     *         these communities.
     */
    public Map<Integer, Long> weightToNeighboringCommunities(String node) {
        Map<Integer, Long> weights = new TreeMap<Integer, Long>();

        // TODO

        return weights;
    }

    /**
     * Compute one level of communities by running the first phase of Louvain.
     * 
     * Make sure you do not iterate more than PASS_MAX times over all nodes.
     */
    public void assignCommunities() {
        // TODO
        // Hint: use weightToNeighboringCommunities(), removeNodeFromCommunity()
        // and insertNodeIntoCommunity()
    }

    /**
     * Removes a node from its community and updates the different community
     * quantities (degree, internal weight).
     * 
     * @param node
     *            The node to remove from its community.
     * @param weight
     *            The total weight from this node to other nodes of its
     *            community.
     */
    public void removeNodeFromCommunity(String node, long weight) {
        int community = nodesCommunity.get(node);
        communitiesDegrees.put(community, communitiesDegrees.get(community)
                - graph.getNodeDegree(node));
        communitiesInternalWeights.put(community,
                ((communitiesInternalWeights.get(community) - weight - graph
                        .getSelfLoopWeight(node))));
        nodesCommunity.put(node, -1);
    }

    /**
     * Inserts a node into a community, and update the community degree and
     * internal weight.
     * 
     * @param node
     *            The node to add.
     * @param community
     *            The community in which we want to add the node.
     * @param weight
     *            The weight of the edges from the node to the community.
     */
    public void insertNodeIntoCommunity(String node, int community, long weight) {
        nodesCommunity.put(node, community);
        communitiesDegrees.put(community, communitiesDegrees.get(community)
                + graph.getNodeDegree(node));
        communitiesInternalWeights.put(community,
                (communitiesInternalWeights.get(community) + weight + graph
                        .getSelfLoopWeight(node)));
    }

    /**
     * Renumber the communities to have values sequential and starting from 0.
     */
    public void renumberCommunities() {
        Map<Integer, Integer> old2new = new TreeMap<Integer, Integer>();
        Map<Integer, Long> newDegrees = new TreeMap<Integer, Long>();
        Map<Integer, Long> newInternalWeights = new TreeMap<Integer, Long>();

        int nextId = 0;
        for (Entry<String, Integer> e : nodesCommunity.entrySet()) {
            String node = e.getKey();
            int oldCommunity = e.getValue();

            int newCommunity = 0;
            if (old2new.containsKey(oldCommunity)) {
                newCommunity = old2new.get(oldCommunity);
            } else {
                newCommunity = nextId++;
                old2new.put(oldCommunity, newCommunity);
                newDegrees.put(newCommunity,
                        communitiesDegrees.get(oldCommunity));
                newInternalWeights.put(newCommunity,
                        communitiesInternalWeights.get(oldCommunity));
            }

            nodesCommunity.put(node, newCommunity);
        }

        communitiesDegrees = newDegrees;
        communitiesInternalWeights = newInternalWeights;
    }

    /**
     * Gets the status of the next level, containing the graph induced by the
     * communities assignment. This graph has one node per community, with a
     * self loop corresponding to the internal weight of the community, and an
     * edged between each communities with the weight corresponding to the total
     * weight between them.
     * 
     * @return The status representing the induced graph for the next iteration
     *         of louvain.
     */
    public Status getNextLevel() {
        WeightedGraph induced = new WeightedGraph();

        // first, add one node per community, with the self loop
        for (Entry<Integer, Long> e : communitiesInternalWeights.entrySet()) {
            String node = e.getKey().toString();
            long weight = e.getValue();
            induced.addEdge(node, node, weight);
        }

        // then, add the total weight between each community
        for (String node : this.graph.getNodes()) {
            String c1 = this.nodesCommunity.get(node).toString();
            for (Entry<Integer, Long> e : this.weightToNeighboringCommunities(
                    node).entrySet()) {
                String c2 = e.getKey().toString();
                long weight = e.getValue();
                induced.addEdge(c1, c2, weight);
            }
        }

        return new Status(induced);
    }

    public Map<String, Integer> getNodesCommunity() {
        return this.nodesCommunity;
    }
}