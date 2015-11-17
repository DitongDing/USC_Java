package ddt.utils.bean.tarantula;

import java.util.HashSet;
import java.util.Set;

public class Line implements Comparable<Line> {
	private ClassFile classFile;
	private Integer lineNumber;
	private Set<String> failedTestCases;
	private Set<String> passedTestCases;
	private Double suspiciousness;
	private String lineContent;

	public Line(ClassFile classFile, Integer lineNumber, String lineContent) {
		this.classFile = classFile;
		this.lineNumber = lineNumber;
		failedTestCases = new HashSet<String>();
		passedTestCases = new HashSet<String>();
		suspiciousness = null;
		this.lineContent = lineContent;
	}

	public void addCount(String testCaseName, Boolean passed) {
		if (passed)
			passedTestCases.add(testCaseName);
		else
			failedTestCases.add(testCaseName);
	}

	public Integer getLineNumber() {
		return lineNumber;
	}

	public Double getSuspiciousness() {
		return suspiciousness;
	}

	public void close() {
		final Integer totalFailedCount = classFile.getTotalFailedCount();
		final Integer totalPassedCount = classFile.getTotalPassedCount();
		if (totalFailedCount == 0)
			suspiciousness = 0.0;
		else if (totalPassedCount == 0)
			suspiciousness = 1.0;
		else {
			final Double failedRate = failedTestCases.size() / Double.valueOf(totalFailedCount);
			final Double passedRate = passedTestCases.size() / Double.valueOf(totalPassedCount);
			suspiciousness = (failedRate) / (failedRate + passedRate);
		}
	}

	public String toSimpleString() {
		return String.format("%-5d(%3.2f%%) | %s", lineNumber, suspiciousness * 100,
				lineContent == null ? "" : lineContent.trim());
	}

	// in the format of:
	// filename(linenumber)[: linecontent]
	// \tsuspiciousness: (\d)
	// \tpassed test cases:
	// \t\t[]
	// \t failed test cases:
	// \t\t[]
	public String toFullString() {
		String result = "========================================\n";
		result += String.format("%s(%d)%s\n", classFile.getClassFileName(), lineNumber,
				lineContent == null ? "" : (": " + lineContent.trim()));
		result += String.format("\tSuspiciousness: %3.2f%%\n", (suspiciousness * 100));
		result += String.format("\tExecuted in Passed Test Cases:\n");
		for (String testCaseName : classFile.getTotalPassedTestCases())
			result += String.format("\t\t%s %s\n", passedTestCases.contains(testCaseName) ? "[✓]" : "[ ]",
					testCaseName);
		result += String.format("\tExecuted in Failed Test Cases:\n");
		for (String testCaseName : classFile.getTotalFailedTestCases())
			result += String.format("\t\t%s %s\n", failedTestCases.contains(testCaseName) ? "[✓]" : "[ ]",
					testCaseName);
		return result;
	}

	@Override
	public int compareTo(Line arg0) {
		assert(classFile.isClosed());

		int result = suspiciousness.compareTo(arg0.suspiciousness);
		if (result == 0)
			result = -lineNumber.compareTo(arg0.lineNumber);

		return result;
	}

	public String getClassFileName() {
		return classFile.getClassFileName();
	}
}