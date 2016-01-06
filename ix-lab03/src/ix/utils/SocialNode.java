package ix.utils;

import java.util.List;

/**
 * Class representing a member of a social network.
 */
public class SocialNode {

    /** Identifier of the member. */
    public String id;

    /** Age of the member. */
    public int age;

    /** List of identifiers of the neighbors of the member in the graph, i.e., his friends. */
    public List<String> neighbors;

    @Override
    public String toString() {
        StringBuilder neighborsList = new StringBuilder();
        for (String neighbor: this.neighbors) {
            if (neighborsList.length() > 0) {
                neighborsList.append(", ");
            }

            neighborsList.append(neighbor);
        }
        return String.format("Node %s (age: %d,  neighbors: [%s])", this.id, this.age, neighborsList);
    }


}
