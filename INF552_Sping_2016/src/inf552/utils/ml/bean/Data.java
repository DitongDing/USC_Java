package inf552.utils.ml.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Data {
	private Double[] feature;
	private Double label;

	public Data(Double[] feature, Double label) {
		this.feature = feature;
		this.label = label;
	}

	public Double[] getFeature() {
		return feature;
	}

	public Double getLabel() {
		return label;
	}

	public void setLabel(Double label) {
		this.label = label;
	}

	@Override
	public Object clone() {
		Double[] feature = Arrays.copyOf(this.feature, this.feature.length);
		Double label = new Double(this.label);
		Object result = new Data(feature, label);
		return result;
	}

	public static List<Data> clone(List<Data> dataSet) {
		List<Data> result = new ArrayList<Data>();

		for (Data data : dataSet)
			result.add((Data) data.clone());

		return result;
	}
}
