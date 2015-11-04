package ddt.utils.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
	private Integer totalFailedCount;
	private Integer totalPassedCount;
	private Map<String, ClassFile> classFileMap;
	private Boolean isClosed;
	private Map<String, List<Line>> sortedLines;

	public Project() {
		totalFailedCount = 0;
		totalPassedCount = 0;
		classFileMap = new HashMap<String, ClassFile>();
		isClosed = false;
	}

	public void addCount(Boolean passed) {
		if (passed)
			totalPassedCount++;
		else
			totalFailedCount++;
	}

	public Integer getTotalFailedCount() {
		return totalFailedCount;
	}

	public Integer getTotalPassedCount() {
		return totalPassedCount;
	}

	public void addLine(String classFileName, Integer lineNumber, Boolean passed) {
		ClassFile classFile = classFileMap.get(classFileName);
		if (classFile == null) {
			classFile = new ClassFile(classFileName, this);
			classFileMap.put(classFileName, classFile);
		}
		classFile.addLine(lineNumber, passed);
	}

	public void close() {
		isClosed = true;
		for (ClassFile classFile : classFileMap.values())
			classFile.close();

		sortedLines = new HashMap<String, List<Line>>();
		for (ClassFile classFile : classFileMap.values())
			sortedLines.put(classFile.getClassFileName(), classFile.getSortedLines());
	}

	public Map<String, List<Line>> getSortedLines() {
		return sortedLines;
	}

	public Boolean isClosed() {
		return isClosed;
	}
}