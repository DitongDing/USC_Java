package ddt.test;

import ddt.utils.WGETUtils;

public class Run {
	// Usage: Run <input.xml> <output.sh> <URLBase>
	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 3) {
			System.out.println("The usage is: Run <input.xml> <output.sh> <URLBase>");
			System.exit(-1);
		}

		final String input = args[0];
		final String output = args[1];
		final String URLBase = args[2];
		new WGETUtils(input, output, URLBase).parse();
	}
}