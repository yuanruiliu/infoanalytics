package ix.utils;

/**
 * Class representing a moment in time, defined by a date and a time.
 */
public class SimpleDateTime implements Comparable<SimpleDateTime> {

	public int year;
	public int month;
	public int day;
	
	public int hours;
	public int minutes;
	public int seconds;
	
	public SimpleDateTime(int year, int month, int day, int hours, int minutes, int seconds) {
		this.year = year;
		this.month = month;
		this.day = day;
		
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	public SimpleDateTime(String year, String month, String day, String hours, String minutes, String seconds) {
		this(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day),
				Integer.parseInt(hours), Integer.parseInt(minutes), Integer.parseInt(seconds));
	}
	
	/**
	 * Checks whether this datetime corresponds to the same day as the given datetime.
	 * @param other
	 * @return True if they both are on the same day (regardless of the time).
	 */
	public boolean isSameDay(SimpleDateTime other) {
		return (this.year == other.year) && (this.month == other.month) && (this.day == other.day);
	}
	
	@Override
	public String toString() {
		return String.format("%d-%02d-%02d %02d:%02d:%02d", year, month, day, hours, minutes, seconds);
	}

	@Override
	public int compareTo(SimpleDateTime o) {
		return this.toString().compareTo(o.toString());
	}
	
	/**
	 * Parse a SimpleDate object from a timestamp taken from the tweets dataset.
	 * @param timestamp (e.g. "2013-01-17 10:37:44")
	 * @return A SimpleDate object representing that timestamp
	 */
	public static SimpleDateTime parseFromTweet(String timestamp) {
		String[] parts = timestamp.split(" ");
		String[] day = parts[0].split("-");
		String[] time = parts[1].split(":");
		
		return new SimpleDateTime(day[0], day[1], day[2], time[0], time[1], time[2]);
	}

	/**
	 * Parse a SimpleDate object from a timestamp taken from the Wikipedia edits dataset.
	 * @param timestamp (e.g. "2002-04-03T07:36:30Z")
	 * @return A SimpleDate object representing that timestamp
	 */
	public static SimpleDateTime parseFromWikiEdit(String timestamp) {
		String[] parts = timestamp.replace("T", " ").replace("Z", "").split(" ");
		String[] day = parts[0].split("-");
		String[] time = parts[1].split(":");
		
		return new SimpleDateTime(day[0], day[1], day[2], time[0], time[1], time[2]);
	}

}
