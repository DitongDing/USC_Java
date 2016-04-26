package test;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import Luxand.*;
import inf552.bean.Face;
import inf552.utils.FaceUtils;

public class TestExtractFacialLocation {
	public static String input = "input/jaffe";
	public static String output = "output/jaffe";

	public static void main(String[] args) throws Exception {

		List<Face> result = FaceUtils.extractFacialLocation(new File(input));

		PrintWriter pw = new PrintWriter(output);

		for (Face face : result) {
			pw.println(face.fileName);
			for (FSDK.TPoint point : face.features)
				pw.print(String.format("%d,%d,", point.x, point.y));
			pw.println();
		}

		pw.close();
	}
}