package ddt.utils.bean;

import java.util.List;

public class Parameter {
	private String name;
	private String type;
	private List<String> values;
	// The meaning of lines remains unknown.
	@SuppressWarnings("unused")
	private List<String> lines;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}
}