package test.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import inf552.bean.ml.Data;
import inf552.bean.ml.ValidationResult;
import inf552.utils.ml.CrossValidation;
import inf552.utils.ml.Model;
import inf552.utils.ml.bean.knn.KNNClassifier;
import inf552.utils.ml.svm.TwoClassSVMClassifier;
import inf552.utils.preprocessor.SpaceALocation;

public class CVTest {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_HA&NE_SpaceALocation";

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

		Set<Double> classes = new HashSet<Double>(Arrays.asList(new Double[] { -1.0, 1.0 }));
		List<Model> models = new ArrayList<Model>();
		models.add(new Model(new SpaceALocation(), false, new TwoClassSVMClassifier(1.0, null, classes)));
		models.add(new Model(new SpaceALocation(), true, new TwoClassSVMClassifier(1.0, null, classes)));
		models.add(new Model(new SpaceALocation(), false, new TwoClassSVMClassifier(1.0, 0.0, classes)));
		models.add(new Model(new SpaceALocation(), false, new TwoClassSVMClassifier(1.0, 1.0, classes)));
		models.add(new Model(new SpaceALocation(), false, new KNNClassifier(1, 2, classes)));

		CrossValidation CV = new CrossValidation(10, models, trainSet);

		Model model = CV.getBestModel();

		model.train(trainSet);

		System.out.println(new ValidationResult(model.predict(trainSet), trainSet));

		System.out.println(new ValidationResult(model.predict(testSet), testSet));
	}
}
