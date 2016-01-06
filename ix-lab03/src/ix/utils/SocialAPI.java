package ix.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;


/**
 * This class allows you to call the API of a social network, to get information about a node (age, list of neighbors).
 *
 */
public class SocialAPI {

    public static final int FACEBOOK = 1;
    public static final int AGES = 2;
    public static final int DIRECTIONS = 3;

    public static final String SEED_U = "03afdbd66e7929b125f8597834fa83a4"; // corresponds to hash(1)
    public static final String SEED_V = "99bfaf293cf48f7cecfacf98feb17d60";  // corresponds to hash (60000)
    public static final String SEED_W = "753b1ab8ed5ef9b55cb07a9e8677ec82"; // corresponds to hash(150000)

    private static final String API_URL = "http://icsil1-prj-01.epfl.ch:5050";

    /**
     * Loads the content at the given url into an String.
     * @param contentUrl URL of the content to load
     * @return The content if found, null otherwise
     */
    private String getContent(String contentUrl) {
        try {
            // init url reader
            URL url = new URL(contentUrl);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            StringBuilder content = new StringBuilder();
            String inputLine;

            // read page content line by line
            while ((inputLine = reader.readLine()) != null) {
                content.append(inputLine);
            }

            // return content
            return content.toString();
        }
        // if any problem occurs, return null
        catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            return null;
        }
    }

    /**
     * Gets the information of the member of the social network with the given id.
     * @param graphId Identifier of the graph
     * @param nodeId Identifier of the member of the social network
     * @return An object describing the member if found, null otherwise
     */
    public SocialNode getNode(int graphId, String nodeId) {
        // build url
        String url = String.format("%s/%d/%s", API_URL, graphId, nodeId);

        // get content
        String apiResult = this.getContent(url);

        if (apiResult != null) {
            // parse json response to object
            Gson jsonParser = new Gson();
            SocialNode node = jsonParser.fromJson(apiResult, SocialNode.class);

            return node;
        } else {
            return null;
        }
    }

    /**
     * Sample usage of the API.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        SocialAPI api = new SocialAPI();

        int graphId = SocialAPI.FACEBOOK;
        String nodeId = SocialAPI.SEED_W;

        SocialNode node = api.getNode(graphId, nodeId);
        //TODO
        System.out.println(node);
    }

}
