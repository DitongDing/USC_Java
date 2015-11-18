package ddt.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ComUtils {
	public static String getTestCaseName(File file) {
		String fileName = file.getName();
		Boolean passed = fileName.endsWith("_true.xml");
		return passed ? fileName.substring(0, fileName.length() - "_true.xml".length())
				: fileName.substring(0, fileName.length() - "_false.xml".length());
	}

	public static Set<Integer> getExecutedLineNumbers(File coverageReport, String methodName) {
		Set<Integer> result = new HashSet<Integer>();

		try {
			String methodClassName = CFGUtils.getMethodClassName(methodName);
			String methodShortName = CFGUtils.getMethodShortName(methodName);
			String xPathString = String.format(".//class[@name='%s']//method[@name='%s']//line[@hits!=0]",
					methodClassName, methodShortName);

			XPath xPath = XPathFactory.newInstance().newXPath();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(coverageReport);
			NodeList lineNodes = (NodeList) xPath.evaluate(xPathString, document.getDocumentElement(),
					XPathConstants.NODESET);
			for (int i = 0; i < lineNodes.getLength(); i++) {
				Element lineNode = (Element) lineNodes.item(i);
				result.add(Integer.valueOf(lineNode.getAttribute("number")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean compareArrays(Object[] array0, Object[] array1) {
		boolean result = true;

		if (array0 == null)
			result = array1 == null;
		else if (array1 == null || array0.length != array1.length)
			result = false;
		else
			for (int i = 0; i < array0.length && result; i++)
				result = array0[i].equals(array1[i]);

		return result;
	}
}
