package inf552.utils.preprocessor;

import java.util.ArrayList;
import java.util.List;

import Luxand.FSDK;
import inf552.utils.ComUtils;
import inf552.utils.ml.bean.Data;

public class SpaceAFeature extends PreProcessor {
	@Override
	public List<Data> preProcess(List<Data> original) {
		List<Data> result = new ArrayList<Data>(original.size());

		for (Data data : original) {
			Double[] orgFeature = data.getFeature();

			Double[] feature = null;
			Double label = data.getLabel();

			double mouthHeight = ComUtils.getDistance(ComUtils.getXYByIndex(orgFeature, FSDK.FSDKP_MOUTH_TOP_INNER),
					ComUtils.getXYByIndex(orgFeature, FSDK.FSDKP_MOUTH_BOTTOM_INNER));
			double leftEyeHeight = ComUtils.getDistance(ComUtils.getXYByIndex(orgFeature, FSDK.FSDKP_LEFT_EYE_UPPER_LINE2),
					ComUtils.getXYByIndex(orgFeature, FSDK.FSDKP_LEFT_EYE_LOWER_LINE2));
			double rightEyeHeight = ComUtils.getDistance(ComUtils.getXYByIndex(orgFeature, FSDK.FSDKP_RIGHT_EYE_UPPER_LINE2),
					ComUtils.getXYByIndex(orgFeature, FSDK.FSDKP_RIGHT_EYE_LOWER_LINE2));
			feature = new Double[] { mouthHeight, leftEyeHeight, rightEyeHeight };

			result.add(new Data(feature, label));
		}

		return result;
	}
}
