package ddt.utils.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassFile {
	private String classFileName;
	private Project project;
	private Map<Integer, Line> lineMap;

	public ClassFile(String classFileName, Project project) {
		this.classFileName = classFileName;
		this.project = project;
		lineMap = new HashMap<Integer, Line>();
	}

	public String getClassFileName() {
		return classFileName;
	}

	public Boolean isClosed() {
		return project.isClosed();
	}

	public Integer getTotalFailedCount() {
		return project.getTotalFailedCount();
	}

	public Integer getTotalPassedCount() {
		return project.getTotalPassedCount();
	}

	public void addLine(Integer lineNumber, Boolean passed) {
		Line line = lineMap.get(lineNumber);
		if (line == null) {
			line = new Line(this, lineNumber);
			lineMap.put(lineNumber, line);
		}

		line.addCount(passed);
	}

	public void close() {
		for (Line line : lineMap.values())
			line.close();
	}

	public List<Line> getSortedLines() {
		assert(project.isClosed());

		List<Line> result = new ArrayList<Line>(lineMap.values());
		Collections.sort(result);

		return result;
	}
}