package ddt.utils.bean.cfg;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ConstantPoolGen;

public class CFG {
	private static final String DOTTY_HEADER = "digraph control_flow_graph {\n\tnode [shape = rectangle]; entry exit;\n\tnode [shape = circle];";
	private static final String DOTTY_FOOTER = "}";

	// methodName -> method
	protected Map<String, Method> methodMap;
	protected Method mainMethod;
	protected ConstantPoolGen cpg;

	protected Map<Node, Set<Node>> beReachedMap;

	public CFG(String classFilePath) throws Exception {
		methodMap = new HashMap<String, Method>();

		// Parse class file
		JavaClass cls;
		cls = (new ClassParser(classFilePath)).parse();
		cpg = new ConstantPoolGen(cls.getConstantPool());
		// Traverse methods and add them to map
		for (org.apache.bcel.classfile.Method method : cls.getMethods()) {
			Method m = new Method(cls, this, method);
			if (method.getName().equals("main"))
				mainMethod = m;
			addMethod(m);
		}
		// Initialize methods
		List<Method> methods = new ArrayList<Method>(methodMap.values());
		for (Method method : methods)
			method.initialize();

		// ArrayList<Node> nodes = new ArrayList<Node>(mainMethod.getNodeMap().values());
		// Collections.sort(nodes);
		// for (Node node : nodes) {
		// System.out.println(node.getOffset() + ":");
		// ArrayList<Node> beReacheds = new ArrayList<Node>(node.getBeReached());
		// Collections.sort(beReacheds);
		// for (Node beReached : beReacheds)
		// System.out.println("\t" + beReached.getOffset());
		// }
	}

	private void addMethod(Method method) {
		assert (method != null && method.getMethodName() != null);
		methodMap.put(method.getMethodName(), method);
	}

	public Method getMethod(String methodName) {
		Method result = methodMap.get(methodName);
		if (result == null) {
			assert (methodName.startsWith("java"));
			result = new Method(this, methodName);
			addMethod(result);
		}
		return result;
	}

	public ConstantPoolGen getCPG() {
		return cpg;
	}

	// Only draw main function.
	public void mainToDottyFile(String outputFileName) throws Exception {

		PrintWriter pw = new PrintWriter(outputFileName);

		pw.println(DOTTY_HEADER);
		pw.println();

		Set<Node> visited = new HashSet<Node>();
		LinkedList<Node> queue = new LinkedList<Node>();
		List<Edge> edges = new ArrayList<Edge>();
		queue.add(mainMethod.getEntry());
		while (!queue.isEmpty()) {
			Node currentNode = queue.pollFirst();
			if (!visited.contains(currentNode)) {
				for (Edge edge : currentNode.getOutEdges()) {
					edges.add(edge);
					queue.add(edge.getEndNode());
				}
				visited.add(currentNode);
			}
		}

		Collections.sort(edges);
		for (Edge edge : edges)
			pw.println(edge.toDottyString());
		List<Node> nodes = new ArrayList<Node>(visited);
		Collections.sort(nodes);
		for (Node node : nodes)
			pw.println(String.format("%s [label = \"%s\"]", node.toDottyString(), node.toString()));

		pw.println();
		pw.println(DOTTY_FOOTER);

		pw.close();
	}

	public List<Node> getMainNodesByDescription(String description) {
		List<Node> result = new ArrayList<Node>();

		for (Node node : mainMethod.getNodeMap().values())
			if (node.getDescription().equals(description))
				result.add(node);

		return result;
	}

	public boolean checkReachability(Node preNode, Node postNode) {
		boolean result = false;
		if (preNode.equals(postNode)) {
			Node node = preNode;
			for (Edge edge : node.inEdges) {
				if (result)
					break;
				if (edge.getStartNode().equals(node))
					result = true;
				else
					result = checkReachability(node, edge.getStartNode());
			}
			for (Edge edge : node.outEdges) {
				if (result)
					break;
				if (edge.getEndNode().equals(node))
					result = true;
				else
					result = checkReachability(edge.getEndNode(), node);
			}
		} else
			result = postNode.canBeReachedBy(preNode);
		return result;
	}
}