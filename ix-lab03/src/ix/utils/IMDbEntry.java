package ix.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/** Small helper class to parse a line of an IMDb file. */
public class IMDbEntry {

    /** Regular expression to extract the actor, movie and year. */
    private static final Pattern LINE_REGEX = Pattern.compile("([^,]+),(.+) \\((\\d{4})\\)");

    public String actor;
    public String movie;
    public int year;

    public boolean parse(String line) {
        Matcher m = LINE_REGEX.matcher(line);
        if (m.matches()) {
            this.actor = m.group(1).trim();
            this.movie = m.group(2).trim();
            this.year = Integer.parseInt(m.group(3));
            return true;
        } else {
            return false;
        }
    }

    public String getTitle() {
        return String.format("%s (%d)", this.movie, this.year);
    }

    @Override
    public String toString() {
        return String.format("%s (%d) : %s", this.movie, this.year, this.actor);
    }

}
