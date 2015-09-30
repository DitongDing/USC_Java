package ddt.utils.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
	private Set<String> valuesSet;
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
		this.type = type.toLowerCase();
	}

	public List<String> getValues() {
		return values;
	}

	private void setValues(String values) {
		this.values = new ArrayList<String>();
		this.valuesSet = new HashSet<String>();

		StringTokenizer st = new StringTokenizer(values, "[], ");
		while (st.hasMoreTokens())
			addValue(st.nextToken());

		addTypeValue();
		addNameValue();
	}

	private void addValue(String value) {
		if (!valuesSet.contains(value)) {
			this.values.add(value);
			this.valuesSet.add(value);
		}
	}

	// TODO: add value by type.
	// Contains default correct/type error/null
	// Three types: STRING, INTEGER, DOUBLE
	private void addTypeValue() {
		if ("string".equals(type)) {
			// default correct
			addValue("abc");
			// type error

		} else if ("integer".equals(type)) {
			// default correct
			addValue("0");
			// type error
			addValue("x");
			addValue("0.1");
		} else if ("double".equals(type)) {
			// default correct
			addValue("0.0");
			// type error
			addValue("x");
		}
		// null
		addValue("");
	}

	// TODO: add value by name
	// minus
	private void addNameValue() {
		// if (name.equals("FormAction")) {
		//
		// } else if (name.contains("Sorting") || name.contains("Sorted")) {
		// for (int i = 0; i < 5; i++)
		// addValue(Integer.toString(i));
		// }
	}

	@Override
	public int compareTo(Parameter arg0) {
		return name.compareTo(arg0.name);
	}
}