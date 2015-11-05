package ddt.utils;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;

import ddt.utils.bean.Line;
import ddt.utils.bean.Project;

public class TarantulaUtils {
	private String coverageDir;
	private String resultFile;

	private Project project;
	private Map<String, List<Line>> result;

	public static final Integer K = 20;

	public TarantulaUtils(String coverageDir, String resultFile, String baseDir) {
		this.coverageDir = coverageDir;
		this.resultFile = resultFile;
		this.project = new Project(baseDir);
	}

	public void analysis() {
		try {
			// handle report input
			read();

			// calculate suspicious
			calculate();

			// write result to result file
			write();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void read() throws Exception {
		File dir = new File(coverageDir);
		assert(dir.exists());

		XPath xPath = XPathFactory.newInstance().newXPath();
		for (File file : dir.listFiles()) {
			Boolean passed = file.getName().endsWith("_true.xml");
			project.addCount(passed);

			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			NodeList classFileNodes = (NodeList) xPath.evaluate(".//class[@line-rate!=0]",
					document.getDocumentElement(), XPathConstants.NODESET);
			for (int i = 0; i < classFileNodes.getLength(); i++) {
				Element classFileNode = (Element) classFileNodes.item(i);
				NodeList lineNodes = (NodeList) xPath.evaluate(".//line[@hits!=0]", classFileNode,
						XPathConstants.NODESET);

				String classFileName = "/" + classFileNode.getAttribute("filename");
				String className = classFileNode.getAttribute("name");
				for (int j = 0; j < lineNodes.getLength(); j++) {
					Element lineNode = (Element) lineNodes.item(j);
					Integer lineNumber = Integer.valueOf(lineNode.getAttribute("number"));
					project.addLine(classFileName, className, lineNumber, passed);
				}
			}
		}
	}

	private void calculate() {
		project.close();
		// The result is for each file
		result = project.getResult();
	}

	private void write() throws Exception {
		PrintWriter pw = new PrintWriter(resultFile);

		// for (Entry<String, List<Line>> entry : result.entrySet()) {
		// pw.println("========================================");
		// pw.println("File: " + entry.getKey());
		// pw.println("Lines: ");
		// for (Line line : entry.getValue())
		// pw.println("\t" + line.toString());
		// pw.println();
		// }

		ArrayList<Line> lines = new ArrayList<Line>();
		for (List<Line> linesPerFile : result.values())
			lines.addAll(linesPerFile);

		Collections.sort(lines, new Comparator<Line>() {
			@Override
			public int compare(Line o1, Line o2) {
				return -o1.compareTo(o2);
			}
		});

		Integer count = 0;
		for (Line line : lines)
			if (count++ < K)
				pw.println(String.format("%-40s| %s", line.getClassFileName(), line.toString()));
			else
				break;

		pw.close();
	}
}
