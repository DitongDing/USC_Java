package test;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import Luxand.FSDK;
import utils.ComUtils;

// Filter HA & NE, save location to HA&NE_Location, save feature (mouth height, average eye height, eye center distance (for standardize)) to HA&NE_Feature
// Label 0 = NE, 1 = HA
public class FacialDataFilter_libSVM {
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
				String label = line.contains(".NE") ? "-1" : "+1";
				line = br.readLine();

				StringTokenizer st = new StringTokenizer(line, ",");
				Point[] points = new Point[FSDK.FSDK_FACIAL_FEATURE_COUNT];
				int count = 0;
				while (st.hasMoreTokens()) {
					int X = Integer.valueOf(st.nextToken());
					int Y = Integer.valueOf(st.nextToken());
					points[count++] = new Point(X, Y);
				}

				count = 1;
				locationPW.print(label);
				for (Point point : points) {
					locationPW.print(String.format(" %d:%d %d:%d", count, point.x, count + 1, point.y));
					count += 2;
				}
				locationPW.println();

				double mouthHeight = ComUtils.getDistance(points[FSDK.FSDKP_MOUTH_TOP_INNER], points[FSDK.FSDKP_MOUTH_BOTTOM_INNER]);
				double leftEyeHeight = ComUtils.getDistance(points[FSDK.FSDKP_LEFT_EYE_UPPER_LINE2], points[FSDK.FSDKP_LEFT_EYE_LOWER_LINE2]);
				double rightEyeHeight = ComUtils.getDistance(points[FSDK.FSDKP_RIGHT_EYE_UPPER_LINE2], points[FSDK.FSDKP_RIGHT_EYE_LOWER_LINE2]);
				double eyeDistance = ComUtils.getDistance(points[FSDK.FSDKP_LEFT_EYE], points[FSDK.FSDKP_RIGHT_EYE]);
				featurePW.println(String.format("%s 1:%.2f 2:%.2f 3:%.2f 4:%.2f", label, mouthHeight, leftEyeHeight, rightEyeHeight, eyeDistance));
			}

			line = br.readLine();
		}

		featurePW.close();
		locationPW.close();
		br.close();
	}
}