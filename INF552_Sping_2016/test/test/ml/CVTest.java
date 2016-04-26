package test.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import inf552.utils.ml.Classifier;
import inf552.utils.ml.CrossValidation;
import inf552.utils.ml.bean.Data;
import inf552.utils.ml.bean.ValidationResult;
import inf552.utils.ml.svm.TwoClassSVMClassifier;

public class CVTest {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_HA&NE_SpaceALocation";
		Integer featureCount = 0;

		List<Data> trainSet = new ArrayList<Data>();
		// Top 10
		List<Data> testSet = new ArrayList<Data>();

		BufferedReader br = new BufferedReader(new FileReader(input));

		String line = br.readLine();
		while (line != null) {
			StringTokenizer st = new StringTokenizer(line, " :");
			Double label = Double.valueOf(st.nextToken());

			featureCount = st.countTokens() / 2;
			Double[] feature = new Double[featureCount];
			for (int i = 0; i < featureCount; i++) {
				st.nextToken();
				feature[i] = Double.valueOf(st.nextToken());
			}

			if (testSet.size() < 10)
				testSet.add(new Data(feature, label));
			else
				trainSet.add(new Data(feature, label));

			line = br.readLine();
		}

		br.close();

		Set<Double> classes = new HashSet<Double>(Arrays.asList(new Double[] { -1.0, 1.0 }));
		List<Classifier> classifiers = new ArrayList<Classifier>();
		classifiers.add(new TwoClassSVMClassifier(1.0, 1.0 / featureCount, false, classes));
		classifiers.add(new TwoClassSVMClassifier(1.0, 1.0 / featureCount, true, classes));
		classifiers.add(new TwoClassSVMClassifier(1.0, 0.0, false, classes));
		classifiers.add(new TwoClassSVMClassifier(1.0, 1.0, false, classes));

		CrossValidation CV = new CrossValidation(10, classifiers, trainSet);

		Classifier SVM = CV.getBestClassifier();

		SVM.train(trainSet);

		List<Data> prediction = SVM.predict(trainSet);
		System.out.println(new ValidationResult(prediction, trainSet).getOverallAccuracy());

		prediction = SVM.predict(testSet);
		System.out.println(new ValidationResult(prediction, testSet).getOverallAccuracy());
	}
}
