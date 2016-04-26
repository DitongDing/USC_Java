package inf552.utils.ml;

import java.util.List;

import inf552.utils.ml.bean.Data;
import inf552.utils.ml.bean.ScaleModel;

public abstract class Classifier {
	protected ScaleModel scaleModel;

	// Only label member matters
	public abstract List<Data> predict(List<Data> dataSet);

	public abstract void train(List<Data> dataSet);

	public abstract void save(String path);

	public abstract void load(String path);

	public abstract Classifier cloneBeforeTraining();
}