package test.dejavu;

import java.io.File;
import java.util.Set;

import ddt.utils.ComUtils;
import ddt.utils.bean.cfg.CFG;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;
import ddt.utils.bean.dejavu.TestCase;

public class TestTestCase {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		final File file = new File("./input/CoverageReport/Open default page with 0 weekly featured book(s)_false.xml");
		final String methodPartName = "Recommended_Show";
		final Method method = new CFG("./input/Default_jsp.class", new String[] { methodPartName }, new String[0]).getMethodByPartName(methodPartName);
		final String sourcePath = "./input/Default_jsp.java";
		final String output = "./output/executedLines.txt";

		Set<Node> nodes = new TestCase(file, method).getExecutedNodes();

		ComUtils.writeLinesByNodes(sourcePath, nodes, output);
	}
}