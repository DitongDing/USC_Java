package ddt.utils.bean.node;

import java.util.HashMap;
import java.util.Map;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionList;

import ddt.utils.bean.CFG;

public class MethodNode extends Node {
	protected Node entry;
	protected Node exit;
	protected Map<String, Node> nodeMap;

	public MethodNode(String ID) {
		super(ID);
		entry = new Node("entry");
		exit = new Node("exit");
		nodeMap = new HashMap<String, Node>();
		addNode(entry);
		addNode(exit);
	}

	// The ID for MethodNode is its function signature
	public MethodNode(Method method, CFG cfg) {
		// TODO: <1 HIGH> finish constructor of MethodNode.
		this(method.getName());

		InstructionList il = new InstructionList(method.getCode().getCode());
		Instruction[] instructionList = il.getInstructions();
		int[] instructionOffset = il.getInstructionPositions();
		for (int i = 0; i < instructionOffset.length; i++)
			addNode(new LineNode(instructionOffset[i], instructionList[i]));
	}

	private void addEdge(Node node) {
		// TODO: <1 HIGH> finish add node function in CFG
	}

	private void addNode(Node node) {
		assert (node != null && node.getID() != null);
		nodeMap.put(node.getID(), node);
	}
}