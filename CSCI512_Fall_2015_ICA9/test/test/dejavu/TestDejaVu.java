package test.dejavu;

import ddt.utils.DejaVuUtils;

public class TestDejaVu {
	public static void main(String[] args) {
		final String coverageDir = "./input/CR2";
		final String testSuiteFile0 = "./input/testSuite0.feature";
		final String classFilePath0 = "./input/0_Login_jsp.class";
		final String sourceFilePath0 = "./input/0_Login_jsp.java";
		final String classFilePath1 = "./input/1_Login_jsp.class";
		final String sourceFilePath1 = "./input/1_Login_jsp.java";
		final String methodName = "_jspService";
		final String dangerousLines = "./output/dangerousLines";
		final String testSuiteFile1 = "./output/testSuite1.feature";

		DejaVuUtils.run(coverageDir, testSuiteFile0, classFilePath0, sourceFilePath0, classFilePath1, sourceFilePath1,
				methodName, dangerousLines, testSuiteFile1);
	}
}