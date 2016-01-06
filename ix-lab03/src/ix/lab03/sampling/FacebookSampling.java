package ix.lab03.sampling;

import java.util.Random;

import ix.utils.SocialAPI;
import ix.utils.SocialNode;

@SuppressWarnings("unused")
public class FacebookSampling {

    public static int N = 10000;
    public static int GRAPH = SocialAPI.FACEBOOK;

    public static void main(String[] args) {
        SocialAPI api = new SocialAPI();

        // TODO Complete.
        // Hint: you might find java.util.Random useful.
        SocialNode pnt = api.getNode(GRAPH, SocialAPI.SEED_U);
        double sum=0;
        
        for(int i=0;i<N;i++){
        	sum+= pnt.age;
        	Random rand = new Random();
        	String tempID = pnt.neighbors.get(rand.nextInt(pnt.neighbors.size()));
        	pnt=api.getNode(GRAPH, tempID);
        }
        System.out.printf("The average age of Facebook User is %.2d.", sum/N );
    }
}
