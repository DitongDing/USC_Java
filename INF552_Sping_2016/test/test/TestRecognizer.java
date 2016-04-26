package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import inf552.utils.Recognizer;

public class TestRecognizer {

	public static void main(String[] args) {
		List<String> filePaths = new ArrayList<String>(Arrays.asList(new String[] { "input/jaffe/KA.HA1.29.jpg", "input/jaffe/KA.AN1.39.jpg",
				"input/TestFaceSDK/4242435-face.jpg", "input/TestFaceSDK/Get-that-fresh-face-celebrity-look-with-Bramis-Facial-Rejuvenation-Clinic.jpg",
				"input/TestFaceSDK/hairstyles-that-flatter-your-face-round-faces.jpg" }));
		Recognizer recognizer = new Recognizer("3rd_final_model");
		List<String> results = recognizer.recognize(filePaths);

		for (String result : results)
			System.out.println(result);
	}
}