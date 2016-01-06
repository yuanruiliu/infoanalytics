package ix.lab02.degdist;

import org.apache.hadoop.mapreduce.Job;


/**
 * This job combines the two steps of the computation of the degree distribution.
 */
public class WikipediaDegreeDistribution {

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println(String.format("Usage: %s <input path> <output path>",
                    WikipediaDegreeDistribution.class.getName()));
            System.exit(-1);
        }

        String inputFolder = args[0];
        String outputFolder = args[1];
        String intermediateFolder = outputFolder + "-tmp"; // output folder for the first job

        Job job1 = NeighborsSet.getJob(inputFolder, intermediateFolder);
        Job job2 = DegreeDistribution.getJob(intermediateFolder, outputFolder);

        job1.waitForCompletion(true);
        job2.waitForCompletion(true);
    }

}
