package ddt.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import ddt.utils.bean.CFG;

// Usage: Run <class file> <property file> <cfg output> <property output>
// property file should be in "method name" > "method name" format.
public class Run {
	public static void main(String[] args) throws Exception {
		if (args == null || args.length != 4) {
			System.out.println("Usage: Run <class file> <property file> <cfg output> <property output>");
			System.exit(-1);
		}

		final String classFile = args[0];
		final String propertyInput = args[1];
		final String cfgOutput = args[2];
		final String propertyOutput = args[3];

		CFG cfg = new CFG(classFile);
		cfg.mainToDottyFile(cfgOutput);

		BufferedReader br = new BufferedReader(new FileReader(propertyInput));
		PrintWriter pw = new PrintWriter(propertyOutput);
		String line = br.readLine();
		while (line != null) {
			// TODO: <1 HIGH> finish property check.

			line = br.readLine();
		}
		br.close();
		pw.close();
	}
}