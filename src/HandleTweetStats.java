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

	public static void setCurrentWeek() {
		Date now = new Date();
		DateFormat nowWeek = new SimpleDateFormat("yyyyww");
		currentWeek = nowWeek.format(now);
	}

	public static void performIO() throws IOException, ClassNotFoundException {
		for (Status status : ImportingTweets.statuses) {
			ObjectIO.saveObjectToFile(status);
		}

		System.out.println("Updated " + ImportingTweets.statuses.size() + " statuses in the database.");
		ImportingTweets.statuses.clear();
		ObjectIO.loadObjectFromFile();
		System.out.println(
				"Imported the user database successfully with " + ImportingTweets.statuses.size() + " statuses.");
	}

	public static void calcStats() throws TwitterException, FontFormatException, ClassNotFoundException, IOException {
		List<Integer> lengthsOfTweets = new ArrayList<Integer>();
		List<String> callPeoples = new ArrayList<String>();
		List<Integer[]> typesOfSignsInTweetsList = new ArrayList<Integer[]>();
		List<String> datesWeeks = new ArrayList<String>();
		Boolean verifyIfQuery = (MainClass.query != null && !MainClass.query.isEmpty());

		performIO();

		for (Status status : ImportingTweets.statuses) {
			if (verifyIfQuery) {
				String text = status.getText();
				if (!text.toLowerCase().contains(MainClass.query.toLowerCase())) {
					continue;
				}
			}

			int length = status.getText().length();
			lengthsOfTweets.add(length);

			// Look for @ signs
			Pattern pattern = Pattern.compile("@(\\S+)");
			Matcher matcher = pattern.matcher(status.getText());
			while (matcher.find()) {
				callPeoples.add(matcher.group(1));
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

			Date createdAtDate = status.getCreatedAt();
			DateFormat week = new SimpleDateFormat("yyyyww");
			String reportWeek = week.format(createdAtDate);
			datesWeeks.add(reportWeek);

			String ISOCode = status.getLang();
			AnalyzeLang.vec1.add(ISOCode);
		}

		if (lengthsOfTweets.isEmpty() && verifyIfQuery) {
			System.out.println("Could not find a single Tweet containing the query!");
			System.exit(0);
		} else if (lengthsOfTweets.isEmpty()) {
			System.out.println("Could not acquire any Tweets!");
		}

		setCurrentWeek();

		Integer totalTypesOfSignsInATweet[] = { 0, 0, 0, 0, 0 };
		for (Integer[] entries : typesOfSignsInTweetsList) {
			for (int i = 0; i < 5; i++)
				totalTypesOfSignsInATweet[i] += entries[i];
		}

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
		Map<String, Long> datesWeeksOccurences = datesWeeks.stream()
				.collect(Collectors.groupingBy(e -> e, Collectors.counting()));

		ImportingTweets.handleUser();
		AnalyzeLang.analyzeLang();
		ChartGeneration.charactersInTweets(totalTypesOfSignsInATweet, typesOfSignsInTweetsList);
		ChartGeneration.mentionPieChart(instancesOfNicksSorted);
		ChartGeneration.datesLineChart(datesWeeksOccurences);
		System.out.println("Finished the execution of the program.");
	}
}
