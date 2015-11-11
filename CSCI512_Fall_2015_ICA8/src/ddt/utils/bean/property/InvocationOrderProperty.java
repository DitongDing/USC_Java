package ddt.utils.bean.property;

import ddt.utils.bean.Node;

public class InvocationOrderProperty {
	private Node preNode;
	private Node postNode;

	public InvocationOrderProperty(Node preNode, Node postNode) {
		super();
		this.preNode = preNode;
		this.postNode = postNode;
	}

	@Override
	public String toString() {
		return String.format("Pre Node = \"%s\", Post Node = \"%s\"", preNode.toString(), postNode.toString());
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof InvocationOrderProperty))
			return false;
		final InvocationOrderProperty otherInvocationOrderProperty = (InvocationOrderProperty) other;
		return this.preNode.equals(otherInvocationOrderProperty.preNode) && this.postNode.equals(otherInvocationOrderProperty.postNode);
	}

	public Node getPreNode() {
		return preNode;
	}

	public Node getPostNode() {
		return postNode;
	}
}