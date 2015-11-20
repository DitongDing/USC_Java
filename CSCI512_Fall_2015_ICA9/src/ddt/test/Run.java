package ddt.test;

import ddt.utils.DejaVuUtils;
import ddt.utils.bean.dejavu.ClassFile;

// input: coverageDir, testSuiteFile0, classFilePath0, sourceFilePath0, classFilePath1, sourceFilePath1, methodName
// output: dangerousLines, testSuiteFile1.
public class Run {
	public static void main(String[] args) {
		if (args == null || args.length != 9) {
			System.out.println(
					"Usage: Run <coverageDir> <testSuiteFile0> <classFilePath0> <sourceFilePath0> <classFilePath1> <sourceFilePath1> <methodName> <output dangerousLines> <output testSuiteFile1>");
			System.exit(-1);
		}

		final String coverageDir = args[0];
		final String testSuiteFile0 = args[1];
		final String classFilePath0 = args[2];
		final String sourceFilePath0 = args[3];
		final String classFilePath1 = args[4];
		final String sourceFilePath1 = args[5];
		final String methodName = args[6];
		final String dangerousLines = args[7];
		final String testSuiteFile1 = args[8];

		DejaVuUtils.run(coverageDir, testSuiteFile0, new ClassFile(classFilePath0, sourceFilePath0), new ClassFile(classFilePath1, sourceFilePath1), methodName,
				dangerousLines, testSuiteFile1);
	}
}