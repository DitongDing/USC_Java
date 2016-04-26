package train;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import inf552.bean.ml.Data;
import inf552.bean.ml.ValidationResult;
import inf552.utils.ml.CrossValidation;
import inf552.utils.ml.Model;
import inf552.utils.ml.bean.knn.KNNClassifier;
import inf552.utils.ml.svm.MultiClassSVMClassifier;
import inf552.utils.preprocessor.PreProcessor;
import inf552.utils.preprocessor.SpaceAFeature;
import inf552.utils.preprocessor.SpaceAFeature2;
import inf552.utils.preprocessor.SpaceALocation;
import inf552.utils.preprocessor.SpaceBFeature2;

public class Train2 {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_SpaceALocation_generated";
		int n_fold = 10;
		Random r = new Random();

		List<Data> trainSet = new ArrayList<Data>();
		List<Data> testSet = new ArrayList<Data>();

		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = br.readLine();
		while (line != null) {
			if (r.nextDouble() <= 1.0 / n_fold)
				testSet.add(Data.valueOf(line));
			else
				trainSet.add(Data.valueOf(line));

			line = br.readLine();
		}
		br.close();

		Double[] classesArray = { 0.0, 1.0, 2.0, 3.0, 4.0, 5.0 };
		Set<Double> classes = new HashSet<Double>(Arrays.asList(classesArray));
		List<Model> models = new ArrayList<Model>();
		PreProcessor[] preProcessors = new PreProcessor[] { new SpaceALocation(), new SpaceAFeature(), new SpaceAFeature2(), new SpaceBFeature2() };
		boolean[] ifScales = { true };
		Integer[] Ks = { 1, 3 };
		Integer[] lps = { 2 };
		Double[] CRange = { -2.0, 5.0 };
		// Lower, Upper, Default
		Double[] gammaRange = { -5.0, 5.0, null };
		Double defaultPossibility = 0.8;
		Integer SVMCount = 3;

		for (PreProcessor preProcessor : preProcessors)
			for (boolean ifScale : ifScales) {
				// KNN
				for (Integer K : Ks)
					for (Integer lp : lps)
						models.add(new Model(preProcessor, ifScale, new KNNClassifier(K, lp, classes)));
				// MultiClassSVM
				for (int svm = 0; svm < SVMCount; svm++) {
					Map<String, Double[]> CGammaMap = new HashMap<String, Double[]>();
					for (int i = 0; i < classesArray.length; i++)
						for (int j = i + 1; j < classesArray.length; j++) {
							String classifierName = MultiClassSVMClassifier.getClassifierName(classesArray[i], classesArray[j]);
							Double C = r.nextDouble() * (CRange[1] - CRange[0]) + CRange[0];
							Double gamma = r.nextDouble() < defaultPossibility ? null : r.nextDouble() * (gammaRange[1] - gammaRange[0]) + gammaRange[0];
							CGammaMap.put(classifierName, new Double[] { C, gamma });
						}
					models.add(new Model(preProcessor, ifScale, new MultiClassSVMClassifier(CGammaMap, classes)));
				}
			}

		CrossValidation CV = new CrossValidation(n_fold, models, trainSet);
		Model model = CV.getBestModel();

		PrintWriter pw = new PrintWriter("CV.log");
		for (String log : CV.logs)
			pw.println(log);
		pw.close();

		model.train(trainSet);
		System.out.println(new ValidationResult(model.predict(trainSet), trainSet));
		System.out.println(new ValidationResult(model.predict(testSet), testSet));
		model.save("4rd_final_model");
	}
}