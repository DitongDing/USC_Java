package ddt.utils.bean;

import java.util.HashSet;
import java.util.Set;

public class Node {
	// Assume ID should be unique in one graph. Can use line number instead
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
		assert(edge != null);
		assert(this.equals(edge.getEndNode()));
		assert(!inEdges.contains(edge));
		inEdges.add(edge);
	}

	public Set<Edge> getOutEdges() {
		return outEdges;
	}

	public void addOutEdge(Edge edge) {
		assert(edge != null);
		assert(this.equals(edge.getStartNode()));
		assert(!outEdges.contains(edge));
		outEdges.add(edge);
	}
}
