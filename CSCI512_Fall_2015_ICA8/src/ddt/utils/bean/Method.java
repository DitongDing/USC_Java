package ddt.utils.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;

import ddt.utils.ComUtils;
import ddt.utils.Constant;

public class Method {
	protected CFG cfg;
	// className.functionname signature(/).
	protected String methodName;
	protected Node entry;
	protected Node exit;
	// offset -> node
	protected Map<String, Node> nodeMap;

	protected org.apache.bcel.classfile.Method method;

	public Method(CFG cfg, String methodName) {
		super();
		this.cfg = cfg;
		this.methodName = methodName;
		entry = new Node(this, String.format("%s", Constant.ENTRY), String.format("Entry for %s", methodName));
		exit = new Node(this, String.format("%s", Constant.EXIT), String.format("Exit for %s", methodName));
		nodeMap = new HashMap<String, Node>();
		addNode(entry);
		addNode(exit);
	}

	public Method(JavaClass cls, CFG cfg, org.apache.bcel.classfile.Method method) {
		this(cfg, ComUtils.getMethodName(cls, method));
		this.method = method;
	}

	public void initialize() {
		InstructionList instructionList = new InstructionList(method.getCode().getCode());
		InstructionHandle[] instructionHandles = instructionList.getInstructionHandles();

		// Add all node
		for (int i = 0; i < instructionHandles.length; i++)
			addNode(new Node(this, instructionHandles[i]));

		// Initialize each node
		Node currentNode = entry;
		Node nextNode = getNode("" + instructionHandles[0].getPosition());
		currentNode.initialize(nextNode);
		for (int i = 1; i < instructionHandles.length - 1; i++) {
			currentNode = nextNode;
			nextNode = getNode("" + instructionHandles[i].getPosition());
			currentNode.initialize(nextNode);
		}
		currentNode = nextNode;
		nextNode = exit;
		currentNode.initialize(nextNode);
	}

	public Method getMethod(String methodName) {
		return cfg.getMethod(methodName);
	}

	public Node getNode(String offset) {
		return nodeMap.get(offset);
	}

	private void addNode(Node node) {
		assert (node != null && node.getOffset() != null);
		nodeMap.put(node.getOffset(), node);
	}

	public ConstantPoolGen getCPG() {
		return cfg.getCPG();
	}

	@Override
	public String toString() {
		return String.format("%s", methodName);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Method))
			return false;
		final Method otherMethod = (Method) other;
		return methodName.equals(otherMethod.methodName);
	}

	public String getMethodName() {
		return methodName;
	}

	public Node getEntry() {
		return entry;
	}

	public Node getExit() {
		return exit;
	}
}
