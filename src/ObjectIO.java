import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import twitter4j.Status;

public class ObjectIO {

	public static void verifyIfFolderExists(String username) {
		File theDir = new File("data/TweetDatabase");
		File userFolder = new File("data/TweetDatabase/" + username);
		if (!theDir.exists()) {
			try {
				theDir.mkdir();
			} catch (SecurityException se) {
			}
		}
		if (!userFolder.exists()) {
			try {
				userFolder.mkdir();
			} catch (SecurityException se) {
			}
		}
	}

	public static void saveObjectToFile(Status toSave) throws IOException {
		String username = MainClass.nickOfAPerson;
		String filename = "data/TweetDatabase/" + username + "/" + Long.toString(toSave.getId()) + ".status";
		verifyIfFolderExists(username);
		FileOutputStream fos = new FileOutputStream(filename);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(toSave);
		oos.close();
	}

	public static void loadObjectFromFile() throws IOException, ClassNotFoundException {
		File dir = new File("data/TweetDatabase/" + MainClass.nickOfAPerson);
		for (File file : dir.listFiles()) {
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			ObjectInputStream ois = new ObjectInputStream(fis);
			Status imported = (Status) ois.readObject();
			ImportingTweets.statuses.add(imported);
			ois.close();
		}
	}
}
