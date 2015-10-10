package ddt.test;

import ddt.test.utils.TestUtils;

public class Run {
	// Usage: Run <generate> <input> <wget_output> <expected_result_output>
	// Or: Run <compare> <expected> <actual> <output>
	public static void main(String[] args) throws Exception {
		if (args == null || !((args.length == 4 && "generate".equals(args[0]))
				|| (args.length == 4 && "compare".equals(args[0])))) {
			System.out.println(
					"The usage is: Run <generate> <input> <wget_output> <expected_result_output> | Run <compare> <expected> <actual> <output>");
			System.exit(-1);
		}

		if ("generate".equals(args[0])) {
			final String input = args[1];
			final String wgetOutput = args[2];
			final String expectedResultOutput = args[3];

			TestUtils.generate(input, wgetOutput, expectedResultOutput);
		} else if ("compare".equals(args[0])) {
			final String expected = args[1];
			final String actual = args[2];
			final String output = args[3];

			TestUtils.compare(expected, actual, output);
		}
	}
}