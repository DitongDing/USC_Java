package ddt.utils.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Interface {
	private List<String> methods;
	private String target;
	private List<Parameter> parameters;

	private Set<Data> dataSet;

	public Interface(Node node) {
		setMethods(((Element) node).getAttribute("method"));
		setTarget(((Element) node).getAttribute("target"));
		setParameters(node.getChildNodes());
		setDataSet();
	}

	public String toString() {
		String result = target + ":";

		Iterator<String> im = methods.iterator();
		if (im.hasNext()) {
			result += im.next();
			while (im.hasNext())
				result += "|" + im.next();
		}
		result += "{\n";
		Iterator<Parameter> ip = parameters.iterator();
		while (ip.hasNext())
			result += "\t" + ip.next().toString() + "\n";
		result += "}";

		return result;
	}

	public List<String> getMethods() {
		return methods;
	}

	private void setMethods(String methods) {
		this.methods = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(methods, "|");
		while (st.hasMoreTokens())
			this.methods.add(st.nextToken());
	}

	public String getTarget() {
		return target;
	}

	private void setTarget(String target) {
		this.target = target;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	private void setParameters(NodeList parameters) {
		this.parameters = new ArrayList<Parameter>();
		boolean formAction = false;
		boolean formName = false;

		for (int i = 0; i < parameters.getLength(); i++) {
			Node node = parameters.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Parameter parameter = new Parameter(node);
				this.parameters.add(parameter);
				if (!formAction)
					formAction = parameter.getName().equals("FormAction");
				if (!formName)
					formName = parameter.getName().equals("FormName");
			}
		}

		// assert parameters contains FormAction and FormName, if it contains parameters.
		if (this.parameters.size() != 0 && !(formAction && formName))
			throw new RuntimeException("Interface not contains formAction and fz");

		Collections.sort(this.parameters);
	}

	public Set<Data> getDataSet() {
		return dataSet;
	}

	// TODO: finish setDataTupleCombination().
	private void setDataSet() {
		this.dataSet = new HashSet<Data>();

		// FormAction and FormName error

		// Sorting and Sorted tuple should only exists some combination
	}
}
