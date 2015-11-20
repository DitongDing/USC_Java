package test.dejavu;

import ddt.utils.DejaVuUtils;
import ddt.utils.bean.dejavu.ClassFile;

// TODO: remain untested.
public class TestDejaVu {
	public static void main(String[] args) {
		final String coverageDir = "./input/CoverageReport";
		final String testSuiteFile0 = "./input/Default.feature";
		final String classFilePath0 = "./input/org_Default_jsp.class";
		final String sourceFilePath0 = "./input/org_Default_jsp.java";
		final String classFilePath1 = "./input/1_Default_jsp_.class";
		final String sourceFilePath1 = "./input/1_Default_jsp_.java";
		final String methodName = "Recommended_Show";
		final String dangerousLines = "./output/dangerousLines";
		final String testSuiteFile1 = "./output/testSuite1.feature";

		DejaVuUtils.run(coverageDir, testSuiteFile0, new ClassFile(classFilePath0, sourceFilePath0), new ClassFile(classFilePath1, sourceFilePath1), methodName,
				dangerousLines, testSuiteFile1);
	}
}