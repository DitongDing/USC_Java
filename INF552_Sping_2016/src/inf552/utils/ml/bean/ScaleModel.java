package inf552.utils.ml.bean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ScaleModel {
	public Double[] feature_max;
	public Double[] feature_min;
	public Double upper;
	public Double lower;

	public ScaleModel(List<Double> maxs, List<Double> mins, Double upper, Double lower) {
		this.feature_max = maxs.toArray(new Double[0]);
		this.feature_min = mins.toArray(new Double[0]);
		this.upper = upper;
		this.lower = lower;
	}

	public ScaleModel(List<Data> dataSet, Double upper, Double lower) {
		this.upper = upper;
		this.lower = lower;

		Integer featureCount = dataSet.get(0).getFeature().length;
		feature_max = new Double[featureCount];
		feature_min = new Double[featureCount];
		for (int i = 0; i < featureCount; i++) {
			feature_max[i] = Double.NEGATIVE_INFINITY;
			feature_min[i] = Double.POSITIVE_INFINITY;
		}

		for (Data data : dataSet) {
			Double[] feature = data.getFeature();
			for (int i = 0; i < featureCount; i++) {
				if (feature[i] < feature_min[i])
					feature_min[i] = feature[i];
				if (feature[i] > feature_max[i])
					feature_max[i] = feature[i];
			}
		}
	}

	public List<Data> scale(List<Data> original, boolean ifScale) {
		List<Data> result = null;

		if (original != null) {
			result = new ArrayList<Data>(original.size());

			for (Data data : original) {
				Double[] feature = new Double[data.getFeature().length];
				Double label = data.getLabel();

				for (int i = 0; i < feature.length; i++) {
					if (ifScale) {
						if ((feature_min[i] - feature_max[i]) == 0)
							feature[i] = 0.0;
						else
							feature[i] = lower + (upper - lower) * (data.getFeature()[i] - feature_min[i]) / (feature_max[i] - feature_min[i]);
					} else
						feature[i] = data.getFeature()[i];
					
					if (feature[i].toString().equals("NaN"))
						throw new RuntimeException(String.format("Scale error"));
				}

				result.add(new Data(feature, label));
			}
		}

		return result;
	}

	// Save to libSVM format scale file.
	public void save(String scaleModelFilePath) throws FileNotFoundException {
		PrintWriter pw = new PrintWriter(scaleModelFilePath);

		pw.println("x");
		pw.println(String.format("%f %f", lower, upper));
		for (int i = 0; i < feature_max.length; i++)
			pw.println(String.format("%d %f %f", i + 1, feature_min[i], feature_max[i]));
		pw.close();
	}

	public static ScaleModel load(String scaleModelFilePath) throws IOException {
		ScaleModel scaleModel = null;

		BufferedReader br = new BufferedReader(new FileReader(scaleModelFilePath));
		String line = br.readLine();

		if (line.equals("x")) {
			line = br.readLine();
			StringTokenizer st = new StringTokenizer(line, " \n");
			double lower = Double.parseDouble(st.nextToken());
			double upper = Double.parseDouble(st.nextToken());

			ArrayList<Double> mins = new ArrayList<Double>();
			ArrayList<Double> maxs = new ArrayList<Double>();
			line = br.readLine();
			while (line != null) {
				st = new StringTokenizer(line, " \n");
				st.nextToken();
				mins.add(Double.parseDouble(st.nextToken()));
				maxs.add(Double.parseDouble(st.nextToken()));
				line = br.readLine();
			}
			br.close();

			scaleModel = new ScaleModel(maxs, mins, upper, lower);
		}

		br.close();
		return scaleModel;
	}
}
