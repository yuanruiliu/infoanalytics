package ix.utils;


import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class contains tests to check that the SimpleDateTime class works correctly
 */
public class SimpleDateTimeTest {

	@Test
	public void parseFromTweetTest() {
		String timestamp = "2013-01-17 04:37:44";

		SimpleDateTime date = SimpleDateTime.parseFromTweet(timestamp);

		assertEquals(2013, date.year);
		assertEquals(1, date.month);
		assertEquals(17, date.day);
		assertEquals(4, date.hours);
		assertEquals(37, date.minutes);
		assertEquals(44, date.seconds);

		assertEquals(timestamp, date.toString());
	}

	@Test
	public void parseFromWikiEditTest() {
		String timestamp = "2002-04-03T07:36:30Z";

		SimpleDateTime date = SimpleDateTime.parseFromWikiEdit(timestamp);

		assertEquals(2002, date.year);
		assertEquals(4, date.month);
		assertEquals(3, date.day);
		assertEquals(7, date.hours);
		assertEquals(36, date.minutes);
		assertEquals(30, date.seconds);

		assertEquals("2002-04-03 07:36:30", date.toString());
	}
}
