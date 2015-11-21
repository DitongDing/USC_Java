package ddt.utils.bean.dejavu;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import ddt.utils.ComUtils;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;

// Designed for cucumber test case
public class TestCase {
	protected String testCaseName;
	protected String testCaseContent;
	protected Set<Node> executedNodes;

	public TestCase(File coverageReport, Method method, String testCaseName, String testCaseContent) {
		this.testCaseName = testCaseName;
		this.testCaseContent = testCaseContent;
		this.executedNodes = new HashSet<Node>();

		Set<Integer> executedLineNumbers = ComUtils.getExecutedLineNumbers(coverageReport, method);

		for (Node node : method.getNodeMap().values()) {
			Integer lineNumber = node.getLineNumber();
			if (lineNumber != null && executedLineNumbers.contains(lineNumber))
				executedNodes.add(node);
		}
	}

	public String getTestCaseName() {
		return testCaseName;
	}

	public Set<Node> getExecutedNodes() {
		return executedNodes;
	}

	public String getCucumberTestCase() {
		return String.format("Scenario: %s\n%s", testCaseName, testCaseContent);
	}

	@Override
	public int hashCode() {
		return testCaseName.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof TestCase))
			return false;
		final TestCase otherTestCase = (TestCase) other;
		return testCaseName.equals(otherTestCase.testCaseName);
	}
}
