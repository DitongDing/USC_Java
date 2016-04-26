package inf552.utils.preprocessor;

import java.util.ArrayList;
import java.util.List;

import Luxand.FSDK;
import inf552.utils.ComUtils;
import inf552.utils.ml.bean.Data;

public class SpaceBLocation extends PreProcessor {
	@Override
	public List<Data> preProcess(List<Data> original) {
		List<Data> result = new ArrayList<Data>(original.size());

		for (Data data : original) {
			Double[] orgFeature = data.getFeature();
			Double[] feature = new Double[orgFeature.length];
			Double label = data.getLabel();

			// Move to originB
			for (int i = 0, x = 0, y = 1; i < FSDK.FSDK_FACIAL_FEATURE_COUNT; i++, x += 2, y += 2){
				feature[x] = orgFeature[x] - ComUtils.getXByIndex(orgFeature, FSDK.FSDKP_NOSE_BRIDGE);
				feature[y] = orgFeature[y] - ComUtils.getYByIndex(orgFeature, FSDK.FSDKP_NOSE_BRIDGE);
			}
			// Mapping
			double Xi = ComUtils.getXByIndex(feature, FSDK.FSDKP_RIGHT_EYE) - ComUtils.getXByIndex(feature, FSDK.FSDKP_LEFT_EYE);
			double Xj = ComUtils.getYByIndex(feature, FSDK.FSDKP_RIGHT_EYE) - ComUtils.getYByIndex(feature, FSDK.FSDKP_LEFT_EYE);
			double Yi = ComUtils.getXByIndex(feature, FSDK.FSDKP_NOSE_TIP) - ComUtils.getXByIndex(feature, FSDK.FSDKP_NOSE_BRIDGE);
			double Yj = ComUtils.getYByIndex(feature, FSDK.FSDKP_NOSE_TIP) - ComUtils.getYByIndex(feature, FSDK.FSDKP_NOSE_BRIDGE);
			double Ii = Xi / Math.sqrt(Xi * Xi + Xj * Xj);
			double Ij = Xj / Math.sqrt(Xi * Xi + Xj * Xj);
			double Ji = Yi / Math.sqrt(Yi * Yi + Yj * Yj);
			double Jj = Yj / Math.sqrt(Yi * Yi + Yj * Yj);
			double det = Ii * Jj - Ij * Ji;
			double iI = Jj / det;
			double iJ = -Ij / det;
			double jI = -Ji / det;
			double jJ = Ii / det;

			for (int i = 0, x = 0, y = 1; i < FSDK.FSDK_FACIAL_FEATURE_COUNT; i++, x += 2, y += 2) {
				feature[x] = feature[x] * iI + feature[y] * jI;
				feature[y] = feature[x] * iJ + feature[y] * jJ;
			}

			result.add(new Data(feature, label));
		}

		return result;
	}
}
