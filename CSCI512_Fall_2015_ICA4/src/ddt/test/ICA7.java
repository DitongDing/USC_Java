package ddt.test;

import ddt.test.utils.ComUtils;

public class ICA7 {
	// Usage: Run <input> <org_appname> <output>
	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 3) {
			System.out
					.println("The usage is: Run <input> <org_appname> <output>");
			System.exit(-1);
		}

		final String input = args[0];
		final String orgAppname = args[1];
		final String output = args[2];

		ComUtils.generate(input, orgAppname, output);
	}
}