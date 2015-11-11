package ddt.utils.bean.node;

import java.util.HashSet;
import java.util.Set;

import ddt.utils.bean.Edge;

public class Node {
	protected String ID;
	protected Set<Edge> inEdges;
	protected Set<Edge> outEdges;

	public Node(String ID) {
		super();
		this.ID = ID;
		this.inEdges = new HashSet<Edge>();
		this.outEdges = new HashSet<Edge>();
	}

	@Override
	// May change later
	public String toString() {
		return ID;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Node))
			return false;
		final Node otherNode = (Node) other;
		return this.ID.equals(otherNode.ID);
	}

	public String getID() {
		return ID;
	}

	public Set<Edge> getInEdges() {
		return inEdges;
	}

	public void addInEdge(Edge edge) {
		assert (edge != null && this.equals(edge.getEndNode()) && !inEdges.contains(edge));
		inEdges.add(edge);
	}

	public Set<Edge> getOutEdges() {
		return outEdges;
	}

	public void addOutEdge(Edge edge) {
		assert (edge != null && this.equals(edge.getStartNode()) && !outEdges.contains(edge));
		outEdges.add(edge);
	}
}
