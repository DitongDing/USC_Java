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
import inf552.utils.ml.svm.TwoClassSVMClassifier;

public class TwoClassSVMTest {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_HA&NE_SpaceBLocation";

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

		Classifier SVM = new TwoClassSVMClassifier(1.0, null, new HashSet<Double>(Arrays.asList(new Double[] { -1.0, 1.0 })));

		SVM.train(trainSet);
		SVM.save("model");
		SVM = new TwoClassSVMClassifier();
		SVM.load("model");

		System.out.println(new ValidationResult(SVM.predict(trainSet), trainSet));

		System.out.println(new ValidationResult(SVM.predict(testSet), testSet));
	}
}