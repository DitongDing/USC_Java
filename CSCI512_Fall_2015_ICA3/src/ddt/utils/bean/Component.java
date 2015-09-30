package ddt.utils.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Component {
	private String name;
	private List<Interface> interfaces;
	private String target;

	public Component(Node node) {
		setName(((Element) node).getAttribute("name"));
		setInterfaces(node.getChildNodes());
		setTarget();
	}

	public String toString() {
		String result = name;

		Iterator<Interface> iterator = interfaces.iterator();
		while (iterator.hasNext())
			result += "\n====================\n" + iterator.next().toString();

		return result;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public List<Interface> getInterfaces() {
		return interfaces;
	}

	private void setInterfaces(NodeList interfaces) {
		this.interfaces = new ArrayList<Interface>();

		for (int i = 0; i < interfaces.getLength(); i++) {
			Node node = interfaces.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
				this.interfaces.add(new Interface(node));
		}
	}

	public String getTarget() {
		return target;
	}

	private void setTarget() {
		if (interfaces.size() == 0)
			target = null;
		else
			target = interfaces.get(0).getTarget();
		
		for(Interface i : interfaces)
			if(!i.getTarget().equals(target))
				throw new RuntimeException("Error");
	}
}
