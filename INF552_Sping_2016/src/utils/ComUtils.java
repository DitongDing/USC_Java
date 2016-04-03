package utils;

import java.io.File;
import java.io.PrintWriter;

import Luxand.*;
import Luxand.FSDK.*;
import Luxand.FSDKCam.*;

public class ComUtils {
	static {
		// Check key
		int r = FSDK.ActivateLibrary(Constants.key);
		if (r == FSDK.FSDKE_OK) {
			FSDK.Initialize();
			// The first two parameter are related with spin detection; the third parameter is related with output quality, the higher the better, and slower.
			FSDK.SetFaceDetectionParameters(true, true, 384);
		} else {
			System.out.println("Evaluation key out of date");
			System.exit(-1);
		}
	}

	public static void extractFacialLocation_ByFolder(String inputFolder, String outputFile) throws Exception {
		File folder = new File(inputFolder);
		PrintWriter pw = new PrintWriter(outputFile);

		if (folder.isDirectory()) {
			File[] files = folder.listFiles();
			for (File file : files)
				if (file.getName().endsWith(".jpg")) {
					FSDK.TPoint[] points = extractFacialLocation_ByFile(file.getAbsolutePath());
					if (points != null) {
						pw.println(file.getName());
						for (FSDK.TPoint point : points)
							pw.print(String.format("%d,%d,", point.x, point.y));
						pw.println();
					}
				}
		}

		pw.close();
	}

	public static FSDK.TPoint[] extractFacialLocation_ByFile(String inputFile) {
		FSDK.TPoint[] result = null;

		HImage imageHandle = new HImage();
		if (FSDK.LoadImageFromFileW(imageHandle, inputFile) == FSDK.FSDKE_OK) {
			FSDK.TFacePosition.ByReference facePosition = new FSDK.TFacePosition.ByReference();
			if (FSDK.DetectFace(imageHandle, facePosition) == FSDK.FSDKE_OK) {
				FSDK_Features.ByReference facialFeatures = new FSDK_Features.ByReference();
				FSDK.DetectFacialFeaturesInRegion(imageHandle, (FSDK.TFacePosition) facePosition, facialFeatures);
				result = facialFeatures.features;
			} else
				System.out.println(String.format("No face detected in %s", inputFile));
		} else
			System.out.println(String.format("Load image %s fail", inputFile));

		return result;
	}
}