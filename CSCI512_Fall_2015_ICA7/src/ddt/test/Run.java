package ddt.test;

import ddt.utils.TarantulaUtils;

public class Run {

	public static void main(String[] args) {
		if (args == null || (args.length != 3 && args.length != 4)) {
			System.out.println("The usage is: Run <coverage dir> <result file path> <source file dir> [<topK>]");
			System.exit(-1);
		}

		final String coverageDir = args[0];
		final String resultFile = args[1];
		final String baseDir = args[2];
		final Integer K = args.length == 3 ? null : Integer.valueOf(args[3]);

		new TarantulaUtils(coverageDir, resultFile, baseDir, K).analysis();
	}
}