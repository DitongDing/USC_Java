package test.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import inf552.bean.ml.Data;
import inf552.bean.ml.ValidationResult;
import inf552.utils.ml.Classifier;
import inf552.utils.ml.bean.knn.KNNClassifier;

public class KNNTest {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_SpaceALocation";

		List<Data> trainSet = new ArrayList<Data>();
		// Top 10
		List<Data> testSet = new ArrayList<Data>();

		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = br.readLine();
		while (line != null) {
			if (testSet.size() < 50)
				testSet.add(Data.valueOf(line));
			else
				trainSet.add(Data.valueOf(line));

			line = br.readLine();
		}
		br.close();

		Classifier classifier = new KNNClassifier(1, 2, new HashSet<Double>(Arrays.asList(new Double[] { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0 })));

		classifier.train(trainSet);
		classifier.save("model");
		classifier = new KNNClassifier();
		classifier.load("model");

		System.out.println(new ValidationResult(classifier.predict(trainSet), trainSet));

		System.out.println(new ValidationResult(classifier.predict(testSet), testSet));
	}
}
