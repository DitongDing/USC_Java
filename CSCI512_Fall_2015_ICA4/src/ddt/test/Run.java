package ddt.test;

import ddt.test.utils.TestUtils;

public class Run {
	// Usage: Run <generate> <input> <wget_org> <org_appname> <wget_test> <test_appname> <URLBase>
	// Or: Run <compare> <expected> <actual> <output>
	public static void main(String[] args) throws Exception {
		if (args == null || !((args.length == 7 && "generate".equals(args[0]))
				|| (args.length == 4 && "compare".equals(args[0])))) {
			System.out.println(
					"The usage is: Run <generate> <input> <wget_org> <org_appname> <wget_test> <test_appname> <URLBase> | Run <compare> <expected> <actual> <output>");
			System.exit(-1);
		}

		if ("generate".equals(args[0])) {
			final String input = args[1];
			final String wgetOrg = args[2];
			final String orgAppname = args[3];
			final String wgetTest = args[4];
			final String testAppname = args[5];
			final String URLBase = args[6];

			TestUtils.generate(input, wgetOrg, orgAppname, wgetTest, testAppname, URLBase);
		} else if ("compare".equals(args[0])) {
			final String expected = args[1];
			final String actual = args[2];
			final String output = args[3];

			TestUtils.compare(expected, actual, output);
		}
	}
}