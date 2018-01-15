public final class MainClass {

	static String nickOfAPerson = "szymbar15";
	static String query = null;

	public static void main(String[] args) throws Exception {
		Integer amountOfTweets = 200;

		if (args.length > 0) {
			try {
				Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.out.println("Incorrect number argument!");
				System.exit(0);
			}
			if (Integer.parseInt(args[0]) < 200 && Integer.parseInt(args[0]) > 0)
				amountOfTweets = Integer.parseInt(args[0]);
		}
		if (args.length > 1)
			nickOfAPerson = args[1];
		if (args.length > 2)
			query = args[2];

		System.out.println("A request to download " + amountOfTweets + " Tweets "
				+ ((query != null && !query.isEmpty()) ? ("containing the query " + query + " ") : ("")) + "from "
				+ nickOfAPerson + " has been made.");

		InitializeTwitterInstance.checkLogin();
		ImportingTweets.handleStatuses(amountOfTweets);
		HandleTweetStats.calcStats();
		System.exit(0);
	}

}
