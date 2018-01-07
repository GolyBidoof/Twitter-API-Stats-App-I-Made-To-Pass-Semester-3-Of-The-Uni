import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import twitter4j.Status;
import twitter4j.TwitterException;

public class HandleTweetStats {
	 public static void calcStats() throws TwitterException, FontFormatException, IOException {
		 //Tweet Lengths
		 List<Integer> lengthsOfTweets = new ArrayList<Integer>();
		 //List<Integer> retweetAmounts = new ArrayList<Integer>();
		 List<String> callPeoples = new ArrayList<String>();
		 for (Status status : AnalyzeLang.statuses) {
			 int length = status.getText().length();
			 //int retweets = status.getRetweetCount();
			 lengthsOfTweets.add(length);
			 System.out.println(status.getText());
			 
			 Pattern pattern = Pattern.compile("@(\\S+)");
			 Matcher matcher = pattern.matcher(status.getText());
			 while (matcher.find()) {
				 callPeoples.add(matcher.group(1));
			     System.out.println(matcher.group(1));
			 }
		 }
		 //int totalSum = 0, average = 0;
		 //for (int i=0; i<lengthsOfTweets.size(); i++) {
			 //totalSum+=lengthsOfTweets.indexOf(i);
		 //}
		 //average = totalSum / lengthsOfTweets.size();
		 Map<String, Integer> instancesOfNicks = new HashMap<String, Integer>();
		 if (!callPeoples.isEmpty()) {
	 		for (int i=0; i<callPeoples.size(); i++) {
	 			 Integer freq = instancesOfNicks.get(callPeoples.get(i));
	             instancesOfNicks.put(callPeoples.get(i), (freq == null) ? 1 : freq + 1);
	 		}
		 }
		 Map<String, Integer> instancesOfNicksSorted =
	                instancesOfNicks.entrySet().stream()
	                        .sorted(Map.Entry.<String, Integer> comparingByValue().reversed())
	                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                                (e1, e2) -> e2, LinkedHashMap::new));
		 
		 System.out.println(instancesOfNicksSorted.size() + " people tweeted at:");
	     System.out.println(instancesOfNicksSorted);
		 //TwitterAPIPostingActions.writeATextTweet("In the person's last 100 Tweets, " + totalSum + " characters were written with " + average + " average characters a Tweet");
		 JFreeChartBarColors.charactersInTweets(lengthsOfTweets);
		 JFreeChartBarColors.mentionPieChart(instancesOfNicksSorted);
	 }
}
