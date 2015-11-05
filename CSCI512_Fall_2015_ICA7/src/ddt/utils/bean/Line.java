package ddt.utils.bean;

public class Line implements Comparable<Line> {
	private ClassFile classFile;
	private Integer lineNumber;
	private Integer failedCount;
	private Integer passedCount;
	private Double suspiciousness;
	private String lineContent;

	public Line(ClassFile classFile, Integer lineNumber, String lineContent) {
		this.classFile = classFile;
		this.lineNumber = lineNumber;
		failedCount = 0;
		passedCount = 0;
		suspiciousness = null;
		this.lineContent = lineContent;
	}

	public void addCount(Boolean passed) {
		if (passed)
			passedCount++;
		else
			failedCount++;
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
			final Double failedRate = failedCount / Double.valueOf(totalFailedCount);
			final Double passedRate = passedCount / Double.valueOf(totalPassedCount);
			suspiciousness = (failedRate) / (failedRate + passedRate);
		}
	}

	@Override
	public String toString() {
		return String.format("%-5d(%3.2f%%) | %s", lineNumber, suspiciousness * 100,
				lineContent == null ? "" : lineContent.trim());
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