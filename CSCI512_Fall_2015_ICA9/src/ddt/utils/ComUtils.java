package ddt.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		return passed ? fileName.substring(0, fileName.length() - "_true.xml".length())
				: fileName.substring(0, fileName.length() - "_false.xml".length());
	}

	public static Set<Integer> getExecutedLineNumbers(File coverageReport, Method method) {
		return getExecutedLineNumbers(coverageReport, method.getMethodName());
	}

	public static Set<Integer> getExecutedLineNumbers(File coverageReport, String methodFullName) {
		Set<Integer> result = new HashSet<Integer>();

		try {
			String methodClassName = CFGUtils.getMethodClassName(methodFullName);
			String methodShortName = CFGUtils.getMethodShortName(methodFullName);
			String xPathString = String.format(".//class[@name='%s']//method[@name='%s']//line[@hits!=0]",
					methodClassName, methodShortName);

			XPath xPath = XPathFactory.newInstance().newXPath();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setValidating(false);
			dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			Document document = dbf.newDocumentBuilder().parse(coverageReport);
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

	public static Set<TestCase> getTestSuite(Method method, String coverageDir, String testSuiteFile) {
		Set<TestCase> testSuite = new HashSet<TestCase>();

		Map<String, String> testCaseMap = ComUtils.readCucumberTestSuites(testSuiteFile);

		File dir = new File(coverageDir);
		assert (dir.exists());
		for (File file : dir.listFiles()) {
			String testCaseName = ComUtils.getTestCaseName(file);
			String testCaseContent = testCaseMap.get(testCaseName);
			testSuite.add(new TestCase(file, method, testCaseName, testCaseContent));
		}

		return testSuite;
	}

	public static Map<String, String> readCucumberTestSuites(String testSuiteFile) {
		Map<String, String> result = new HashMap<String, String>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(testSuiteFile));

			String line = br.readLine();

			while (line != null) {
				while (!line.startsWith("Scenario"))
					line = br.readLine();
				String testCaseName = line.substring("Scenario: ".length());
				String testCaseContent = "";
				while (true) {
					line = br.readLine();
					if (line == null || line.equals(""))
						break;
					testCaseContent += line + "\n";
				}
				result.put(testCaseName, testCaseContent);
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void writeTestSuite(String sourceFile0, String sourceFile1, Set<TestCase> testSuite, String output) {
		try {
			PrintWriter pw = new PrintWriter(output);

			pw.println("Feature: Reduced test suite\n");

			for (TestCase testCase : testSuite)
				pw.println(testCase.getCucumberTestCase());

			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void writeReport(Set<Node> nodes, Set<TestCase> testSuite0, Set<TestCase> testSuite1, String output) {
		try {
			PrintWriter pw = new PrintWriter(output);

			pw.println("Dangerous lines in source file 0:");
			Set<Integer> lineNumberSet = new HashSet<Integer>();
			for (Node node : nodes) {
				Integer lineNumber = node.getLineNumber();
				if (lineNumber != null)
					lineNumberSet.add(lineNumber);
			}
			List<Integer> lineNumbers = new ArrayList<Integer>(lineNumberSet);
			Collections.sort(lineNumbers);
			for (Integer lineNumber : lineNumbers)
				pw.println(String.format("%d", lineNumber));

			pw.println(
					"================================================================================\nOriginal Test Suites:");
			int count = 0;
			for (TestCase testCase : testSuite0)
				pw.println(String.format("%-3d\t%s", ++count, testCase.getTestCaseName()));

			count = 0;
			pw.println(
					"================================================================================\nReduced Test Suites:");
			for (TestCase testCase : testSuite1)
				pw.println(String.format("%-3d\t%s", ++count, testCase.getTestCaseName()));

			pw.println("================================================================================");

			pw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
