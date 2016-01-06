package ix.lab01.wikipedia;

import org.apache.hadoop.mapreduce.Job;

/**
 * This job combines the LastEdit and ArticleLinks jobs to extract the graph of
 * wikipedia articles from the raw dataset of wikipedia edits.
 * 
 */
public class ArticlesGraphExtraction {

	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: ix.lab01.wikipedia.ArticlesGraphExtraction <input path> <output path>");
			System.exit(-1);
		}

		String inputFolder = args[0];
		String outputFolder = args[1];
		String intermediateFolder = outputFolder + "-tmp"; // output folder for the first job

		Job job1 = LastEdit.getJob(inputFolder, intermediateFolder);
		Job job2 = ArticleLinks.getJob(intermediateFolder, outputFolder);

		job1.waitForCompletion(true);
		job2.waitForCompletion(true);
	}

}
