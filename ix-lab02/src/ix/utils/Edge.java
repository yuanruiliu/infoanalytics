package ix.utils;

import java.util.StringTokenizer;


/** Class representing an edge in the Wikipedia articles graph */
public class Edge {

    /** Separators for parsing the edge from a text file */
    private static final String SEPARATORS = "|\t\n";

    /** Name of the source article of the edge. */
    public String sourceArticleName;

    /** Name of the destination article of the edge */
    public String destinationArticleName;

    /** Weight of the edge. */
    public Long weight;

    /** Parses an edge from a line of the following form: "sourceNode|destNode	edgeWeight" */
    public void parse(String line) {
        // separate line into three parts
        StringTokenizer wordIterator = new StringTokenizer(line, SEPARATORS);

        this.sourceArticleName = wordIterator.nextToken();
        this.destinationArticleName = wordIterator.nextToken();
        this.weight = Long.parseLong(wordIterator.nextToken());
    }

}