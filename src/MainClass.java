public final class MainClass {

	public static void main(String[] args) throws Exception {
		InitializeTwitterInstance.checkLogin();
		AnalyzeLang.handleStatuses(350);
		AnalyzeLang.analyzeLang();
		HandleTweetStats.calcStats();
		System.exit(0);
	}

}
