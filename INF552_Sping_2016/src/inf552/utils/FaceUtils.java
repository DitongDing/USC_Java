package inf552.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java.awt.Color;
import java.awt.image.BufferedImage;

import Luxand.*;
import Luxand.FSDK.*;
import Luxand.FSDKCam.*;
import bean.Face;

@SuppressWarnings("unused")
public class FaceUtils {
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

	public static List<Face> extractFacialLocation(File input) throws Exception {
		List<Face> result = new ArrayList<Face>();

		if (input != null) {
			File[] files = null;

			if (input.isDirectory())
				files = input.listFiles();
			else
				files = new File[] { input };

			for (File file : files)
				if (file.getName().endsWith(".jpg")) {
					Face face = extractFacialLocation_ByFile(file);
					if (face != null)
						result.add(face);
				}
		}

		return result;
	}

	private static Face extractFacialLocation_ByFile(File input) {
		Face result = null;
		String filePath = input.getAbsolutePath();

		HImage imageHandle = new HImage();
		if (FSDK.LoadImageFromFileW(imageHandle, filePath) == FSDK.FSDKE_OK) {
			FSDK.TFacePosition.ByReference facePosition = new FSDK.TFacePosition.ByReference();
			if (FSDK.DetectFace(imageHandle, facePosition) == FSDK.FSDKE_OK) {
				FSDK_Features.ByReference facialFeatures = new FSDK_Features.ByReference();
				FSDK.DetectFacialFeaturesInRegion(imageHandle, (FSDK.TFacePosition) facePosition, facialFeatures);
				result = new Face(input.getName(), filePath, facialFeatures.features);
			} else
				System.out.println(String.format("No face detected in %s", filePath));
		} else
			System.out.println(String.format("Load image %s fail", filePath));

		return result;
	}

	public static void extractAndPaint(File input, File output, int radius) throws Exception {
		if (input.isFile() && radius >= 0) {
			Face face = extractFacialLocation_ByFile(input);
			if (face != null) {
				BufferedImage original = ImageIO.read(input);
				for (FSDK.TPoint point : face.features)
					for (int x = point.x - radius; x <= point.x + radius; x++)
						for (int y = point.y - radius; y <= point.y + radius; y++)
							original.setRGB(x, y, new Color(255, 255, 255).getRGB());
				ImageIO.write(original, "jpg", output);
			}
		} else
			System.out.println("Input is not a file");
	}
}