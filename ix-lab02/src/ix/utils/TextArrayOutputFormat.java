package ix.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Custom output format that allows mappers/reducers to output arrays of Text objects.
 * 
 * It simply takes the String value of teach Text object, and concatenates them using
 * a given separator.
 *
 */
public class TextArrayOutputFormat extends FileOutputFormat<Text, TextArrayWritable> {

	protected static class TextArrayRecordWriter extends RecordWriter<Text, TextArrayWritable> {

		private DataOutputStream out;
		private String separator;
		private byte[] newline;
		private final String utf8 = "UTF-8";

		public TextArrayRecordWriter(DataOutputStream out, String separator)
				throws UnsupportedEncodingException {
			this.out = out;
			this.separator = separator;
			this.newline = "\n".getBytes(this.utf8);
		}

		public TextArrayRecordWriter(DataOutputStream out)
				throws UnsupportedEncodingException {
			this(out, ", ");
		}

		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			this.out.close();
		}

		private void writeArray(TextArrayWritable array) throws IOException {
			StringBuilder stringValues = new StringBuilder();
			String separator = "";

			for (Writable element : array.get()) {
				stringValues.append(separator).append(element.toString());
				separator = this.separator;
			}
			
			out.write(stringValues.toString().getBytes(this.utf8));
		}

		@Override
		public void write(Text key, TextArrayWritable value) throws IOException,
				InterruptedException {
			if (null != key) {
				this.out.write(String.format("%s\t", key.toString()).getBytes(this.utf8));
			}

			this.writeArray(value);
			
			this.out.write(this.newline);
		}

	}

	@Override
	public RecordWriter<Text, TextArrayWritable> getRecordWriter(TaskAttemptContext job)
			throws IOException, InterruptedException {
		String extension = "";
		Path file = getDefaultWorkFile(job, extension);
		FileSystem fs = file.getFileSystem(job.getConfiguration());
		FSDataOutputStream fileOut = fs.create(file, false);
		return new TextArrayRecordWriter(fileOut);
	}

}
