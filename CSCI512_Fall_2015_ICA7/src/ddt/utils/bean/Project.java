package ddt.utils.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
	private List<String> totalFailedTestCases;
	private List<String> totalPassedTestCases;
	private Map<String, ClassFile> classFileMap;
	private Boolean isClosed;
	private String baseDir;

	public Project(String baseDir) {
		totalFailedTestCases = new ArrayList<String>();
		totalPassedTestCases = new ArrayList<String>();
		classFileMap = new HashMap<String, ClassFile>();
		isClosed = false;
		this.baseDir = baseDir;
	}

	public void addCount(String testCaseName, Boolean passed) {
		if (passed)
			totalPassedTestCases.add(testCaseName);
		else
			totalFailedTestCases.add(testCaseName);
	}
	
	public List<String> getTotalFailedTestCases() {
		return totalFailedTestCases;
	}

	public Integer getTotalFailedCount() {
		return totalFailedTestCases.size();
	}
	
	public List<String> getTotalPassedTestCases() {
		return totalPassedTestCases;
	}

	public Integer getTotalPassedCount() {
		return totalPassedTestCases.size();
	}

	public void addLine(String classFileName, String className, Integer lineNumber, String testCaseName,
			Boolean passed) {
		ClassFile classFile = classFileMap.get(classFileName);
		if (classFile == null) {
			classFile = new ClassFile(classFileName, className, this);
			classFileMap.put(classFileName, classFile);
		}
		classFile.addLine(lineNumber, testCaseName, passed);
	}

	public void close() {
		isClosed = true;
		for (ClassFile classFile : classFileMap.values())
			classFile.close();
	}

	public Map<String, List<Line>> getResult(Integer K) {
		Map<String, List<Line>> result = new HashMap<String, List<Line>>();

		for (ClassFile classFile : classFileMap.values()) {
			List<Line> lines = classFile.getResult(K);
			if (!lines.isEmpty())
				result.put(classFile.getClassFileName(), lines);
		}

		return result;
	}

	public Boolean isClosed() {
		return isClosed;
	}

	public File getFile(String fileName) {
		return new File(baseDir + fileName);
	}
}