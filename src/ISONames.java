import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class ISONames {

	public static class ISOLangPair {
		String code;
		String fullName;

		public ISOLangPair(String code2, String engFullName) {
			code = code2;
			fullName = engFullName;
		}
	}

	static Vector<ISOLangPair> AllTheLangs = new Vector<ISOLangPair>();

	public static void Load() throws IOException {
		File fr = new File("data/RelevantFiles/data_csv.csv");
		Scanner inputStream = new Scanner(fr);
		while (inputStream.hasNextLine()) {
			String line = inputStream.nextLine();
			String values[] = line.split(",");
			String code = values[1].toLowerCase();
			String EngFullName = values[0];
			ISOLangPair temp = new ISOLangPair(code, EngFullName);
			AllTheLangs.addElement(temp);
		}
		inputStream.close();
		System.out.println("Imported language file");
	}

	public static Map<String, Long> changeNamesToISO(Map<String, Long> sortedMap) {
		Map<String, Long> gudLangMap = new LinkedHashMap<String, Long>();
		for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {

			String key = entry.getKey();
			Long val = entry.getValue();

			for (int i = 0; i < ISONames.AllTheLangs.size(); i++) {
				if (key.equals("und")) {
					gudLangMap.put("Undetermined", val);
					break;
				} else if (key.equals(ISONames.AllTheLangs.get(i).code)) {
					gudLangMap.put(ISONames.AllTheLangs.get(i).fullName, val);
					break;
				}

				if (i == ISONames.AllTheLangs.size() - 1) {
					Long initialVal = (long) 0;
					if (gudLangMap.get("Undetermined") != null)
						initialVal = gudLangMap.get("Undetermined");
					gudLangMap.put("Undetermined", initialVal + val);
				}
			}
		}
		return gudLangMap;
	}
}
