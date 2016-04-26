package inf552.utils.ml;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import inf552.utils.ml.bean.Data;
import inf552.utils.ml.bean.ValidationResult;

public class CrossValidation {
	private Integer n_fold;
	private List<Classifier> classifiers;
	private List<Data> dataSet;

	public CrossValidation(Integer n_fold, List<Classifier> classifiers, List<Data> dataSet) {
		this.n_fold = n_fold;
		this.classifiers = classifiers;
		this.dataSet = dataSet;
	}

	public Classifier getBestClassifier() {
		Classifier bestClassifier = null;
		Classifier classifier = null;
		ValidationResult bestValidationResult = null;
		ValidationResult validationResult = null;

		ListIterator<Classifier> classifierLI = classifiers.listIterator();

		while (classifierLI.hasNext()) {
			classifier = classifierLI.next();
			validationResult = singleClassifierCrossValidation(classifier);
			// The greater the better.
			if (bestValidationResult == null || validationResult.compareTo(bestValidationResult) > 0) {
				bestClassifier = classifier;
				bestValidationResult = validationResult;
			}
			
			System.out.println(String.format("==========CrossValidation: ==========Current Best Classifier: %s, Best Validation Result: %s=========", bestClassifier.toString(), bestValidationResult.toString()));
		}

		return bestClassifier;
	}

	private ValidationResult singleClassifierCrossValidation(Classifier classifier) {
		ValidationResult result = null;

		if (classifier != null) {
			Random random = new Random();
			List<ValidationResult> validationResults = new ArrayList<ValidationResult>();

			for (Integer i = 0; i < n_fold; i++) {
				// TODO: <3 LOW> Consider test set class distribution in CV process, as well as actual data set size.
				// Generate data set.
				List<Data> trainSet = new ArrayList<Data>();
				List<Data> validationSet = new ArrayList<Data>();

				Double threshold = 1.0 / n_fold;
				ListIterator<Data> dataSetLI = dataSet.listIterator();
				while (dataSetLI.hasNext()) {
					if (random.nextDouble() <= threshold)
						validationSet.add(dataSetLI.next());
					else
						trainSet.add(dataSetLI.next());
				}

				// Train on train set.
				classifier.train(trainSet);

				// Test on validation set
				List<Data> prediction = classifier.predict(validationSet);
				ValidationResult validationResult = new ValidationResult(prediction, validationSet);

				// Add to result set
				validationResults.add(validationResult);
			}

			result = ValidationResult.getAverageValidationResult(validationResults);
		}

		return result;
	}
}
