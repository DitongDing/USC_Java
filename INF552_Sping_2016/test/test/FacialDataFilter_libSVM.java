package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

import Luxand.FSDK;
import bean.Point;
import utils.ComUtils;

// Filter HA & NE, save location to HA&NE_Location, save feature (mouth height, average eye height, eye center distance (for standardize)) to HA&NE_Feature
// Label 0 = NE, 1 = HA
public class FacialDataFilter_libSVM {
	public static String input = "output/jaffe";
	// x, y for all location, label
	// Use NoseBridge(22)<->NoseTip(2) as y axis, LeftEye(0)<->RightEye(1) as x axis to standardize location.
	public static String spaceALocationOutput = "output/jaffe_HA&NE_SpaceALocation";
	public static String spaceBLocationOutput = "output/jaffe_HA&NE_SpaceBLocation";
	// mouth height (TopInner(61)<->BottomInner(64)), eye height(LeftUpperCenter(28)<->LeftLowerCenter(27), RightUpperCenter(32)<->RightLowerCenter(31)), eye center
	// distance(LeftCenter(0)<->RightCenter(1)), label
	public static String spaceAFeatureOutput = "output/jaffe_HA&NE_SpaceAFeature";
	public static String spaceBFeatureOutput = "output/jaffe_HA&NE_SpaceBFeature";

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(input));
		PrintWriter spaceALocationPW = new PrintWriter(spaceALocationOutput);
		PrintWriter spaceBLocationPW = new PrintWriter(spaceBLocationOutput);
		PrintWriter spaceAFeaturePW = new PrintWriter(spaceAFeatureOutput);
		PrintWriter spaceBFeaturePW = new PrintWriter(spaceBFeatureOutput);

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

				Point[] spaceBPoints = new Point[FSDK.FSDK_FACIAL_FEATURE_COUNT];
				Point originB = points[FSDK.FSDKP_NOSE_BRIDGE];
				// Move to originB
				for (int i = 0; i < FSDK.FSDK_FACIAL_FEATURE_COUNT; i++)
					spaceBPoints[i] = new Point(points[i].x - originB.x, points[i].y - originB.y);
				// Mapping
				double Xi = spaceBPoints[FSDK.FSDKP_RIGHT_EYE].x - spaceBPoints[FSDK.FSDKP_LEFT_EYE].x;
				double Xj = spaceBPoints[FSDK.FSDKP_RIGHT_EYE].y - spaceBPoints[FSDK.FSDKP_LEFT_EYE].y;
				double Yi = spaceBPoints[FSDK.FSDKP_NOSE_TIP].x - spaceBPoints[FSDK.FSDKP_NOSE_BRIDGE].x;
				double Yj = spaceBPoints[FSDK.FSDKP_NOSE_TIP].y - spaceBPoints[FSDK.FSDKP_NOSE_BRIDGE].y;
				double Ii = Xi / Math.sqrt(Xi * Xi + Xj * Xj);
				double Ij = Xj / Math.sqrt(Xi * Xi + Xj * Xj);
				double Ji = Yi / Math.sqrt(Yi * Yi + Yj * Yj);
				double Jj = Yj / Math.sqrt(Yi * Yi + Yj * Yj);
				double det = Ii * Jj - Ij * Ji;
				double iI = Jj / det;
				double iJ = -Ij / det;
				double jI = -Ji / det;
				double jJ = Ii / det;
				for (int i = 0; i < FSDK.FSDK_FACIAL_FEATURE_COUNT; i++) {
					double x = spaceBPoints[i].x, y = spaceBPoints[i].y;
					spaceBPoints[i] = new Point(x * iI + y * jI, x * iJ + y * jJ);
				}

				count = 1;
				spaceALocationPW.print(label);
				for (Point point : points) {
					spaceALocationPW.print(String.format(" %d:%.2f %d:%.2f", count, point.x, count + 1, point.y));
					count += 2;
				}
				spaceALocationPW.println();

				count = 1;
				spaceBLocationPW.print(label);
				for (Point point : spaceBPoints) {
					spaceBLocationPW.print(String.format(" %d:%.2f %d:%.2f", count, point.x, count + 1, point.y));
					count += 2;
				}
				spaceBLocationPW.println();

				double mouthHeight = ComUtils.getDistance(points[FSDK.FSDKP_MOUTH_TOP_INNER], points[FSDK.FSDKP_MOUTH_BOTTOM_INNER]);
				double leftEyeHeight = ComUtils.getDistance(points[FSDK.FSDKP_LEFT_EYE_UPPER_LINE2], points[FSDK.FSDKP_LEFT_EYE_LOWER_LINE2]);
				double rightEyeHeight = ComUtils.getDistance(points[FSDK.FSDKP_RIGHT_EYE_UPPER_LINE2], points[FSDK.FSDKP_RIGHT_EYE_LOWER_LINE2]);
				// double eyeDistance = ComUtils.getDistance(points[FSDK.FSDKP_LEFT_EYE], points[FSDK.FSDKP_RIGHT_EYE]);
				spaceAFeaturePW.println(String.format("%s 1:%.2f 2:%.2f 3:%.2f", label, mouthHeight, leftEyeHeight, rightEyeHeight));

				mouthHeight = ComUtils.getDistance(spaceBPoints[FSDK.FSDKP_MOUTH_TOP_INNER], spaceBPoints[FSDK.FSDKP_MOUTH_BOTTOM_INNER]);
				leftEyeHeight = ComUtils.getDistance(spaceBPoints[FSDK.FSDKP_LEFT_EYE_UPPER_LINE2], spaceBPoints[FSDK.FSDKP_LEFT_EYE_LOWER_LINE2]);
				rightEyeHeight = ComUtils.getDistance(spaceBPoints[FSDK.FSDKP_RIGHT_EYE_UPPER_LINE2], spaceBPoints[FSDK.FSDKP_RIGHT_EYE_LOWER_LINE2]);
				// double eyeDistance = ComUtils.getDistance(spaceBPoints[FSDK.FSDKP_LEFT_EYE], spaceBPoints[FSDK.FSDKP_RIGHT_EYE]);
				spaceBFeaturePW.println(String.format("%s 1:%.2f 2:%.2f 3:%.2f", label, mouthHeight, leftEyeHeight, rightEyeHeight));
			}

			line = br.readLine();
		}

		spaceBFeaturePW.close();
		spaceAFeaturePW.close();
		spaceBLocationPW.close();
		spaceALocationPW.close();
		br.close();
	}
}