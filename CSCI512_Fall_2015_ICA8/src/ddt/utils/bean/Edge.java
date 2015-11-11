package ddt.utils.bean;

import ddt.utils.bean.node.Node;

public class Edge {
	// Assume <startNode, endNode> is the ID for Edge.
	protected Node startNode;
	protected Node endNode;
	protected String description;

	public Edge(Node inNode, Node outNode, String description) {
		this.startNode = inNode;
		this.endNode = outNode;
		this.description = description;
	}

	@Override
	// May be changed later. Assume we will use it in output to dotty.
	public String toString() {
		return String.format("%s -> %s [label = \"%s\"];", startNode.getID(), endNode.getID(), description);
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Edge))
			return false;
		final Edge otherEdge = (Edge) other;
		return this.startNode.equals(otherEdge.startNode) && this.endNode.equals(otherEdge.endNode);
	}

	public Node getStartNode() {
		return startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public String getDescription() {
		return description;
	}
}