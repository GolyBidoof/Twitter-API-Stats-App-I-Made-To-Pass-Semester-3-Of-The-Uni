import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterAPIPostingActions {
	
	public static void writeATextTweet(String text) throws TwitterException {
		Twitter twitter = loginTest.twitter;
		Status status2 = twitter.updateStatus(text);
	    System.out.println("Successfully wrote a Tweet containing [" + status2.getText() + "].");
	}
	
	public static void writeATweetContainingAPic(String text, File a) throws TwitterException {
		StatusUpdate status = new StatusUpdate(text);
		status.setMedia(a);
		Status status2 = loginTest.twitter.updateStatus(status);
		System.out.println("Successfully wrote a Tweet with a picture: [" + status2.getText() + "] and a media file " + a.getName() + ".");
	}
}
	