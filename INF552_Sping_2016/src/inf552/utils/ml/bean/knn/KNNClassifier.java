package inf552.utils.ml.bean.knn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import inf552.bean.ml.Data;
import inf552.utils.ComUtils;
import inf552.utils.ml.Classifier;

public class KNNClassifier extends Classifier {
	private Integer K;
	private Integer lp;
	private List<Data> knownDataSet;

	private static final String KNN_FILE = "knn.model";

	public KNNClassifier(Integer K, Integer lp, Set<Double> classes) {
		this.K = K;
		this.lp = lp;
		this.classes = classes;
	}

	public KNNClassifier() {

	}

	@Override
	public List<Data> predict(List<Data> dataSet) {
		dataSet = Data.clone(dataSet);

		for (Data data : dataSet) {
			List<KNNPair> KNNPairs = new ArrayList<KNNPair>();
			for (Data knownData : knownDataSet)
				KNNPairs.add(new KNNPair(ComUtils.getDistance(data.getFeature(), knownData.getFeature(), lp), knownData.getLabel()));

			Collections.sort(KNNPairs);

			// Count times.
			Map<Double, Integer> labelToCount = new HashMap<Double, Integer>();
			int i = 0;
			for (KNNPair KNNPair : KNNPairs) {
				if (i++ >= K)
					break;
				Integer count = labelToCount.get(KNNPair.label);
				if (count == null)
					count = 0;
				labelToCount.put(KNNPair.label, count + 1);
			}

			// Get most frequent one.
			Double label = null;
			Integer count = null;
			for (Entry<Double, Integer> entry : labelToCount.entrySet()) {
				if (count == null || count < entry.getValue()) {
					label = entry.getKey();
					count = entry.getValue();
				}
			}

			data.setLabel(label);
		}

		return dataSet;
	}

	@Override
	public void train(List<Data> dataSet) {
		knownDataSet = Data.clone(dataSet);
	}

	@Override
	public void save(String path) {
		File dir = new File(path);
		dir.mkdir();
		try {
			PrintWriter pw = new PrintWriter(ComUtils.getFilePath(dir.getPath(), KNN_FILE));

			pw.println(K);
			pw.println(lp);
			for (Data data : knownDataSet)
				pw.println(data.toString());

			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("KNN model save error");
		}
	}

	@Override
	public void load(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(ComUtils.getFilePath(dir.getPath(), KNN_FILE)));

				K = Integer.valueOf(br.readLine());
				lp = Integer.valueOf(br.readLine());
				knownDataSet = new ArrayList<Data>();
				String line = br.readLine();
				while (line != null) {
					knownDataSet.add(Data.valueOf(line));
					line = br.readLine();
				}

				br.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("SVM model load error");
			}
		} else
			throw new RuntimeException("SVM model does not exist");
	}

	@Override
	public String toString() {
		return String.format("KNN: K=%d, lp=%d", K, lp);
	}

	protected class KNNPair implements Comparable<KNNPair> {
		public Double distance;
		public Double label;

		public KNNPair(Double distance, Double label) {
			super();
			this.distance = distance;
			this.label = label;
		}

		@Override
		public int compareTo(KNNPair arg0) {
			return distance.compareTo(arg0.distance);
		}
	}
}