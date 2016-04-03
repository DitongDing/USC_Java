package bean;

import Luxand.FSDK;
import Luxand.FSDK.TPoint;

public class Face {
	public String fileName;
	public String filePath;
	public FSDK.TPoint[] features;

	public Face(String fileName, String filePath, TPoint[] features) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.features = features;
	}
}
