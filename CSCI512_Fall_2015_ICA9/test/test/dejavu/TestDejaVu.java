package test.dejavu;

import ddt.utils.DejaVuUtils;

// TODO: remain untested.
public class TestDejaVu {
	public static void main(String[] args) {
		final String coverageDir = "./input/CoverageReport";
		final String testSuiteFile0 = "./input/Default.feature";
		final String classFilePath0 = "./input/0_Default_jsp.class";
		final String classFilePath1 = "./input/1_Default_jsp.class";
		final String methodName = "Recommended_Show";
		final String dangerousLines = "./output/dangerousLines";
		final String testSuiteFile1 = "./output/testSuite1.feature";

		DejaVuUtils.run(coverageDir, testSuiteFile0, classFilePath0, classFilePath1, methodName, dangerousLines,
				testSuiteFile1);
	}
}