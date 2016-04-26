package test.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import inf552.bean.ml.Data;
import inf552.bean.ml.ValidationResult;
import inf552.utils.ml.Classifier;
import inf552.utils.ml.svm.MultiClassSVMClassifier;

public class MultiClassSVMTest {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_HA&NE&AN_SpaceALocation";

		List<Data> trainSet = new ArrayList<Data>();
		// Top 10
		List<Data> testSet = new ArrayList<Data>();

		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = br.readLine();
		while (line != null) {
			if (testSet.size() < 10)
				testSet.add(Data.valueOf(line));
			else
				trainSet.add(Data.valueOf(line));

			line = br.readLine();
		}
		br.close();

		Map<String, Double[]> CGammaMap = new HashMap<String, Double[]>();
		CGammaMap.put("0_1", new Double[] { 1.0, null });
		CGammaMap.put("0_2", new Double[] { 1.0, null });
		CGammaMap.put("1_2", new Double[] { 1.0, null });

		Classifier classifier = new MultiClassSVMClassifier(CGammaMap, new HashSet<Double>(Arrays.asList(new Double[] { 0.0, 1.0, 2.0 })));

		classifier.train(trainSet);
		classifier.save("model");
		classifier = new MultiClassSVMClassifier();
		classifier.load("model");

		System.out.println(new ValidationResult(classifier.predict(trainSet), trainSet));

		System.out.println(new ValidationResult(classifier.predict(testSet), testSet));
	}
}
