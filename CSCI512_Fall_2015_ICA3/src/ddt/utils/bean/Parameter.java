package ddt.utils.bean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

// TODO: generate possible datatuple. null, invalid value, correct value.
// TODO: think about default value?
// TODO: think about how to combine potential values for an interface.
public class Parameter implements Comparable<Parameter> {
	private String name;
	private String type;
	private List<String> values;
	// The meaning of lines remains unknown.
	@SuppressWarnings("unused")
	private List<String> lines;

	public Parameter(Node node) {
		setName(((Element) node).getAttribute("name"));
		setType(((Element) node).getAttribute("type"));
		setValues(((Element) node).getAttribute("values"));
	}

	@Override
	public String toString() {
		String result = name + ":" + type + "=[";
		Iterator<String> iterator = values.iterator();
		if (iterator.hasNext()) {
			result += iterator.next();
			while (iterator.hasNext())
				result += "," + iterator.next();
		}
		result += "]";

		return result;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	private void setType(String type) {
		this.type = type;
	}

	public List<String> getValues() {
		return values;
	}

	// TODO: complete set values by rules.
	private void setValues(String values) {
		this.values = new ArrayList<String>();

		StringTokenizer st = new StringTokenizer(values, "[], ");
		while (st.hasMoreTokens())
			this.values.add(st.nextToken());
	}

	@Override
	public int compareTo(Parameter arg0) {
		return name.compareTo(arg0.name);
	}
}