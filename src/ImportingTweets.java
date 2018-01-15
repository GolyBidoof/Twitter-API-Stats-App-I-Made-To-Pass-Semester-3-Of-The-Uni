import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

public class ImportingTweets {

	static List<Status> statuses;
	static List<Status> likes;
	public static void handleStatuses(int number) throws TwitterException {
		Paging p = new Paging();
		p.setCount(number);
		statuses = InitializeTwitterInstance.twitter.getUserTimeline(MainClass.nickOfAPerson, p);
		//likes = InitializeTwitterInstance.twitter.getFavorites(nickOfAPerson, p);
	}

}
