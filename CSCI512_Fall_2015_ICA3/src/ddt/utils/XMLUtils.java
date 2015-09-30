package ddt.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ddt.utils.bean.Component;

public class XMLUtils {
	public static List<Component> parseXML(String filepath) throws Exception {
		List<Component> components = new ArrayList<Component>();

		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(filepath));
		Element xml = document.getDocumentElement();

		NodeList children = xml.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node node = children.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE)
				components.add(new Component(node));
		}

		return components;
	}
}