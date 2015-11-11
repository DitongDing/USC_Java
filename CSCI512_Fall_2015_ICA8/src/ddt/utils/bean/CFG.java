package ddt.utils.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;

import ddt.utils.bean.node.MethodNode;
import ddt.utils.bean.node.Node;

public class CFG {
	// As for Java, all code should be executed by a method, so the basic
	// element in CFG is MethodNode.
	private Map<String, MethodNode> methodMap;

	public CFG(String classFilePath) throws Exception {
		methodMap = new HashMap<String, MethodNode>();

		// Parse class file
		JavaClass cls;
		cls = (new ClassParser(classFilePath)).parse();
		// Traverse methods and add them to map
		for (Method method : cls.getMethods())
			addMethodNode(new MethodNode(method, this));
	}

	public MethodNode getMethodNode(String ID) {
		MethodNode result = methodMap.get(ID);
		if (result == null) {
			result = new MethodNode(ID);
			addMethodNode(result);
		}
		return methodMap.get(ID);
	}

	private void addMethodNode(MethodNode methodNode) {
		assert (methodNode != null && methodNode.getID() != null);
		methodMap.put(methodNode.getID(), methodNode);
	}

	public void toDottyFile(String outputFileName) {
		// TODO: <3 LOW> finish CFG to dotty file function.
	}

	public Boolean checkReachability(Node preNode, Node postNode) {
		// TODO: <2 MID> finish check reachability function.
		return false;
	}
}
