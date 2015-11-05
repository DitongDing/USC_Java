package ddt.utils.bean;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Project {
	private Integer totalFailedCount;
	private Integer totalPassedCount;
	private Map<String, ClassFile> classFileMap;
	private Boolean isClosed;
	private String baseDir;

	public Project(String baseDir) {
		totalFailedCount = 0;
		totalPassedCount = 0;
		classFileMap = new HashMap<String, ClassFile>();
		isClosed = false;
		this.baseDir = baseDir;
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

	public void addLine(String classFileName, String className, Integer lineNumber, Boolean passed) {
		ClassFile classFile = classFileMap.get(classFileName);
		if (classFile == null) {
			classFile = new ClassFile(classFileName, className, this);
			classFileMap.put(classFileName, classFile);
		}
		classFile.addLine(lineNumber, passed);
	}

	public void close() {
		isClosed = true;
		for (ClassFile classFile : classFileMap.values())
			classFile.close();
	}

	public Map<String, List<Line>> getResult() {
		Map<String, List<Line>> result = new HashMap<String, List<Line>>();

		for (ClassFile classFile : classFileMap.values()) {
			List<Line> lines = classFile.getResult();
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