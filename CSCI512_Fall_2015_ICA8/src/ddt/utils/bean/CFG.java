package ddt.utils.bean;

import java.util.Map;

import ddt.utils.bean.property.InvocationOrderProperty;

public class CFG {
	private Map<String, Node> nodeMap;

	public CFG(String classFilePath) {
		Node entry = new Node("entry");
		Node exit = new Node("exit");

		// TODO: <1 HIGH> finish CFG constructor
	}

	private void addNode(Node node) {
		// TODO: <1 HIGH> finish add node function in CFG
	}

	private void addEdge(Node node) {
		// TODO: <1 HIGH> finish add node function in CFG
	}

	public void toDottyFile(String outputFileName) {
		// TODO: <3 LOW> finish CFG to dotty file function.
	}

	public Boolean checkProperty(InvocationOrderProperty property) {
		// TODO: <2 MID> finish check invocation order property function.
		return false;
	}

	public Boolean checkReachability(Node preNode, Node postNode) {
		// TODO: <2 MID> finish check reachability function.
		return false;
	}
}
