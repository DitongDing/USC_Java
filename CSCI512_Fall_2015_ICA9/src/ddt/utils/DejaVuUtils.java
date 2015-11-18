package ddt.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ddt.utils.bean.cfg.CFG;
import ddt.utils.bean.cfg.Edge;
import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;
import ddt.utils.bean.dejavu.ClassFile;
import ddt.utils.bean.dejavu.TestCase;

public class DejaVuUtils {
	public static Set<TestCase> run(String coverageDir, ClassFile[] classFiles0, ClassFile[] classFiles1) {
		Set<TestCase> result = new HashSet<TestCase>();

		try {

			// Step 1: Build CFGs for org and revised.
			final String[] rejects = { "cobertura" };
			List<CFG> cfg0s = new ArrayList<CFG>();
			List<CFG> cfg1s = new ArrayList<CFG>();
			for (int i = 0; i < classFiles0.length; i++) {
				assert (classFiles0[i].equals(classFiles1[i]));
				cfg0s.add(new CFG(classFiles0[i].getClassFilePath(), classFiles0[i].getMethodNames(), rejects));
				cfg1s.add(new CFG(classFiles1[i].getClassFilePath(), classFiles1[i].getMethodNames(), rejects));
			}

			// Step 2: Build TCNodeTable based on cfg0s and coverageDir
//			for (int i = 0; i < classFiles0.length; i++)
//				for
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// Step 2 unit
	private static Map<Node, Set<TestCase>> getTCNodeTable(CFG cfg0, String methodName, String coverageDir) {
		Map<Node, Set<TestCase>> result = new HashMap<Node, Set<TestCase>>();

		@SuppressWarnings("deprecation")
		Method method = cfg0.getMethodByPartName(methodName);
		File dir = new File(coverageDir);
		assert (dir.exists());
		for (File file : dir.listFiles()) {
			TestCase testCase = new TestCase(file, methodName, method);
			for (Node executedNode : testCase.getExecutedNodes()) {
				Set<TestCase> relatedTestCases = result.get(executedNode);
				if (relatedTestCases == null) {
					relatedTestCases = new HashSet<TestCase>();
					result.put(executedNode, relatedTestCases);
				}
				relatedTestCases.add(testCase);
			}
		}

		return result;
	}

	// Step 3 unit
	private static Set<Node> getDangerousNodes(CFG cfg0, CFG cfg1, String methodName) {
		Set<Node> result = new HashSet<Node>();
		Set<Node> visited = new HashSet<Node>();

		Node node0 = cfg0.getMethodByFullName(methodName).getEntry();
		Node node1 = cfg1.getMethodByFullName(methodName).getEntry();

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
	private static Set<TestCase> reduceTestSuites(Map<Node, Set<TestCase>> TCNodeTable, Set<Node> dangerousNodes) {
		Set<TestCase> result = new HashSet<TestCase>();

		for (Node dangerousNode : dangerousNodes)
			result.addAll(TCNodeTable.get(dangerousNode));

		return result;
	}
}