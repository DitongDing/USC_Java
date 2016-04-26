package inf552.utils;

import java.util.List;

import inf552.utils.ml.Classifier;
import inf552.utils.ml.CrossValidation;
import inf552.utils.ml.bean.Data;
import inf552.utils.ml.bean.ValidationResult;
import inf552.utils.preprocessor.PreProcessor;

// Data->Recognizer
public class Trainer {
	private List<Data> originalData;
	private List<PreProcessor> preProcessors;
	private Integer n_fold;
	private List<Classifier> classifiers;

	public Trainer(List<Data> originalData, List<PreProcessor> preProcessers, Integer n_fold, List<Classifier> classifiers) {
		this.originalData = originalData;
		this.preProcessors = preProcessers;
		this.n_fold = n_fold;
		this.classifiers = classifiers;
	}

	public Recognizer train() {
		Recognizer result = null;
		ValidationResult bestValidationResult = null;

		for (PreProcessor preProcessor : preProcessors) {
			// PreProcess
			List<Data> processedData = preProcessor.preProcess(originalData);
			// CrossValidation
			CrossValidation crossValidation = new CrossValidation(n_fold, classifiers, processedData);
			Classifier bestClassifier = crossValidation.getBestClassifier();
			// Train with bestClassifier
			bestClassifier.train(processedData);
			// Get validation result
			ValidationResult validationResult = new ValidationResult(bestClassifier.predict(processedData), processedData);
			// Update result
			if (bestValidationResult == null || bestValidationResult.compareTo(validationResult) < 0) {
				bestValidationResult = validationResult;
				result = new Recognizer(preProcessor, bestClassifier);
			}
			
			System.out.println(String.format("==========Trainer: ==========Current PreProcessor: %s, Current Best Classifier: %s, Best Validation Result: %s=========", preProcessor.getClass().getName(), bestClassifier.toString(), bestValidationResult.toString()));
		}

		return result;
	}
}
