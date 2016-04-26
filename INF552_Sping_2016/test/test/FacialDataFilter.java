package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import Luxand.FSDK;
import bean.Point;
import utils.ComUtils;

@Deprecated
// Filter HA & NE, save location to HA&NE_Location, save feature (mouth height, average eye height, eye center distance (for standardize)) to HA&NE_Feature
// Label 0 = NE, 1 = HA
public class FacialDataFilter {
	public static String input = "output/jaffe";
	// x, y for all location, label
	// Use NoseBridge(22)<->NoseTip(2) as y axis, LeftEye(0)<->RightEye(1) as x axis to standardize location.
	public static String locationOutput = "output/jaffe_HA&NE_Location";
	// mouth height (TopInner(61)<->BottomInner(64)), eye height(LeftUpperCenter(28)<->LeftLowerCenter(27), RightUpperCenter(32)<->RightLowerCenter(31)), eye center
	// distance(LeftCenter(0)<->RightCenter(1)), label
	public static String featureOutput = "output/jaffe_HA&NE_Feature";

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(input));
		PrintWriter locationPW = new PrintWriter(locationOutput);
		PrintWriter featurePW = new PrintWriter(featureOutput);

		String line = br.readLine();
		while (line != null) {
			if (line.contains(".NE") || line.contains(".HA")) {
				int label = line.contains(".NE") ? 0 : 1;
				line = br.readLine();

				locationPW.print(line);
				locationPW.println(label);

				StringTokenizer st = new StringTokenizer(line, ",");
				Point[] points = new Point[FSDK.FSDK_FACIAL_FEATURE_COUNT];
				int count = 0;
				while (st.hasMoreTokens()) {
					int X = Integer.valueOf(st.nextToken());
					int Y = Integer.valueOf(st.nextToken());
					points[count++] = new Point(X, Y);
				}

				double mouthHeight = ComUtils.getDistance(points[FSDK.FSDKP_MOUTH_TOP_INNER], points[FSDK.FSDKP_MOUTH_BOTTOM_INNER]);
				double leftEyeHeight = ComUtils.getDistance(points[FSDK.FSDKP_LEFT_EYE_UPPER_LINE2], points[FSDK.FSDKP_LEFT_EYE_LOWER_LINE2]);
				double rightEyeHeight = ComUtils.getDistance(points[FSDK.FSDKP_RIGHT_EYE_UPPER_LINE2], points[FSDK.FSDKP_RIGHT_EYE_LOWER_LINE2]);
				double eyeDistance = ComUtils.getDistance(points[FSDK.FSDKP_LEFT_EYE], points[FSDK.FSDKP_RIGHT_EYE]);
				featurePW.println(String.format("%.2f,%.2f,%.2f,%.2f,%d", mouthHeight, leftEyeHeight, rightEyeHeight, eyeDistance, label));
			}

			line = br.readLine();
		}

		featurePW.close();
		locationPW.close();
		br.close();
	}
}