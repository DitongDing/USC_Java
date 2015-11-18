package ddt.utils.bean.cfg;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
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

// Currently CFG only works on one .class file, but it can be changed to multiple files.
public class CFG {
	private static final String DOTTY_HEADER = "digraph control_flow_graph {\n\tnode [shape = rectangle]; entry exit;\n\tnode [shape = circle];";
	private static final String DOTTY_FOOTER = "}";

	// methodName -> method
	protected Map<String, Method> methodMap;
	protected ConstantPoolGen cpg;

	protected Map<Node, Set<Node>> beReachedMap;

	public CFG(String classFilePath, String[] accepts, String[] rejects) throws Exception {
		methodMap = new HashMap<String, Method>();

		// Parse class file
		JavaClass cls;
		cls = (new ClassParser(classFilePath)).parse();
		cpg = new ConstantPoolGen(cls.getConstantPool());
		// Traverse methods and add them to map
		for (org.apache.bcel.classfile.Method method : cls.getMethods()) {
			String name = method.getName();
			if (checkIfAccept(name, accepts, rejects)) {
				Method m = new Method(cls, this, method);
				addMethod(m);
			}
		}
		// Initialize methods
		List<Method> methods = new ArrayList<Method>(methodMap.values());
		for (Method method : methods)
			method.initialize();
	}

	public CFG(String classFilePath) throws Exception {
		this(classFilePath, new String[0], new String[0]);
	}

	// accept.length = 0 means accept all; reject.length = 0 reject none. Reject
	// array has higher priority
	private boolean checkIfAccept(String name, String[] accepts, String[] rejects) {
		boolean isAccepted = true;

		// Only check rejects when it has rules.
		if (rejects.length != 0)
			for (String reject : rejects)
				if (name.contains(reject)) {
					isAccepted = false;
					break;
				}

		// Only check accepts when it pass rejects check and it has rules.
		if (isAccepted && accepts.length != 0) {
			isAccepted = false;
			for (String accept : accepts)
				if (name.contains(accept)) {
					isAccepted = true;
					break;
				}
		}

		return isAccepted;
	}

	private void addMethod(Method method) {
		assert (method != null && method.getMethodName() != null);
		methodMap.put(method.getMethodName(), method);
	}

	public Method getMethodByFullName(String methodFullName) {
		Method result = methodMap.get(methodFullName);
		if (result == null) {
			// assert (methodName.startsWith("java"));
			result = new Method(this, methodFullName);
			addMethod(result);
		}
		return result;
	}

	// NOT SAFE!
	@Deprecated
	public Method getMethodByPartName(String methodPartName) {
		for (Method method : methodMap.values())
			if (method.getMethodName().contains(methodPartName))
				return method;
		return null;
	}

	public ConstantPoolGen getCPG() {
		return cpg;
	}

	public void toDottyFile(String methodName, String outputFileName) throws Exception {
		Method method = this.getMethodByPartName(methodName);
		PrintWriter pw = new PrintWriter(outputFileName);

		pw.println(DOTTY_HEADER);
		pw.println();

		Set<Node> visited = new HashSet<Node>();
		LinkedList<Node> queue = new LinkedList<Node>();
		List<Edge> edges = new ArrayList<Edge>();
		queue.add(method.getEntry());
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

	public List<Node> getNodesByDescription(String methodName, String description) {
		Collection<Node> nodes = this.getMethodByPartName(methodName).getNodeMap().values();
		List<Node> result = new ArrayList<Node>();

		for (Node node : nodes)
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