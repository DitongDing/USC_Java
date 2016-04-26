package inf552.utils;

import java.io.File;

public class ComUtils {
	public static String getFilePath(String dirPath, String fileName) {
		return String.format("%s%s%s", dirPath, File.separator, fileName);
	}

	public static Double getXByIndex(Double[] data, Integer index) {
		return data[index * 2];
	}

	public static Double getYByIndex(Double[] data, Integer index) {
		return data[index * 2 + 1];
	}

	public static Double[] getXYByIndex(Double[] data, Integer index) {
		return new Double[] { data[index * 2], data[index * 2 + 1] };
	}

	public static Double getDistance(Double[] data0, Double[] data1, Integer lp) {
		Double result = 0.0;
		if (data0 != null && data1 != null && data0.length == data1.length) {
			for (int i = 0; i < data0.length; i++)
				result += Math.pow((data0[i] - data1[i]), lp);
			result = Math.pow(result, 1.0 / lp);
		}
		return result;
	}
}
