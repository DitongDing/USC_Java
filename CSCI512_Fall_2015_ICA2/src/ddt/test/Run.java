package ddt.test;

import ddt.test.crawljax.Crawler;

public class Run {
	// Usage: Run <URL>
	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 2) {
			System.out.println("The usage is: Run <URL> <login URL>");
			System.exit(-1);
		}

		final String URL = args[0];
		final String loginURL = args[1];
		// Initiate Builder
		Crawler crawlwer = new Crawler(URL, loginURL);
		// Use builder to execute Runner
		crawlwer.run();
	}
}