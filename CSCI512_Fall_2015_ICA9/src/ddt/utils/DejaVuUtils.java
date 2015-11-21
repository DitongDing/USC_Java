package ddt.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ddt.utils.bean.cfg.Edge;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;
import ddt.utils.bean.dejavu.TestCase;

// Method <-> control flow graph
public class DejaVuUtils {
	// Analysis one class.method every time.
	@SuppressWarnings("deprecation")
	public static void run(String coverageDir, String testSuiteFile0, String classFilePath0, String sourceFilePath0,
			String classFilePath1, String sourceFilePath1, String methodPartName, String report, String testSuiteFil1) {
		try {
			// Step 1: Build CFG for org and revised.
			final String[] accepts = { methodPartName };
			final String[] rejects = { "cobertura" };
			Method method0 = CFGUtils.buildCFG(classFilePath0, accepts, rejects).getMethodByPartName(methodPartName);
			Method method1 = CFGUtils.buildCFG(classFilePath1, accepts, rejects).getMethodByPartName(methodPartName);

			// Step 2: Build TCNodeTable based on method0 and test suite
			Set<TestCase> testSuite = ComUtils.getTestSuite(method0, coverageDir, testSuiteFile0);
			Map<Node, Set<TestCase>> TCNodeTable = getTCNodeTable(testSuite);

			// Step 3: Compare cfg0, cfg1 to get dangerous node set.
			Set<Node> dangerousNodes = getDangerousNodes(method0, method1);

			// Step 4: Get reduced test suite
			Set<TestCase> reducedTestSuite = reduceTestSuite(TCNodeTable, dangerousNodes);
			ComUtils.writeTestSuite(sourceFilePath0, sourceFilePath1, reducedTestSuite, testSuiteFil1);

			ComUtils.writeReport(dangerousNodes, testSuite, reducedTestSuite, report);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Step 2 unit. Do not need CFG as test case contains executed node.
	public static Map<Node, Set<TestCase>> getTCNodeTable(Set<TestCase> testSuite) {
		Map<Node, Set<TestCase>> result = new HashMap<Node, Set<TestCase>>();

		for (TestCase testCase : testSuite)
			for (Node executedNode : testCase.getExecutedNodes()) {
				Set<TestCase> relatedTestCases = result.get(executedNode);
				if (relatedTestCases == null) {
					relatedTestCases = new HashSet<TestCase>();
					result.put(executedNode, relatedTestCases);
				}
				relatedTestCases.add(testCase);
			}

		return result;
	}

	// Step 3 unit
	public static Set<Node> getDangerousNodes(Method method0, Method method1) {
		Set<Node> result = new HashSet<Node>();
		Set<Node> visited = new HashSet<Node>();

		Node node0 = method0.getEntry();
		Node node1 = method1.getEntry();

		compare(result, visited, node0, node1);

		return result;
	}

	private static void compare(Set<Node> result, Set<Node> visited, Node node0, Node node1) {
		visited.add(node0);
		for (Edge edge0 : node0.getOutEdges()) {
			Node child0 = edge0.getEndNode();
			boolean isDangerous = false;

			Edge edge1 = node1.getOutEdgeByDescription(edge0.getDescription());
			if (edge1 == null)
				isDangerous = true;
			else {
				Node child1 = edge1.getEndNode();
				if (!visited.contains(child0)) {
					if (!child0.equalsByInstruction(child1))
						isDangerous = true;
					else
						compare(result, visited, child0, child1);
				}
			}

			if (isDangerous)
				result.add(child0);
		}
	}

	// Step 4 unit
	public static Set<TestCase> reduceTestSuite(Map<Node, Set<TestCase>> TCNodeTable, Set<Node> dangerousNodes) {
		Set<TestCase> result = new HashSet<TestCase>();

		for (Node dangerousNode : dangerousNodes)
			result.addAll(TCNodeTable.get(dangerousNode));

		return result;
	}
}