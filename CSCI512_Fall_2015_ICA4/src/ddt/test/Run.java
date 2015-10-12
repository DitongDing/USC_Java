package ddt.test;

import ddt.test.utils.ComUtils;

public class Run {
	// Usage: Run <input> <wget_org> <org_appname> <wget_test> <test_appname> <URLBase>
	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 6) {
			System.out
					.println("The usage is: Run <input> <wget_org> <org_appname> <wget_test> <test_appname> <URLBase>");
			System.exit(-1);
		}

		final String input = args[0];
		final String wgetOrg = args[1];
		final String orgAppname = args[2];
		final String wgetTest = args[3];
		final String testAppname = args[4];
		final String URLBase = args[5];

		ComUtils.generate(input, wgetOrg, orgAppname, wgetTest, testAppname, URLBase);
	}
}