package ddt.utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ddt.utils.bean.Component;
import ddt.utils.bean.Webapp;

public class XMLUtils {
	public static Webapp parseWebapp(String filepath) throws Exception {
		Webapp webapp = new Webapp();

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filepath));
		Element xml = document.getDocumentElement();

		NodeList children = xml.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Component component = new Component(node);
				// Assume there are only one login component per webapp.
				if (component.getName().toLowerCase().contains("login"))
					webapp.setLoginComponent(component);
				else
					webapp.addComponent(component);
			}
		}

		return webapp;
	}
}