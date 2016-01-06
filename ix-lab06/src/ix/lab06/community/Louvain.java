package ix.lab06.community;

import ix.lab06.utils.DataUtils;
import ix.lab06.utils.WeightedGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implements the Louvain community detection algorithm. Each iteration of the
 * algorithm is saved as an object of class Status in the list statusList.
 */
public class Louvain {
    /**
     * Threshold for changes in modularity before stopping louvain iterations.
     */
    @SuppressWarnings("unused")
    private final double CHANGE_MIN = 0.0000001;

    /**
     * List of the status of the communitites as each level of the algorithm.
     */
    private List<Status> statusList = new ArrayList<Status>();

    public Louvain(WeightedGraph g) {
        statusList.add(new Status(g));
    }

    /**
     * Use the two methods assignCommunities() and getNextLevel() to implement
     * the Louvain community detection algorithm. You should stop the iterations
     * as soon as the increase in modularity of one level over the previous one
     * is small than CHANGE_MIN. At each iteration, add the current status to
     * the statusList, in order to be able to get the different levels
     * afterwards.
     */
    public void communityDetection() {
        //TODO
        // Hint: use status.getNextLevel() to get the induced graph for the next iteration of Louvain
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            System.err
                    .format("Usage: %s graph.txt output/communities0.txt output/communitiesFinal.txt",
                            Louvain.class.getName());
            System.exit(-1);
        }

        String fileName = args[0];
        String outputFirst = args[1];
        String outputLast = args[2];

        WeightedGraph graph = WeightedGraph.parse(fileName);

        Louvain louvain = new Louvain(graph);

        louvain.communityDetection();

        // store the community of the nodes obtained from the first level of
        // algorithm.
        Map<String, Integer> comm = louvain.communityAtLevel(0);
        DataUtils.writeNodeCommunities(comm, outputFirst);

        // store the final community of the nodes
        Map<String, Integer> bestcomm = louvain.bestCommunity();
        DataUtils.writeNodeCommunities(bestcomm, outputLast);
    }

    /**
     * Return the communities of the nodes at the given level.
     * 
     * @param level
     *            The level to get.
     * @return The community assignment of each node.
     */
    public Map<String, Integer> communityAtLevel(int level) {
        // get initial communities
        Map<String, Integer> communities = statusList.get(0)
                .getNodesCommunity();

        // at each level, update the community of each node to reflect its assignment
        for (int l = 1; l <= level; l++) {
            Map<String, Integer> newCommunities = statusList.get(l)
                    .getNodesCommunity();
            for (String node : communities.keySet()) {
                communities.put(node,
                        newCommunities.get(String.valueOf(communities.get(node))));
            }
        }

        return communities;
    }

    /**
     * Gets the communities of the graph nodes which try to maximize the
     * modularity using the Louvain heuristics The best communities are obtained
     * from the result of the last iteration of the algorithm.
     * 
     * @return The communities assignment at the last level of Louvain.
     */
    public Map<String, Integer> bestCommunity() {
        return communityAtLevel(statusList.size() - 1);
    }
}
