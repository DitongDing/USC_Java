package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Luxand.FSDK;
import inf552.utils.ml.bean.Data;
import inf552.utils.preprocessor.SpaceAFeature;
import inf552.utils.preprocessor.SpaceBFeature;
import inf552.utils.preprocessor.SpaceBLocation;

// Filter HA & NE, save location to HA&NE_Location, save feature (mouth height, average eye height, eye center distance (for standardize)) to HA&NE_Feature
// Label 0 = NE, 1 = HA
public class FacialDataFilter_libSVM {
	public static String input = "output/jaffe";
	// x, y for all location, label
	// Use NoseBridge(22)<->NoseTip(2) as y axis, LeftEye(0)<->RightEye(1) as x axis to standardize location.
	public static String spaceALocationOutput = "output/jaffe_HA&NE&AN_SpaceALocation";
	public static String spaceBLocationOutput = "output/jaffe_HA&NE&AN_SpaceBLocation";
	// mouth height (TopInner(61)<->BottomInner(64)), eye height(LeftUpperCenter(28)<->LeftLowerCenter(27), RightUpperCenter(32)<->RightLowerCenter(31)), eye center
	// distance(LeftCenter(0)<->RightCenter(1)), label
	public static String spaceAFeatureOutput = "output/jaffe_HA&NE&AN_SpaceAFeature";
	public static String spaceBFeatureOutput = "output/jaffe_HA&NE&AN_SpaceBFeature";

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(input));
		PrintWriter spaceALocationPW = new PrintWriter(spaceALocationOutput);
		PrintWriter spaceBLocationPW = new PrintWriter(spaceBLocationOutput);
		PrintWriter spaceAFeaturePW = new PrintWriter(spaceAFeatureOutput);
		PrintWriter spaceBFeaturePW = new PrintWriter(spaceBFeatureOutput);

		List<Data> spaceALocation = new ArrayList<Data>();
		List<Data> spaceAFeature = new ArrayList<Data>();
		List<Data> spaceBLocation = new ArrayList<Data>();
		List<Data> spaceBFeature = new ArrayList<Data>();

		String line = br.readLine();
		while (line != null) {
			if (line.contains(".NE") || line.contains(".HA") || line.contains(".AN")) {
				Double label = line.contains(".NE") ? 0.0 : (line.contains(".HA") ? 1.0 : 2.0);
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

		spaceAFeature = new SpaceAFeature().preProcess(spaceALocation);
		spaceBLocation = new SpaceBLocation().preProcess(spaceALocation);
		spaceBFeature = new SpaceBFeature().preProcess(spaceALocation);

		for (Data data : spaceALocation)
			spaceALocationPW.println(data.toString());
		for (Data data : spaceAFeature)
			spaceAFeaturePW.println(data.toString());
		for (Data data : spaceBLocation)
			spaceBLocationPW.println(data.toString());
		for (Data data : spaceBFeature)
			spaceBFeaturePW.println(data.toString());

		spaceBFeaturePW.close();
		spaceAFeaturePW.close();
		spaceBLocationPW.close();
		spaceALocationPW.close();
		br.close();
	}
}