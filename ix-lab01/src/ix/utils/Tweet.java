package ix.utils;


/**
 * Class representing a tweet from the Tweets dataset.
 * 
 * It has a method for parsing its values directly from a line of the dataset.
 *
 */
public class Tweet {
	
	/**
	 * Id of the tweet
	 */
	public Long id;
	
	/**
	 * User id of the author of the tweet
	 */
	public Long userId;
	
	/**
	 * User name of the author of the tweet
	 */
	public String userName;
	
	/**
	 * Time different (in seconds) of the author's timezome compared to UTC (not available if set to null)
	 */
	public Integer utcOffset = null;
	
	/**
	 * UTC date and time at which the tweet was created
	 */
	public SimpleDateTime createdAt;
	
	/**
	 * Id of the place to which the tweet was associated (not available if set to null)
	 */
	public String placeId = null;
	
	/**
	 * Latitude of the location from which this tweet was created (not available if set to null)
	 */
	public Float latitude = null;

	/**
	 * Latitude of the location from which this tweet was created (not available if set to null)
	 */
	public Float longitude = null;
	
	/**
	 * Id of the tweet to which this tweet is a reply (not available if set to null)
	 */
	public Long inReplyTo = null;
	
	/**
	 * Description of the source which was used to publish this tweet
	 */
	public String source;
	
	/**
	 * Content of the tweet
	 */
	public String text;
	
	
	/**
	 * Parse a tweet out of a line from the Tweets dataset.
	 * 
	 * One line is formatted as follows:
	 * 
	 * id | userId | screenName | utcOffset | createdAt | placeId | latitude | longitude | inReplyTo | sourceName | text
	 * 
	 * The following fields may not be available, in which case they are set to '?':
	 * - utfOffset
	 * - placeId
	 * - latitude
	 * - longitude
	 * - inReplyTo
	 * 
	 * @param line Line read from the input file
	 * @return The tweet itself, for fluent style
	 */
	public Tweet parse(String line) {
		String[] parts = line.split(" \\| ");
		
		this.id = Long.parseLong(parts[0]);
		this.userId = Long.parseLong(parts[1]);
		this.userName = parts[2];
		this.utcOffset = parts[3].equals("?") ? null : Integer.parseInt(parts[3]);
		this.createdAt = SimpleDateTime.parseFromTweet(parts[4]);
		this.placeId = parts[5].equals("?") ? null : parts[5];
		this.latitude = parts[6].equals("?") ? null : Float.parseFloat(parts[6]);
		this.longitude = parts[7].equals("?") ? null : Float.parseFloat(parts[7]);
		this.inReplyTo = parts[8].equals("?") ? null : Long.parseLong(parts[8]);
		this.source = parts[9];
		this.text = parts[10];
		
		return this;
	}

	@Override
	public String toString() {
		return String.format("[%s] @%s (via %s) : %s", this.createdAt, this.userName, this.source, this.text);
	}
}
