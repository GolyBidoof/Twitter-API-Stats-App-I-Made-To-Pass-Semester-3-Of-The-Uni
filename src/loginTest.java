import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
 
public final class loginTest {
		static Twitter twitter = TwitterFactory.getSingleton();
		public static void main(String[] args) throws Exception {
		  	//Message Contents
		    
			twitter.setOAuthConsumer(keys.keyPublic, keys.keySecret);
		    
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
			    	accessToken=new AccessToken(token, tokenSecret);
			    	twitter.setOAuthAccessToken(accessToken);
			    } else {
			    	System.out.println("Invalid token!");
			    	System.exit(0);
			    }
			    reader.close();
		    } else {
		    	//Request to obtain the token
		    	RequestToken requestToken = twitter.getOAuthRequestToken();
			    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			    while (null == accessToken) {
				      System.out.println("Copy this link to your browser to get a token");
				      System.out.println(requestToken.getAuthorizationURL());
				      System.out.print("And now just enter PIN: ");
				      String pin = br.readLine();
				      try{
					         if(pin.length() > 0){
					           accessToken = twitter.getOAuthAccessToken(requestToken, pin);
					         } else {
					           accessToken = twitter.getOAuthAccessToken();
					         }
					         BufferedWriter writer = new BufferedWriter(new FileWriter("token"));
					         writer.write(accessToken.getToken());
					         writer.newLine();
					         writer.append(accessToken.getTokenSecret());
					         writer.close();
				      } catch (TwitterException te) {
					        if(401 == te.getStatusCode()){
					          System.out.println("Unable to get the access token.");
					        } else {
					          te.printStackTrace();
					        }
				      }
			    }
		    }
		    
		    
		    AnalyzeLang.analyzeLang();
		    
		    
		    //String output = "She wrote " + polcount + " tweets in Polish and " + engcount + " tweets in English";
		    //TwitterAPIPostingActions.writeATextTweet(output);
		    //User username = twitter.showUser("szymbar15");
		    //Status status = twitter.updateStatus("My creator Szymbar has " + username.getFollowersCount() + " followers, " + username.getStatusesCount() + " tweets, he uses " + username.getLang() + " language and claims he's from " + username.getLocation() + ".");
		    //System.out.println("Successfully updated the status to [" + status.getText() + "].");
		    
		    //Status status2 = twitter.updateStatus("BAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		    //System.out.println("Successfully updated the status to [" + status2.getText() + "].");
		    /*System.out.println("Showing home timeline.");
		    for (Status status : statuses) {
		        System.out.println(status.getUser().getName() + ":" + status.getCreatedAt() + ":" +
		                           status.getText());
		    }*/
		    
		    
		    System.exit(0);
		  }
	  
	  
}