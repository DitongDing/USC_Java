package inf552.utils.preprocessor;

import java.util.List;

import inf552.bean.ml.Data;

public class SpaceBFeature implements PreProcessor {
	@Override
	public List<Data> preProcess(List<Data> original) {
		return new SpaceAFeature().preProcess(new SpaceBLocation().preProcess(original));
	}
}