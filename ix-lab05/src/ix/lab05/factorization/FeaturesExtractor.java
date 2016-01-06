package ix.lab05.factorization;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.mahout.common.Pair;
import org.apache.mahout.math.VectorWritable;

import ix.utils.LabUtils;

/** Extract features in a sequence file to a CSV file. */
public final class FeaturesExtractor {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println(String.format(
                    "Usage: %s /path/to/seqfile", FeaturesExtractor.class.getName()));
            return;
        }
        Path path = new Path(args[0]);
        Configuration conf = new Configuration();

        for (Pair<IntWritable, VectorWritable> pair :
                LabUtils.readSequence(path, conf)) {
            int id = pair.getFirst().get();
            System.out.print(id);

            for (int i = 0; i < pair.getSecond().get().size(); ++i) {
                double coord = pair.getSecond().get().getElement(i).get();
                System.out.print(String.format(",%.3f", coord));
            }

            System.out.println();
        }
    }
}
