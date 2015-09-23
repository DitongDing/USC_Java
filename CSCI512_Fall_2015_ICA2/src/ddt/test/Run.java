package ddt.test;

import ddt.test.crawljax.Crawler;

public class Run {
	// Usage: Run <URL>
	public static void main(String[] args) {
		if(args == null || args.length != 1) {
			System.out.println("The usage is: Run <URL>");
			System.exit(-1);
		}
		
		final String URL = args[0];
		// Initiate Builder
		Crawler crawlwer = new Crawler(URL);
		// Use builder to execute Runner
		crawlwer.run();
	}
}