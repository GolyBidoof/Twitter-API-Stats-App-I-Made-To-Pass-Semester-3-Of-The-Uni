public final class MainClass {
	
	public static void main(String[] args) throws Exception {
		InitializeTwitterInstance.checkLogin();
		AnalyzeLang.handleStatuses(100);
		AnalyzeLang.analyzeLang();
		HandleTweetStats.calcStats();
		System.exit(0);
	}

}
