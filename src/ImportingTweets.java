import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

public class ImportingTweets {

	static List<Status> statuses;
	
	public static void handleStatuses(int number) {
		Paging p = new Paging();
		p.setCount(number);
		try {
			statuses = InitializeTwitterInstance.twitter.getUserTimeline(MainClass.nickOfAPerson, p);
		}
		catch(TwitterException e) {
			System.out.println("The following user does not exist!");
			System.exit(0);
		}
	}

}
