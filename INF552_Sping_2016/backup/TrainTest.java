package test.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import inf552.utils.Recognizer;
import inf552.utils.Trainer;
import inf552.utils.ml.Classifier;
import inf552.utils.ml.bean.Data;
import inf552.utils.ml.bean.ValidationResult;
import inf552.utils.ml.svm.TwoClassSVMClassifier;
import inf552.utils.preprocessor.PreProcessor;
import inf552.utils.preprocessor.SpaceAFeature;
import inf552.utils.preprocessor.SpaceALocation;
import inf552.utils.preprocessor.SpaceBFeature;
import inf552.utils.preprocessor.SpaceBLocation;

public class TrainTest {
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

		List<Data> originalData = trainSet;
		List<PreProcessor> preProcessers = new ArrayList<PreProcessor>(
				Arrays.asList(new PreProcessor[] { new SpaceALocation(), new SpaceBLocation(), new SpaceAFeature(), new SpaceBFeature() }));
		Integer n_fold = 10;
		Set<Double> classes = new HashSet<Double>(Arrays.asList(new Double[] { -1.0, 1.0 }));
		List<Classifier> classifiers = new ArrayList<Classifier>(Arrays.asList(
				new Classifier[] { new TwoClassSVMClassifier(1.0, null, true, classes), new TwoClassSVMClassifier(1.0, 1.0 / featureCount, true, classes),
						new TwoClassSVMClassifier(1.0, 0.0, false, classes), new TwoClassSVMClassifier(1.0, 1.0, false, classes) }));

		Trainer trainer = new Trainer(originalData, preProcessers, n_fold, classifiers);

		Recognizer recognizer = trainer.train();
		recognizer.save("model");
		recognizer = new Recognizer();
		recognizer.load("model");

		trainSet = recognizer.preProcessor.preProcess(trainSet);
		System.out.println(new ValidationResult(recognizer.classifier.predict(trainSet), trainSet));

		testSet = recognizer.preProcessor.preProcess(testSet);
		System.out.println(new ValidationResult(recognizer.classifier.predict(testSet), testSet));
	}
}