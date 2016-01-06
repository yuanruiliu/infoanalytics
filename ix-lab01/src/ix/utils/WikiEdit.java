package ix.utils;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.apache.hadoop.util.StringUtils;

/**
 * Class representing an entry of the Wikipedia edits dataset.
 * 
 * It has a method for parsing its values directly from a line of the dataset.
 * 
 */
public class WikiEdit {

	/**
	 * Id of the edited article
	 */
	public Long articleId;

	/**
	 * Id of the edit
	 */
	public Long id;

	/**
	 * Name of the edited article
	 */
	public String articleName;

	/**
	 * Date and time at which the edit was made
	 */
	public SimpleDateTime timestamp;

	/**
	 * User name of the editor (if not registered, IP address of the editor)
	 */
	public String userName;

	/**
	 * User id of the editor (null if the user was not registered)
	 */
	public Long userId = null;

	/**
	 * Set of categories to which the article belongs after the edit
	 */
	public Set<String> categories = new TreeSet<String>();

	/**
	 * Set of other articles to which the edited one links after the edit.
	 */
	public Set<String> outgoingLinks = new TreeSet<String>();

	/**
	 * Parse a WikiEdit object out of one line of the Wikipedia edits dataset.
	 * 
	 * One line is formatted as follows: REVISION articleId revisionId
	 * articleName timestamp userName userId | CATEGORY cat1 cat2 [...] | MAIN
	 * linkedPage1 linkedPage2 [...]
	 * 
	 * @param line
	 *            One line from the raw Wikipedia edits dataset
	 */
	public void parse(String line) {
		// separate the three main parts (REVISION, CATEGORY and MAIN)
		String[] parts = line.split(" \\| ");

		String[] revision = parts[0].replaceFirst("REVISION ", "").split(" ");

		this.articleId = Long.parseLong(revision[0]);
		this.id = Long.parseLong(revision[1]);
		this.articleName = revision[2];
		this.timestamp = SimpleDateTime.parseFromWikiEdit(revision[3]);

		// handle IP address as user name
		if (revision[4].startsWith("ip:")) {
			this.userName = revision[4].replaceFirst("ip:", "");
			this.userId = null;
		} else {
			this.userName = revision[4];
			this.userId = Long.parseLong(revision[5]);
		}

		this.categories.clear();
		if (!parts[1].trim().equals("CATEGORY")) {
			this.categories.addAll(Arrays.asList(parts[1]
					.replaceFirst("CATEGORY", "").trim().split("\\s+")));
		}

		this.outgoingLinks.clear();
		if (!parts[2].trim().equals("MAIN")) {
			this.outgoingLinks.addAll(Arrays.asList(parts[2]
					.replaceFirst("MAIN", "").trim().split("\\s+")));
		}
	}

	@Override
	public String toString() {
		return String
				.format("Edit %d of %s by %s on %s:\nCategories: %s\nOutgoing links: %s",
						this.id, this.articleName, this.userName,
						this.timestamp,
						StringUtils.join(", ", this.categories),
						StringUtils.join(", ", this.outgoingLinks));
	}

}
