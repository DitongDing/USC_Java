package inf552.utils.preprocessor;

import java.util.ArrayList;
import java.util.List;

import inf552.utils.ml.bean.Data;

// No Pre-Process by default
public class PreProcessor {
	public List<Data> preProcess(List<Data> original) {
		List<Data> result = new ArrayList<Data>();

		for (Data data : original) {
			Double[] feature = new Double[data.getFeature().length];
			Double label = data.getLabel();
			for (int i = 0; i < feature.length; i++)
				feature[i] = data.getFeature()[i];

			result.add(new Data(feature, label));
		}

		return result;
	}
}