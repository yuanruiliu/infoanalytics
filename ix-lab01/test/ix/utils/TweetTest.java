package ix.utils;


import static org.junit.Assert.*;

import org.junit.Test;

/**
 * This class contains tests to check that the Tweet class works correctly
 */
public class TweetTest {

	@Test
	public void parseTest() {
		String tweetTxt = "291856748745203712 | 86024102 | franci_de | 3600 | 2013-01-17 10:37:25 | 086752cb03de1d5d | 40.7564 | -73.9631 | ? | Twitter for iPhone | @Krystenritter: Vegas baby http://t.co/Q6VVtxCT I love it when actors are similar to their TV character in real life";
		Tweet t = new Tweet().parse(tweetTxt);

		assertEquals(new Long(291856748745203712l), t.id);
	}

}
