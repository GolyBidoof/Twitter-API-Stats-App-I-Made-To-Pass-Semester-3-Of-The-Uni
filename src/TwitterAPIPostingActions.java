import java.io.File;

import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterAPIPostingActions {

	public static void writeATextTweet(String text) throws TwitterException {
		Twitter twitter = InitializeTwitterInstance.twitter;
		Status status2 = twitter.updateStatus(text);
		System.out.println("Successfully wrote a Tweet containing [" + status2.getText() + "].");
	}

	public static void writeATweetContainingAPic(String text, File a) throws TwitterException {
		StatusUpdate status = new StatusUpdate(text);
		status.setMedia(a);
		Status status2 = InitializeTwitterInstance.twitter.updateStatus(status);
		System.out.println("Wrote a Tweet with a picture: [" 
				+ status2.getText().substring(0,Math.min(status2.getText().length(), 50)) 
				+ "...] and a chart "
				+ a.getName() 
				+ ". URL: "
				+ status2.getText().substring(status2.getText().lastIndexOf("https")) 
				);
	}
}
