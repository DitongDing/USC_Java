package inf552.utils.ml;

import java.util.List;

import inf552.utils.ml.bean.Data;
import inf552.utils.ml.bean.ScaleModel;
import inf552.utils.preprocessor.PreProcessor;

public abstract class Classifier {
	protected ScaleModel scaleModel;
	protected PreProcessor preProcessor;

	// Only label member matters
	public abstract List<Data> predict(List<Data> dataSet);

	public abstract void train(List<Data> dataSet);

	public abstract void save(String path);

	public abstract void load(String path);
}