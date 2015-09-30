package ddt.utils.bean;

import java.util.ArrayList;
import java.util.List;

public class Webapp {
	private List<Component> components;
	private Component loginComponent;

	public Webapp() {
		setComponents(new ArrayList<Component>());
	}

	@Override
	public String toString() {
		String result = "";

		for (Component component : components)
			result += component.toString() + "\n\n";

		return result;
	}

	public List<Component> getComponents() {
		return components;
	}

	public void addComponent(Component component) {
		components.add(component);
	}

	private void setComponents(List<Component> components) {
		this.components = components;
	}

	public Component getLoginComponent() {
		return loginComponent;
	}

	public void setLoginComponent(Component loginComponent) {
		this.loginComponent = loginComponent;
	}

}
