package test.ml;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Luxand.FSDK;
import inf552.bean.ml.Data;
import inf552.utils.Constants;

public class FacialDataFilter {
	public static String input = "output/jaffe";
	public static String spaceALocationOutput = "output/jaffe_SpaceALocation";

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(input));
		PrintWriter spaceALocationPW = new PrintWriter(spaceALocationOutput);

		List<Data> spaceALocation = new ArrayList<Data>();

		String line = br.readLine();
		while (line != null) {
			for (int i = 0; i < Constants.emotions.length; i++)
				if (line.contains(Constants.emotions[i])) {
					Double label = (double) i;
					Double[] feature = new Double[FSDK.FSDK_FACIAL_FEATURE_COUNT * 2];

					line = br.readLine();
					StringTokenizer st = new StringTokenizer(line, ",");
					int count = 0;
					while (st.hasMoreTokens()) {
						feature[count++] = Double.valueOf(st.nextToken());
						feature[count++] = Double.valueOf(st.nextToken());
					}

					spaceALocation.add(new Data(feature, label));
				}

			line = br.readLine();
		}

		for (Data data : spaceALocation)
			spaceALocationPW.println(data.toString());

		spaceALocationPW.close();
		br.close();
	}
}