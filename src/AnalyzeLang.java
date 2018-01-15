import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import twitter4j.TwitterException;

public class AnalyzeLang {

	static List<String> vec1 = new ArrayList<String>();

	public static void analyzeLang() throws TwitterException, FontFormatException, IOException {

		int size = vec1.size();
		Map<String, Long> counts = vec1.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
		Map<String, Long> sortedMap = counts.entrySet().stream()
				.sorted(Map.Entry.<String, Long>comparingByValue().reversed())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

		ISONames.Load();
		Map<String, Long> gudLangMap = ISONames.changeNamesToISO(sortedMap);

		ChartGeneration.languageUses(gudLangMap, size);
	}
}
