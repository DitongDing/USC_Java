package ddt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import ddt.utils.bean.cfg.Method;
import ddt.utils.bean.cfg.Node;
import ddt.utils.bean.dejavu.TestCase;

public class ComUtils {
	public static String getTestCaseName(File file) {
		String fileName = file.getName();
		Boolean passed = fileName.endsWith("_true.xml");
		return passed ? fileName.substring(0, fileName.length() - "_true.xml".length()) : fileName.substring(0, fileName.length() - "_false.xml".length());
	}

	public static Set<Integer> getExecutedLineNumbers(File coverageReport, Method method) {
		return getExecutedLineNumbers(coverageReport, method.getMethodName());
	}

	public static Set<Integer> getExecutedLineNumbers(File coverageReport, String methodFullName) {
		Set<Integer> result = new HashSet<Integer>();

		try {
			String methodClassName = CFGUtils.getMethodClassName(methodFullName);
			String methodShortName = CFGUtils.getMethodShortName(methodFullName);
			String xPathString = String.format(".//class[@name='%s']//method[@name='%s']//line[@hits!=0]", methodClassName, methodShortName);

			XPath xPath = XPathFactory.newInstance().newXPath();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document document = dbf.newDocumentBuilder().parse(coverageReport);
			NodeList lineNodes = (NodeList) xPath.evaluate(xPathString, document.getDocumentElement(), XPathConstants.NODESET);
			for (int i = 0; i < lineNodes.getLength(); i++) {
				Element lineNode = (Element) lineNodes.item(i);
				result.add(Integer.valueOf(lineNode.getAttribute("number")));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	@SuppressWarnings("unused")
	private static boolean compareArrays(Object[] array0, Object[] array1) {
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

	public static Set<TestCase> getTestSuite(Method method, String coverageDir) {
		Set<TestCase> testSuite = new HashSet<TestCase>();

		File dir = new File(coverageDir);
		assert (dir.exists());
		for (File file : dir.listFiles())
			testSuite.add(new TestCase(file, method));

		return testSuite;
	}

	public static void writeLinesByNodes(String sourcePath, Set<Node> nodes, String output) {
		try {
			PrintWriter pw = new PrintWriter(output);
			BufferedReader br = new BufferedReader(new FileReader(sourcePath));

			List<String> lineContents = new ArrayList<String>();
			lineContents.add("===line 0===");
			String line = br.readLine();
			while (line != null) {
				lineContents.add(line);
				line = br.readLine();
			}

			Set<Integer> lineNumberSet = new HashSet<Integer>();
			for (Node node : nodes) {
				Integer lineNumber = node.getLineNumber();
				if (lineNumber != null)
					lineNumberSet.add(lineNumber);
			}
			List<Integer> lineNumbers = new ArrayList<Integer>(lineNumberSet);
			Collections.sort(lineNumbers);
			for (Integer lineNumber : lineNumbers)
				pw.println(String.format("%d: %s", lineNumber, lineContents.get(lineNumber)));

			pw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
