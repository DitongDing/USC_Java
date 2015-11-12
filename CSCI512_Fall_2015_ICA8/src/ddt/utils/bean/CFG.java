package ddt.utils.bean;

import java.io.PrintWriter;
import java.util.ArrayList;
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
	}

	private void addMethod(Method method) {
		assert (method != null && method.getMethodName() != null);
		methodMap.put(method.getMethodName(), method);
	}

	public Method getMethod(String methodName) {
		Method result = methodMap.get(methodName);
		if (result == null)
			assert (methodName.startsWith("java"));
		return result;
	}

	public ConstantPoolGen getCPG() {
		return cpg;
	}

	// TODO: <3 LOW> finish CFG to dotty file function.
	// Only draw main function.
	public void toDottyFile(String outputFileName) throws Exception {

		PrintWriter pw = new PrintWriter(outputFileName);

		pw.println(DOTTY_HEADER);

		Set<Node> visited = new HashSet<Node>();
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(mainMethod.getEntry());
		while (!queue.isEmpty()) {
			Node currentNode = queue.pollFirst();
			if (!visited.contains(currentNode)) {
				
			}
		}

		pw.println(DOTTY_FOOTER);

		pw.close();
	}

	public boolean checkReachability(Node preNode, Node postNode) {
		// TODO: <1 HIGH> finish check reachability
		return false;
	}
}
