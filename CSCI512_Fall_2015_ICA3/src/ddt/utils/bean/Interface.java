package ddt.utils.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Interface {
	private List<String> methods;
	private String target;
	private ArrayList<Parameter> parameters;
	private Map<String, Parameter> parametersMap;

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

	public ArrayList<Parameter> getParameters() {
		return parameters;
	}

	private void setParameters(NodeList parameters) {
		this.parameters = new ArrayList<Parameter>();
		this.parametersMap = new HashMap<String, Parameter>();

		for (int i = 0; i < parameters.getLength(); i++) {
			Node node = parameters.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Parameter parameter = new Parameter(node);
				this.parameters.add(parameter);
				this.parametersMap.put(parameter.getName(), parameter);
			}
		}

		// assert parameters contains FormAction and FormName, if it contains parameters.
		if (this.parameters.size() != 0
				&& !(this.parametersMap.containsKey("FormAction") && this.parametersMap.containsKey("FormName")))
			throw new RuntimeException("Interface not contains formAction and formName");
	}

	public Set<Data> getDataSet() {
		return dataSet;
	}

	private void generate(ListIterator<Parameter> li, List<String[]> data) {
		if (dataSet.size() < 100) {
			if (!li.hasNext()) {
				dataSet.add(new Data(data));
			} else {
				Parameter current = li.next();
				Iterator<String> currentIterator = current.getValues().iterator();
				while (currentIterator.hasNext()) {
					data.add(new String[] { current.getName(), currentIterator.next() });
					generate(li, data);
					data.remove(data.size() - 1);
				}
				li.previous();
			}
		}
	}

	// TODO: finish setDataTupleCombination(). Finish Cartesian product first
	private void setDataSet() {
		this.dataSet = new HashSet<Data>();

		ListIterator<Parameter> li = parameters.listIterator();
		List<String[]> data = new ArrayList<String[]>();

		generate(li, data);

		// FormAction and FormName error

		// Sorting and Sorted tuple should only exists some combination

		// Insert 20 times.
		// Delete 30 times.
	}
}
