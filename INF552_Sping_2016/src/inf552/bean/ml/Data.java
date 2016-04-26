package inf552.bean.ml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

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
		Double label = this.label;
		Object result = new Data(feature, label);
		return result;
	}

	public static List<Data> clone(List<Data> dataSet) {
		List<Data> result = new ArrayList<Data>();

		for (Data data : dataSet)
			result.add((Data) data.clone());

		return result;
	}

	@Override
	public String toString() {
		String result = String.format("%.0f", label);
		int index = 1;
		for (Double value : feature)
			result += String.format(" %d:%f", index++, value);
		return result;
	}

	public static Data valueOf(String s) {
		StringTokenizer st = new StringTokenizer(s, " :");
		Double label = Double.valueOf(st.nextToken());
		List<Double> feature = new ArrayList<Double>();
		while (st.hasMoreTokens()) {
			st.nextToken();
			feature.add(Double.valueOf(st.nextToken()));
		}
		return new Data(feature.toArray(new Double[0]), label);
	}
}
