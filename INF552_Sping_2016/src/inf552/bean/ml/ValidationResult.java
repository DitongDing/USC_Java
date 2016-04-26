package inf552.bean.ml;

import java.util.List;
import java.util.ListIterator;

public class ValidationResult implements Comparable<ValidationResult> {
	private Double overallAccuracy;

	public ValidationResult(Double overallAccuracy) {
		this.overallAccuracy = overallAccuracy;
	}

	public ValidationResult(List<Data> prediction, List<Data> target) {
		overallAccuracy = -1.0;
		if (prediction != null && target != null && prediction.size() == target.size() && prediction.size() != 0) {
			ListIterator<Data> predictionLI = prediction.listIterator();
			ListIterator<Data> targetLI = target.listIterator();
			Integer count = prediction.size();

			overallAccuracy = 0.0;
			while (predictionLI.hasNext()) {
				overallAccuracy += predictionLI.next().getLabel().equals(targetLI.next().getLabel()) ? 1 : 0;
			}

			overallAccuracy /= count;
		}
	}

	public Double getOverallAccuracy() {
		return overallAccuracy;
	}

	public static ValidationResult getAverageValidationResult(List<ValidationResult> validationResults) {
		ValidationResult result = null;

		if (validationResults != null && validationResults.size() != 0) {
			Integer count = validationResults.size();
			Double overallAccuracy = 0.0;
			for (ValidationResult trainResult : validationResults) {
				overallAccuracy += trainResult.getOverallAccuracy();
			}
			result = new ValidationResult(overallAccuracy / count);
		}

		return result;
	}

	// The larger the better
	@Override
	public int compareTo(ValidationResult arg0) {
		return overallAccuracy.compareTo(arg0.overallAccuracy);
	}

	@Override
	public String toString() {
		return String.format("overallAccuracy=%f", overallAccuracy);
	}
}
