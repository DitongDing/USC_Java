package inf552.utils.preprocessor;

import java.util.List;

import inf552.utils.ml.bean.Data;

// No Pre-Process by default
public class PreProcessor {
	public List<Data> preProcess(List<Data> original) {
		return original;
	}
}