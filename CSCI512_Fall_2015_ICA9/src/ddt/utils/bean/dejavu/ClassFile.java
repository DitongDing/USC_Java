package ddt.utils.bean.dejavu;

import ddt.utils.ComUtils;

public class ClassFile {
	private String classFileDir;
	private String classFileName;
	private String[] methodNames;

	public String getClassFilePath() {
		return classFileDir + "/" + classFileName;
	}

	public String[] getMethodNames() {
		return methodNames;
	}

	public ClassFile(String classFileDir, String classFileName, String[] methodNames) {
		super();
		this.classFileDir = classFileDir;
		this.classFileName = classFileName;
		this.methodNames = methodNames;
	}

	@Override
	public boolean equals(Object other) {
		boolean result = other != null && other instanceof ClassFile;
		final ClassFile otherClassFile = (ClassFile) other;

		if (result && (!classFileName.equals(otherClassFile.classFileName)
				|| !ComUtils.compareArrays(methodNames, otherClassFile.methodNames)))
			result = false;

		return result;
	}
}