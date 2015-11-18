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
		final String methodName = "org.apache.jsp.Default_jsp.AdvMenu_Show(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;Ljavax/servlet/http/HttpSession;Ljavax/servlet/jsp/JspWriter;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/sql/Connection;Ljava/sql/Statement;)V";
		final Method method = new CFG("./input/Default_jsp.class", new String[] { "AdvMenu_Show" }, new String[0]).getMethod(methodName);

		System.out.println(new Timer() {
			public void run() {
				Set<Node> nodes = new TestCase(file, methodName, method).getExecutedNodes();

				for (Node node : nodes)
					System.out.println(String.format("Node: %s", node));
			}
		}.getRunTime());
	}
}