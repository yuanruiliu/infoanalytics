package ix.lab05.processing;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.thirdparty.guava.common.base.Charsets;
import org.apache.mahout.common.iterator.FileLineIterable;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern;


/**
 * Parse the raw files provided by Netflix for their Prize.
 *
 * This class merges the ratings in the Netflix dataset (more precisesly, in
 * the folder training_set) into one single, big file where every line contains
 * a self-contained rating of the form:
 *
 * userID<TAB>movieID<TAB>rating<NEWLINE>
 */
@SuppressWarnings("unused")
public final class NetflixParser {

    private static final String TAB = "\t";
    private static final String NEWLINE = "\n";
    private static final String separator = ",";
    private static final String MOVIE_DENOTER = ":";

    /**
     * Parses a single file from the Netflix dataset, and appends the ratings
     * it contains to the Writer instance.
     *
     * @param lines an iterable containing the lines of the file to parse
     * @param writer the Writer used to append the ratings
     * @throws IOException
     */
	public static void processFile(Iterable<String> lines, Writer writer) throws IOException {
        String movieID = null;
        String userID = null;
        String rating = null;

        //TODO: extract the information from lines and write them to writer in the correct format
        boolean firstLineRead = false;
        for (String value:lines) {
           
            if (firstLineRead) {
            	String[] tokens = value.split(separator);
            	userID = tokens[0];
            	rating = tokens[1];
            	writer.write(userID + TAB + movieID + TAB + rating + NEWLINE);
            } else {
              movieID = value.replaceAll(MOVIE_DENOTER, "");
              firstLineRead = true;
            }
          }  
    }


    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.err.println(String.format(
                    "Usage: %s /path/to/training_set /path/to/output.tsv [ratings.tsv ...]",
                    NetflixParser.class.getName()));
            return;
        }

        Path inputDir = new Path(args[0]);
        Path output = new Path(args[1]);

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        FSDataOutputStream outputStream = fs.create(output);
        Writer writer = new BufferedWriter(
                new OutputStreamWriter(outputStream, Charsets.UTF_8));

        int count = 0;
        System.out.println("Processing the Netflix dataset...");
        for (FileStatus status : fs.listStatus(inputDir)) {
            FSDataInputStream stream = fs.open(status.getPath());
            processFile(new FileLineIterable(stream), writer);
            stream.close();
            ++count;
            if (count % 500 == 0) {
                System.out.println(String.format("Processed %d files...", count));
            }
        }
        System.out.println(String.format("Done (processed %d files)", count));

        // Process the additional ratings files.
        System.out.print("Processing additional rating files... ");
        for (int i = 2; i < args.length; ++i) {
            InputStream stream = new FileInputStream(args[i]);
            for (String line : new FileLineIterable(stream)) {
                writer.write(line + NEWLINE);
            }
        }
        writer.close();
        System.out.println("Done.");
    }
}
