package test;

import java.io.File;

import inf552.utils.FaceUtils;

public class TestFaceSDK {
	public static String input = "input/TestFaceSDK";
	public static String output = "output/TestFaceSDK";

	public static void main(String[] args) throws Exception {
		File dir = new File(input);
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (File file : files)
				if (file.getName().endsWith(".jpg"))
					FaceUtils.extractAndPaint(file, new File(output + "/" + file.getName()), 1);
		}
	}
}