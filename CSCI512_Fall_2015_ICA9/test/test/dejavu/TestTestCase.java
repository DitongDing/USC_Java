package test.dejavu;

import java.io.File;
import java.util.Set;

import ddt.utils.Timer;
import ddt.utils.bean.cfg.CFG;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;
import ddt.utils.bean.dejavu.TestCase;

public class TestTestCase {
	public static void main(String[] args) throws Exception {
		final File file = new File("./input/CoverageReport/Open default page with 0 weekly featured book(s)_false.xml");
		final String methodName = "org.apache.jsp.Default_jsp.Recommended_Show()";
		final Method method = new CFG("./input/Default_jsp.class", new String[] { "Recommended_Show" }, new String[0]).getMethodByFullName(methodName);

		System.out.println(new Timer() {
			public void run() {
				Set<Node> nodes = new TestCase(file, methodName, method).getExecutedNodes();

				for (Node node : nodes)
					System.out.println(String.format("Node: %s", node));
			}
		}.getRunTime());
	}
}