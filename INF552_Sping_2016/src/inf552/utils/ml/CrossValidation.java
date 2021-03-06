package inf552.utils.ml;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import inf552.bean.ml.Data;
import inf552.bean.ml.ValidationResult;

// All models will be trained only once.
public class CrossValidation {
	private Integer n_fold;
	private List<Model> models;
	private List<Data> dataSet;

	public List<String> logs;

	public CrossValidation(Integer n_fold, List<Model> models, List<Data> dataSet) {
		this.n_fold = n_fold;
		this.models = models;
		this.dataSet = dataSet;
		logs = new ArrayList<String>();
	}

	public Model getBestModel() {
		Model bestModel = null;
		ValidationResult bestValidationResult = null;

		for (Model model : models) {
			ValidationResult validationResult = singleModelCrossValidation(model);
			logs.add(String.format("Model: %s, Validation Result: %s", model.toString(), validationResult.toString()));

			if (bestValidationResult == null || validationResult.compareTo(bestValidationResult) > 0) {
				bestModel = model;
				bestValidationResult = validationResult;
			}

			System.out.println(String.format("==========CrossValidation: ==========Current Best Model: %s, Best Validation Result: %s=========",
					bestModel.toString(), bestValidationResult.toString()));
		}

		logs.add(String.format("==========Best Model: %s, Best Validation Result: %s=========", bestModel.toString(), bestValidationResult.toString()));

		return bestModel;
	}

	public ValidationResult singleModelCrossValidation(Model model) {
		ValidationResult result = null;

		if (model != null) {
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
				model.train(trainSet);

				// Test on validation set
				ValidationResult validationResult = new ValidationResult(model.predict(validationSet), validationSet);

				// Add to result set
				validationResults.add(validationResult);
			}

			result = ValidationResult.getAverageValidationResult(validationResults);
		}

		return result;
	}
}
