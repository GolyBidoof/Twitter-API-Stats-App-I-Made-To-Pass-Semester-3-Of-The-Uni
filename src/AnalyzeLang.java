import java.awt.FontFormatException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

public class AnalyzeLang {
	
	static List<Status> statuses;
	static String nickOfAPerson = "NitroKoopa";
	
	public static void handleStatuses(int number) throws TwitterException {
		Paging p = new Paging();
        p.setCount(number);
        statuses = loginTest.twitter.getUserTimeline(nickOfAPerson, p);
	}
	
    public static void analyzeLang() throws TwitterException, FontFormatException, IOException {
    	List<String> vec1 = new ArrayList<String>();
	    for (Status status : statuses) {
	    	String ISOCode = status.getLang();
	    	vec1.add(ISOCode);
	    	System.out.println(ISOCode);
	    }
	    
	    Map<String, Long> counts = vec1.stream().collect(Collectors.groupingBy(e -> e, Collectors.counting()));
	    Map<String, Long> sortedMap =
                counts.entrySet().stream()
                        .sorted(Map.Entry.<String, Long> comparingByValue().reversed())
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e2, LinkedHashMap::new));
	    
	    
	    ISONames.Load();
	    Map<String, Long> gudLangMap = ISONames.changeNamesToISO(sortedMap);
	    
	    JFreeChartBarColors.languageUses(gudLangMap);	
	    System.out.println("Sorted Map: " + Arrays.toString(sortedMap.entrySet().toArray()));
	    System.out.println("Sorted Map: " + Arrays.toString(gudLangMap.entrySet().toArray()));
    }
}
