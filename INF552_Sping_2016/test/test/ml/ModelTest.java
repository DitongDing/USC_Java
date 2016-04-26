package test.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

import inf552.bean.ml.Data;
import inf552.bean.ml.ValidationResult;
import inf552.utils.ml.Model;
import inf552.utils.ml.bean.knn.KNNClassifier;
import inf552.utils.ml.svm.TwoClassSVMClassifier;
import inf552.utils.preprocessor.SpaceAFeature;
import inf552.utils.preprocessor.SpaceALocation;
import inf552.utils.preprocessor.SpaceBFeature;
import inf552.utils.preprocessor.SpaceBLocation;

public class ModelTest {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_HA&NE&AN_SpaceALocation";
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

		Model model = new Model(new SpaceBLocation(), false, new KNNClassifier(1, 2, new HashSet<Double>(Arrays.asList(new Double[] { 0.0, 1.0, 2.0 }))));

		model.train(trainSet);
		model.save("model");
		model = new Model();
		model.load("model");

		System.out.println(new ValidationResult(model.predict(trainSet), trainSet));

		System.out.println(new ValidationResult(model.predict(testSet), testSet));
	}
}
