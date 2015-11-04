package ddt.utils.bean;

public class Line implements Comparable<Line> {
	private ClassFile classFile;
	private Integer lineNumber;
	private Integer failedCount;
	private Integer passedCount;
	private Double suspiciousness;

	public Line(ClassFile classFile, Integer lineNumber) {
		this.classFile = classFile;
		this.lineNumber = lineNumber;
		failedCount = 0;
		passedCount = 0;
		suspiciousness = null;
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

	public void close() {
		final Integer totalFailedCount = classFile.getTotalFailedCount();
		final Integer totalPassedCount = classFile.getTotalPassedCount();
		final Double failedRate = failedCount / Double.valueOf(totalFailedCount);
		final Double passedRate = passedCount / Double.valueOf(totalPassedCount);
		suspiciousness = (failedRate) / (failedRate + passedRate);
	}

	@Override
	public String toString() {
		return "" + lineNumber + "\t" + (suspiciousness * 100) + "%";
	}

	@Override
	public int compareTo(Line arg0) {
		assert(classFile.isClosed());

		int result = suspiciousness.compareTo(arg0.suspiciousness);
		if (result == 0)
			result = lineNumber.compareTo(arg0.lineNumber);

		return result;
	}
}