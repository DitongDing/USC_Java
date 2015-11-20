package ddt.utils.bean.dejavu;

public class ClassFile {
	private String classFilePath;
	private String sourceFilePath;

	public ClassFile(String classFilePath, String sourceFilePath) {
		this.classFilePath = classFilePath;
		this.sourceFilePath = sourceFilePath;
	}

	public String getClassFilePath() {
		return classFilePath;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}
}
