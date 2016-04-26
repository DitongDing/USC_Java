package inf552.utils.ml.nn;

import java.util.List;

import inf552.utils.ml.Classifier;
import inf552.utils.ml.bean.Data;

public class NNClassifier extends Classifier {

	@Override
	public List<Data> predict(List<Data> dataSet) {
		return null;
	}

	@Override
	public void train(List<Data> dataSet) {
	}

	@Override
	public void save(String path) {
	}

	@Override
	public void load(String path) {
	}

	@Override
	public Classifier cloneBeforeTraining() {
		return null;
	}
}