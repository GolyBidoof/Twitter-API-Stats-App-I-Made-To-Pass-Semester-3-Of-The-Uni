import java.awt.FontFormatException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import twitter4j.Status;
import twitter4j.TwitterException;

public class HandleTweetStats {

	static String currentWeek = null;
	
	public static void calcStats() throws TwitterException, FontFormatException, IOException, ClassNotFoundException {

		/*
		 * 
		 * This file is heavily WIP ATM
		 *
		 */

		List<Integer> lengthsOfTweets = new ArrayList<Integer>();
		List<String> callPeoples = new ArrayList<String>();
		List<Integer[]> typesOfSignsInTweetsList = new ArrayList<Integer[]>();
		List<String> datesWeeks = new ArrayList<String>();
		
		for (Status status : ImportingTweets.statuses) {
			ObjectIO.saveObjectToFile(status);
		}
		
		System.out.println("Updated " + ImportingTweets.statuses.size() + " statuses in the database.");
		ImportingTweets.statuses.clear();
		ObjectIO.loadObjectFromFile();
		System.out.println("Imported the user database successfully with " + ImportingTweets.statuses.size() + " statuses.");
		AnalyzeLang.analyzeLang();
		
		for (Status status : ImportingTweets.statuses) {
			
			int length = status.getText().length();
			lengthsOfTweets.add(length);
			//System.out.println(status.getText());

			// Look for @ signs
			Pattern pattern = Pattern.compile("@(\\S+)");
			Matcher matcher = pattern.matcher(status.getText());
			while (matcher.find()) {
				callPeoples.add(matcher.group(1));
				//System.out.println(matcher.group(1));
			}

			// Parse string byte by byte
			byte[] arrayOfBytes = status.getText().getBytes();

			Integer typesOfSignsInTweets[] = { 0, // Capital letters
					0, // Lowercase letters
					0, // Numbers
					0, // Spaces
					0 // Other symbols
			};

			for (int i = 0; i < arrayOfBytes.length; i++) {
				if (arrayOfBytes[i] >= '0' && arrayOfBytes[i] <= '9')
					typesOfSignsInTweets[2]++;
				else if (arrayOfBytes[i] >= 'a' && arrayOfBytes[i] <= 'z')
					typesOfSignsInTweets[1]++;
				else if (arrayOfBytes[i] >= 'A' && arrayOfBytes[i] <= 'Z')
					typesOfSignsInTweets[0]++;
				else if (arrayOfBytes[i] == ' ')
					typesOfSignsInTweets[3]++;
				else
					typesOfSignsInTweets[4]++;
			}
			typesOfSignsInTweetsList.add(typesOfSignsInTweets);
			//System.out.println(Arrays.toString(typesOfSignsInTweets));
			
			//Get createdAtDates
			Date createdAtDate = status.getCreatedAt();
			
			//DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			DateFormat week = new SimpleDateFormat("yyyyww");
			//String reportDate = df.format(createdAtDate);
			String reportWeek = week.format(createdAtDate);

			//String[] reportDateWeek = {reportDate, reportWeek};
			datesWeeks.add(reportWeek);
		}
		Date now = new Date(); 
		DateFormat nowWeek = new SimpleDateFormat("yyyyww");
		currentWeek = nowWeek.format(now);
		
		//for (Status status : AnalyzeLang.likes) {
			
		//}

		Integer totalTypesOfSignsInATweet[] = { 0, 0, 0, 0, 0 };
		for (Integer[] entries : typesOfSignsInTweetsList) {
			for (int i = 0; i < 5; i++)
				totalTypesOfSignsInATweet[i] += entries[i];
		}

		// int totalSum = 0, average = 0;
		// for (int i=0; i<lengthsOfTweets.size(); i++) {
		// totalSum+=lengthsOfTweets.indexOf(i);
		// }
		// average = totalSum / lengthsOfTweets.size();
		Map<String, Integer> instancesOfNicks = new HashMap<String, Integer>();
		if (!callPeoples.isEmpty()) {
			for (int i = 0; i < callPeoples.size(); i++) {
				Integer freq = instancesOfNicks.get(callPeoples.get(i));
				instancesOfNicks.put(callPeoples.get(i), (freq == null) ? 1 : freq + 1);
			}
		}
		Map<String, Integer> instancesOfNicksSorted = instancesOfNicks.entrySet().stream()
				.sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		Map<String, Long> datesWeeksOccurences =
			    datesWeeks.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
				
		
		//System.out.println(instancesOfNicksSorted.size() + " people tweeted at:");
		//System.out.println(instancesOfNicksSorted);
		// TwitterAPIPostingActions.writeATextTweet("In the person's last 100 Tweets, "
		// + totalSum + " characters were written with " + average + " average
		// characters a Tweet");
		ChartGeneration.charactersInTweets(totalTypesOfSignsInATweet, typesOfSignsInTweetsList);
		ChartGeneration.mentionPieChart(instancesOfNicksSorted);
		ChartGeneration.datesLineChart(datesWeeksOccurences);
		System.out.println("Finished the execution of the program");
	}
}
