package inf552.utils.preprocessor;

import java.util.List;

import inf552.bean.ml.Data;

public class SpaceALocation implements PreProcessor {

	@Override
	public List<Data> preProcess(List<Data> original) {
		return Data.clone(original);
	}
}
