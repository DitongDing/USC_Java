package ddt.utils.bean;

import ddt.utils.bean.Node;

public class Edge implements Comparable<Edge> {
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
	// May be changed later.
	public String toString() {
		return String.format("%s -> %s [label = \"%s\"];", startNode.getOffset(), endNode.getOffset(), description);
	}

	public String toDottyString() {
		String result = String.format("\t%s -> %s", startNode.toDottyString(), endNode.toDottyString());
		if (!description.equals(""))
			result = String.format("%s [label = \"%s\"];", result, description);
		return result;
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

	@Override
	public int compareTo(Edge edge) {
		int result = startNode.compareTo(edge.startNode);
		if (result == 0)
			result = endNode.compareTo(edge.endNode);
		return result;
	}
}