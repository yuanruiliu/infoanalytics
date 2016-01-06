package ix.lab07.utils;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/** Writable used to hold a term-document pair. */
public class TermDocumentPair implements WritableComparable<TermDocumentPair> {

    private static final String SEPARATOR = ":";

    private Text term = new Text();
    private IntWritable docID = new IntWritable();

    public TermDocumentPair() { }

    public TermDocumentPair(String word, int docID) {
        this.term.set(word);
        this.docID.set(docID);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        this.term.write(out);
        this.docID.write(out);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.term.readFields(in);
        this.docID.readFields(in);
    }

    public void set(String word, int docID) {
        this.term.set(word);
        this.docID.set(docID);
    }

    public String getTerm() {
        return this.term.toString();
    }

    public int getDocument() {
        return this.docID.get();
    }

    @Override
    public int compareTo(TermDocumentPair that) {
        int termComp = this.term.compareTo(that.term);
        return termComp != 0 ? termComp : this.docID.compareTo(that.docID);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.docID.hashCode();
        result = prime * result + this.term.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TermDocumentPair other = (TermDocumentPair) obj;
        if (docID == null) {
            if (other.docID != null)
                return false;
        } else if (!docID.equals(other.docID))
            return false;
        if (term == null) {
            if (other.term != null)
                return false;
        } else if (!term.equals(other.term))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return this.getTerm() + SEPARATOR + this.getDocument();
    }



    /**
     * Parses a Text value and extract the term-document pair. Useful when using
     * TermDocumentPair together with a KeyValueTextInputFormat.
     *
     * @param text the Text from which to extract the term-document pair
     * @return a TermDocumentPair parsed from the Text value
     */
    public static TermDocumentPair fromText(Text text) {
        String[] parts = text.toString().split(SEPARATOR);
        String word = parts[0];
        int docID = Integer.parseInt(parts[1]);
        return new TermDocumentPair(word, docID);
    }
}
