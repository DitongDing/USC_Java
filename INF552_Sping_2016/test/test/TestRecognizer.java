package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import inf552.bean.ml.Data;
import inf552.utils.Recognizer;

public class TestRecognizer {

	public static void main(String[] args) {
		List<String> filePaths = new ArrayList<String>(Arrays.asList(new String[] { "input/jaffe/KA.HA1.29.jpg", "input/jaffe/KA.AN1.39.jpg" }));
		Recognizer recognizer = new Recognizer("final_model");
		List<Data> result = recognizer.recognize(filePaths);

		for (Data data : result)
			System.out.println(data.getLabel());
	}
}