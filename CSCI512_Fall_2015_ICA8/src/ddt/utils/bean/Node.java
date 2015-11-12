package ddt.utils.bean;

import java.util.HashSet;
import java.util.Set;

import org.apache.bcel.generic.*;

import ddt.utils.ComUtils;
import ddt.utils.Constant;

public class Node implements Comparable<Node> {
	protected Method method;
	// offset can be "entry"/"exit"
	protected String offset;
	protected String description;
	protected Set<Edge> inEdges;
	protected Set<Edge> outEdges;

	protected InstructionHandle instructionHandle;

	public Node(Method method, String offset, String description) {
		this.method = method;
		this.offset = offset;
		this.description = description;
		this.inEdges = new HashSet<Edge>();
		this.outEdges = new HashSet<Edge>();
	}

	public Node(Method method, InstructionHandle instructionHandle) {
		this(method, "" + instructionHandle.getPosition(), instructionHandle.getInstruction().toString());
		this.instructionHandle = instructionHandle;
	}

	public void initialize(Node nextNode) {
		if (instructionHandle != null) {
			Instruction instruction = instructionHandle.getInstruction();
			if (instruction instanceof InvokeInstruction) {
				InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
				String methodName = ComUtils.getMethodName(invokeInstruction, method.getCPG());
				Method invokeMethod = method.getMethod(methodName);
				if (invokeMethod != null) {
					linkTo(invokeMethod.getEntry(), String.format("Function call to %s", methodName));
					invokeMethod.getExit().linkTo(nextNode,
							String.format("Function %s finish, go back to %s", invokeMethod.getMethodName(), method.getMethodName()));
				} else {
					linkTo(nextNode, String.format("Function %s finish", methodName));
				}
			} else if (instruction instanceof IfInstruction) {
				IfInstruction ifInstruction = (IfInstruction) instruction;
				String targetOffset = ComUtils.getTargetOffset(ifInstruction);
				linkTo(targetOffset, String.format("If %s then goto %s", ComUtils.getIfMeanning(ifInstruction), targetOffset));
			} else if (instruction instanceof GotoInstruction) {
				GotoInstruction gotoInstruction = (GotoInstruction) instruction;
				String targetOffset = ComUtils.getTargetOffset(gotoInstruction);
				linkTo(targetOffset, String.format("Goto %s", targetOffset));
			} else if (instruction instanceof Select) {
				Select select = (Select) instruction;
				String defaultTargetOffset = "" + select.getTarget().getPosition();
				linkTo(defaultTargetOffset, String.format("Default goto %s", defaultTargetOffset));
				InstructionHandle[] targets = select.getTargets();
				int[] cases = select.getMatchs();
				for (int i = 0; i < targets.length; i++) {
					String targetOffset = "" + targets[i].getPosition();
					linkTo(targetOffset, String.format("If case %s goto %s", cases[i], targetOffset));
				}
			} else if (instruction instanceof RETURN) {
				linkTo(method.getExit(), "");
			} else {
				linkTo(nextNode, "");
			}
		} else {
			assert (offset.startsWith(Constant.ENTRY));
			linkTo(nextNode, "");
		}
	}

	private void linkTo(String offset, String description) {
		linkTo(method.getNode(offset), description);
	}

	private void linkTo(Node node, String description) {
		Edge edge = new Edge(this, node, description);
		this.addOutEdge(edge);
		node.addInEdge(edge);
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", offset, description);
	}

	public String toDottyString() {
		return offset.replace('.', '_');
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Node))
			return false;
		final Node otherNode = (Node) other;
		return method.equals(otherNode.method) && offset.equals(otherNode.offset);
	}

	public String getOffset() {
		return offset;
	}

	public String getDescription() {
		return description;
	}

	public Set<Edge> getInEdges() {
		return inEdges;
	}

	private void addInEdge(Edge edge) {
		assert (!inEdges.contains(edge));
		inEdges.add(edge);
	}

	public Set<Edge> getOutEdges() {
		return outEdges;
	}

	private void addOutEdge(Edge edge) {
		assert (!outEdges.contains(edge));
		outEdges.add(edge);
	}

	private int getIntOffset() {
		if (offset.startsWith(Constant.ENTRY))
			return -1;
		else if (offset.startsWith(Constant.EXIT))
			return Integer.MAX_VALUE;
		else
			return Integer.valueOf(offset);
	}

	@Override
	public int compareTo(Node node) {
		return getIntOffset() - node.getIntOffset();
	}
}