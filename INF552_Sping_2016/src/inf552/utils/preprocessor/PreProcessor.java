package inf552.utils.preprocessor;

import java.util.List;

import inf552.bean.ml.Data;

// No Pre-Process by default
public interface PreProcessor {
	public List<Data> preProcess(List<Data> original);
}