package ix.lab03.extraction;

import org.apache.hadoop.mapreduce.Job;

/** Performs movie cast extraction and co-actor graph on the IMDb dataset in one command. */
public class IMDbCoActorGraph {

    /**
     * This job outputs the co-appearance list of actors.
     * - These co-appearances happened in the period [startingYear, endingYear]
     * - The input is a file having rows in the format : ActorName, MovieTitle
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println(String.format("Usage: %s <input path> <output path> "
                    + "<starting year> <ending year>", IMDbCoActorGraph.class.getName()));
            System.exit(-1);
        }

        String inputFolder = args[0];
        String outputFolder = args[1];
        String intermediateFolder = outputFolder + "-tmp"; // output folder for the first job

        int startingYear = Integer.parseInt(args[2]);
        int endingYear = Integer.parseInt(args[3]);

        Job job1 = MovieCast.getJob(inputFolder, intermediateFolder);
        Job job2 = CoActorGraph.getJob(intermediateFolder, outputFolder,
                startingYear, endingYear);

        job1.waitForCompletion(true);
        job2.waitForCompletion(true);
    }

}
