import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public final class loginTest {
	static Twitter twitter;
	
	public static void main(String[] args) throws Exception {
		//Message Contents
		ConfigurationBuilder needThisForLongerTweets = new ConfigurationBuilder().setTweetModeExtended(true)
				.setOAuthConsumerKey(keys.keyPublic)
				.setOAuthConsumerSecret(keys.keySecret);

		//Check if the token file exists
		File file = new File("token");
		AccessToken accessToken = null;

		if (file.exists()) {
			//Basically set up the file to the old data
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String text = null;
			String token = null;
			String tokenSecret = null;
			if ((text = reader.readLine()) != null) token = text;
			if ((text = reader.readLine()) != null) tokenSecret = text;
			if (token != null && tokenSecret != null) {
				accessToken = new AccessToken(token, tokenSecret);
				needThisForLongerTweets.setOAuthAccessToken(token);
				needThisForLongerTweets.setOAuthAccessTokenSecret(tokenSecret);
				//twitter.setOAuthAccessToken(accessToken);
			} else {
				System.out.println("Invalid token!");
				System.exit(0);
			}
			reader.close();
		} else {
			twitter = TwitterFactory.getSingleton();
			twitter.setOAuthConsumer(keys.keyPublic, keys.keySecret);
			//Request to obtain the token
			RequestToken requestToken = twitter.getOAuthRequestToken();
			BufferedReader br = new BufferedReader(new InputStreamReader(System. in ));
			while (null == accessToken) {
				System.out.println("Copy this link to your browser to get a token");
				System.out.println(requestToken.getAuthorizationURL());
				System.out.print("And now just enter PIN: ");
				String pin = br.readLine();
				try {
					if (pin.length() > 0) {
						accessToken = twitter.getOAuthAccessToken(requestToken, pin);
					} else {
						accessToken = twitter.getOAuthAccessToken();
					}
					needThisForLongerTweets.setOAuthAccessToken(accessToken.getToken());
					needThisForLongerTweets.setOAuthAccessTokenSecret(accessToken.getTokenSecret());
					BufferedWriter writer = new BufferedWriter(new FileWriter("token"));
					writer.write(accessToken.getToken());
					writer.newLine();
					writer.append(accessToken.getTokenSecret());
					writer.close();
				} catch(TwitterException te) {
					if (401 == te.getStatusCode()) {
						System.out.println("Unable to get the access token.");
					} else {
						te.printStackTrace();
					}
				}
			}
		}
		TwitterFactory tf = new TwitterFactory(needThisForLongerTweets.build());
		twitter = tf.getInstance();
		AnalyzeLang.handleStatuses(100);
		AnalyzeLang.analyzeLang();
		HandleTweetStats.calcStats();
		System.exit(0);
	}
}