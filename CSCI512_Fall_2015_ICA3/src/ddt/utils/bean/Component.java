package ddt.utils.bean;

import java.util.List;

public class Component {
	private String name;
	private List<Interface> interfaces;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Interface> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<Interface> interfaces) {
		this.interfaces = interfaces;
	}
}
