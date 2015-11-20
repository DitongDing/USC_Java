package ddt.utils.bean.dejavu;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import ddt.utils.ComUtils;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;

public class TestCase {
	protected String testCaseName;
	protected Set<Node> executedNodes;

	public TestCase(File coverageReport, Method method) {
		this.testCaseName = ComUtils.getTestCaseName(coverageReport);
		this.executedNodes = new HashSet<Node>();

		Set<Integer> executedLineNumbers = ComUtils.getExecutedLineNumbers(coverageReport, method);

		for (Node node : method.getNodeMap().values()) {
			Integer lineNumber = node.getLineNumber();
			if (lineNumber != null && executedLineNumbers.contains(lineNumber))
				executedNodes.add(node);
		}
	}

	public Set<Node> getExecutedNodes() {
		return executedNodes;
	}
}
