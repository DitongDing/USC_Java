package inf552.utils.preprocessor;

import java.util.List;

import inf552.bean.ml.Data;

public class SpaceBFeature2 implements PreProcessor {
	@Override
	public List<Data> preProcess(List<Data> original) {
		return new SpaceAFeature2().preProcess(new SpaceBLocation().preProcess(original));
	}
}