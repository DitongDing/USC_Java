package ddt.test;

import ddt.utils.DejaVuUtils;

// input: coverageDir, testSuiteFile0, classFilePath0, classFilePath1, methodName
// output: dangerousLines, testSuiteFile1.
public class Run {
	public static void main(String[] args) {
		if (args == null || args.length != 7) {
			System.out.println(
					"Usage: Run <coverageDir> <testSuiteFile0> <classFilePath0> <classFilePath1> <methodName> <output dangerousLines> <output testSuiteFile1>");
			System.exit(-1);
		}

		final String coverageDir = args[0];
		final String testSuiteFile0 = args[1];
		final String classFilePath0 = args[2];
		final String classFilePath1 = args[3];
		final String methodName = args[4];
		final String dangerousLines = args[5];
		final String testSuiteFile1 = args[6];

		DejaVuUtils.run(coverageDir, testSuiteFile0, classFilePath0, classFilePath1, methodName, dangerousLines,
				testSuiteFile1);
	}
}