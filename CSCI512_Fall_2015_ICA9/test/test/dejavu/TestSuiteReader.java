package test.dejavu;

import java.util.Set;

import ddt.utils.ComUtils;
import ddt.utils.bean.cfg.CFG;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.dejavu.TestCase;

public class TestSuiteReader {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		final String classFilePath = "./input/Default_jsp.class";
		final String methodPartName = "Recommended_Show";
		final String coverageDir = "./input/CoverageReport";
		final String testSuiteFile = "./input/Default.feature";
		final Method method = new CFG(classFilePath, new String[] { methodPartName }, new String[0]).getMethodByPartName(methodPartName);

		Set<TestCase> testSuite = ComUtils.getTestSuite(method, coverageDir, testSuiteFile);

		for (TestCase testCase : testSuite)
			System.out.println(testCase.getCucumberTestCase());
	}
}
