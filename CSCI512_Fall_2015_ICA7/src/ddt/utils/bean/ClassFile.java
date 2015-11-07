package ddt.utils.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ClassFile {
	private String classFileName;
	@SuppressWarnings("unused")
	private String className;
	private Project project;
	private Map<Integer, Line> lineMap;
	private List<String> lineContents;

	public ClassFile(String classFileName, String className, Project project) {
		this.classFileName = classFileName;
		this.className = className;
		this.project = project;
		lineMap = new HashMap<Integer, Line>();

		try {
			BufferedReader br = new BufferedReader(new FileReader(project.getFile(classFileName)));
			lineContents = new ArrayList<String>();
			lineContents.add("===line 0===");

			String line = br.readLine();
			while (line != null) {
				lineContents.add(line);
				line = br.readLine();
			}

			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getClassFileName() {
		return classFileName;
	}

	public Boolean isClosed() {
		return project.isClosed();
	}

	public List<String> getTotalFailedTestCases() {
		return project.getTotalFailedTestCases();
	}

	public Integer getTotalFailedCount() {
		return project.getTotalFailedCount();
	}

	public List<String> getTotalPassedTestCases() {
		return project.getTotalPassedTestCases();
	}

	public Integer getTotalPassedCount() {
		return project.getTotalPassedCount();
	}

	public void addLine(Integer lineNumber, String testCaseName, Boolean passed) {
		String lineContent = lineContents.get(lineNumber);
		String trimedLineContent = lineContent.trim();
		if (!(trimedLineContent.equals("") || trimedLineContent.equals("{") || trimedLineContent.equals("}")
				|| trimedLineContent.equals(";"))) {
			Line line = lineMap.get(lineNumber);
			if (line == null) {
				line = new Line(this, lineNumber, lineContent);
				lineMap.put(lineNumber, line);
			}

			line.addCount(testCaseName, passed);
		}
	}

	public void close() {
		for (Line line : lineMap.values())
			line.close();
	}

	public List<Line> getResult(Integer K) {
		if (K == null)
			return getSortedLines();
		else
			return getTopKLines(K);
	}

	private List<Line> getTopKLines(Integer K) {
		PriorityQueue<Line> heap = new PriorityQueue<Line>(K);
		List<Line> result = new ArrayList<Line>(K);

		for (Line line : lineMap.values())
			if (line.getSuspiciousness() != 0) {
				if (heap.size() == K && heap.peek().compareTo(line) < 0)
					heap.poll();
				if (heap.size() < K)
					heap.offer(line);
			}

		while (!heap.isEmpty())
			result.add(0, heap.poll());

		return result;
	}

	private List<Line> getSortedLines() {
		assert(project.isClosed());

		List<Line> result = new ArrayList<Line>(lineMap.values());
		Collections.sort(result, new Comparator<Line>() {
			@Override
			public int compare(Line o1, Line o2) {
				return -o1.compareTo(o2);
			}
		});

		return result;
	}
}