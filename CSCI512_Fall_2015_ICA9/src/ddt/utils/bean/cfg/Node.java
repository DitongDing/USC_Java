package ddt.utils.bean.cfg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.bcel.generic.*;

import ddt.utils.CFGUtils;
import ddt.utils.Constant;

public class Node implements Comparable<Node> {
	protected Method method;
	// offset can be "entry"/"exit"
	protected String offset;
	protected String description;
	protected Set<Edge> inEdges;
	protected Set<Edge> outEdges;

	protected InstructionHandle instructionHandle;
	protected Map<String, Edge> outEdgeMap;

	protected Set<Node> beReached;

	protected Integer lineNumber;

	public Node(Method method, String offset, String description) {
		this.method = method;
		this.offset = offset;
		this.description = description;
		this.inEdges = new HashSet<Edge>();
		this.outEdges = new HashSet<Edge>();
		this.beReached = new HashSet<Node>();
		this.outEdgeMap = new HashMap<String, Edge>();
		beReached.add(this);
		lineNumber = null;
	}

	public Node(Method method, InstructionHandle instructionHandle) {
		this(method, "" + instructionHandle.getPosition(), CFGUtils.getNodeDescription(instructionHandle, method.getCPG()));
		this.instructionHandle = instructionHandle;
		lineNumber = method.getLineNumber(Integer.valueOf(offset));
	}

	public void initialize(Node nextNode) {
		if (instructionHandle != null) {
			Instruction instruction = instructionHandle.getInstruction();
			if (instruction instanceof InvokeInstruction) {
				InvokeInstruction invokeInstruction = (InvokeInstruction) instruction;
				String methodName = CFGUtils.getMethodName(invokeInstruction, method.getCPG());
				// Method invokeMethod = method.getMethod(methodName);
				// if (invokeMethod != null) {
				// linkTo(invokeMethod.getEntry(), String.format("Function call to %s", methodName));
				// invokeMethod.getExit().linkTo(nextNode,
				// String.format("Function %s finish, go back to %s", invokeMethod.getMethodName(), method.getMethodName()));
				// } else {
				// linkTo(nextNode, String.format("Function %s finish", methodName));
				// }
				linkTo(nextNode, String.format("Function %s finish", CFGUtils.getMethodShortName(methodName)));
			} else if (instruction instanceof IfInstruction) {
				IfInstruction ifInstruction = (IfInstruction) instruction;
				String targetOffset = CFGUtils.getTargetOffset(ifInstruction);
				linkTo(targetOffset, String.format("If %s", CFGUtils.getIfMeanning(ifInstruction)));
				linkTo(nextNode, String.format("Else"));
			} else if (instruction instanceof GotoInstruction) {
				GotoInstruction gotoInstruction = (GotoInstruction) instruction;
				String targetOffset = CFGUtils.getTargetOffset(gotoInstruction);
				linkTo(targetOffset, String.format(""));
			} else if (instruction instanceof Select) {
				Select select = (Select) instruction;
				String defaultTargetOffset = "" + select.getTarget().getPosition();
				linkTo(defaultTargetOffset, String.format("Default"));
				InstructionHandle[] targets = select.getTargets();
				int[] cases = select.getMatchs();
				for (int i = 0; i < targets.length; i++) {
					String targetOffset = "" + targets[i].getPosition();
					linkTo(targetOffset, String.format("If case %s", cases[i], targetOffset));
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

		outEdgeMap.put(description, edge);
	}

	@Override
	public String toString() {
		return String.format("%s %s", offset, description);
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

	public void setBeReached(Set<Node> beReached) {
		this.beReached = beReached;
	}

	public Set<Node> getBeReached() {
		return beReached;
	}

	public boolean canBeReachedBy(Node node) {
		return beReached.contains(node);
	}

	@Override
	public int compareTo(Node node) {
		return getIntOffset() - node.getIntOffset();
	}

	private String toCPString() {
		return instructionHandle.getInstruction().toString(method.getCPG().getConstantPool());
	}

	public boolean equalsByInstruction(Node other) {
		return toCPString().equals(other.toCPString());
	}

	public Edge getOutEdgeByDescription(String description) {
		return outEdgeMap.get(description);
	}

	public Integer getLineNumber() {
		return lineNumber;
	}
}