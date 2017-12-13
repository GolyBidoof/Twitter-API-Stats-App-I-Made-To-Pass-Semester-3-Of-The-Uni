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

	
    public static void analyzeLang() throws TwitterException, FontFormatException, IOException {
    	Paging p = new Paging();
        p.setCount(100);
        List<Status> statuses = loginTest.twitter.getUserTimeline("FyneQ", p);
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
	    
	    JFreeChartBarColors.color(gudLangMap);	
	    System.out.println("Sorted Map: " + Arrays.toString(sortedMap.entrySet().toArray()));
	    System.out.println("Sorted Map: " + Arrays.toString(gudLangMap.entrySet().toArray()));
	    /*for(Map.Entry<String, Record> entry: map.entrySet()) {
    	System.out.println(entry.getKey() + " : " + entry.getValue());*/
    }
}
