package ddt.test;

import ddt.utils.TarantulaUtils;

public class Run {

	public static void main(String[] args) {
		if (args == null || args.length != 3) {
			System.out.println("The usage is: Run <coverage dir> <result file path> <source file dir>");
			System.exit(-1);
		}

		final String coverageDir = args[0];
		final String resultFile = args[1];
		final String baseDir = args[2];

		new TarantulaUtils(coverageDir, resultFile, baseDir).analysis();
	}
}