package inf552.bean;

import Luxand.FSDK;
import Luxand.FSDK.TPoint;
import inf552.bean.ml.Data;

public class Face {
	public String fileName;
	public String filePath;
	public FSDK.TPoint[] features;

	public Face(String fileName, String filePath, TPoint[] features) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.features = features;
	}

	public Data toData(Double label) {
		Double[] feature = new Double[FSDK.FSDK_FACIAL_FEATURE_COUNT * 2];
		int count = 0;
		for (FSDK.TPoint point : features) {
			feature[count++] = (double) point.x;
			feature[count++] = (double) point.y;
		}
		return new Data(feature, label);
	}
}
