package inf552.utils.ml.bean;

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
}
