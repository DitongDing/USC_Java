package ddt.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

import ddt.utils.bean.cfg.Node;
import ddt.utils.bean.cfg.CFG;

public class CFGUtils {
	public static void checkProperty(CFG cfg, String propertyInput, String propertyOutput) throws Exception {
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
						passedPair += String.format("\n\tNode \"%s\" can reach node \"%s\"", preNode.toString(), postNode.toString());

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