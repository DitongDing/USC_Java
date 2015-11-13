package ddt.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

import ddt.utils.bean.CFG;
import ddt.utils.bean.Node;

// Usage: Run <class file> <property file> <cfg output> <property output>
// property file should be in method name > method name format.
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
			String preNodeDescription = line.substring(0, line.indexOf('>')).trim();
			String postNodeDescription = line.substring(line.indexOf('>') + 1).trim();

			List<Node> preNodes = cfg.getMainNodesByDescription(preNodeDescription);
			List<Node> postNodes = cfg.getMainNodesByDescription(postNodeDescription);

			String passedPair = "";
			for (Node preNode : preNodes)
				for (Node postNode : postNodes)
					if (cfg.checkReachability(preNode, postNode))
						passedPair += String.format("\n\tNode \"%s\" can reach node \"%s\"", preNode.toString(),
								postNode.toString());

			if (passedPair.equals(""))
				pw.println(String.format("\"%s\" is false", line));
			else
				pw.println(String.format("\"%s\" is true, as:%s", line, passedPair));

			pw.println();
			line = br.readLine();
		}
		br.close();
		pw.close();
	}
}