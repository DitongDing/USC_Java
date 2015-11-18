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
import ddt.utils.bean.cfg.Node;
import ddt.utils.bean.dejavu.TestCase;

@Deprecated
public class DejaVuUtilsEdge {
	public static Map<Edge, Set<TestCase>> getTCEdgeTable(CFG cfg0, String methodName, String coverageDir) {
		Map<Edge, Set<TestCase>> result = new HashMap<Edge, Set<TestCase>>();

		// TODO: finish getTCEdgeTable function
		String[] accepts = new String[] { methodName };
		String[] rejects = new String[] { "cobertura" };
		File dir = new File(coverageDir);
		assert (dir.exists());
		for (File file : dir.listFiles()) {
			
		}

		return result;
	}

	public static List<Edge> getDangerousEdges(CFG cfg0, CFG cfg1, String methodName) {
		List<Edge> result = new ArrayList<Edge>();
		Set<Node> visited = new HashSet<Node>();

		Node node0 = cfg0.getMethodByFullName(methodName).getEntry();
		Node node1 = cfg1.getMethodByFullName(methodName).getEntry();

		compare(result, visited, node0, node1);

		return result;
	}

	private static void compare(List<Edge> result, Set<Node> visited, Node node0, Node node1) {
		visited.add(node0);
		for (Edge edge0 : node0.getOutEdges()) {
			boolean isDangerous = false;

			Edge edge1 = node1.getOutEdgeByDescription(edge0.getDescription());
			if (edge1 == null)
				isDangerous = true;
			else {
				Node child0 = edge0.getEndNode();
				Node child1 = edge1.getEndNode();
				if (!visited.contains(child0)) {
					if (!child0.equalsByInstruction(child1))
						isDangerous = true;
					else
						compare(result, visited, child0, child1);
				}
			}

			if (isDangerous)
				result.add(edge0);
		}
	}
}