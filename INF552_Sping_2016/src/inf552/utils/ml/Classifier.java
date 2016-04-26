package inf552.utils.ml;

import java.util.List;
import java.util.Set;

import inf552.utils.ml.bean.Data;

public abstract class Classifier {
	protected Set<Double> classes;

	// Only label member matters
	public abstract List<Data> predict(List<Data> dataSet);

	public abstract void train(List<Data> dataSet);

	public abstract void save(String path);

	public abstract void load(String path);
}