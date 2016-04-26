package inf552.utils.ml.bean.knn;

import java.util.List;
import java.util.Set;

import inf552.utils.ml.Classifier;
import inf552.utils.ml.bean.Data;

public class KNNClassifier extends Classifier {
	private Integer K;
	private Integer lp;
	private List<Data> knownDataSet;
	private Set<Double> classes;

	public KNNClassifier(Integer K, Integer lp, Set<Double> classes) {
		this.K = K;
		this.lp = lp;
		this.classes = classes;
	}

	@Override
	public List<Data> predict(List<Data> dataSet) {		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void train(List<Data> dataSet) {
		knownDataSet = dataSet;
		// TODO Auto-generated method stub
	}

	@Override
	public void save(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void load(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public Classifier cloneBeforeTraining() {
		return new KNNClassifier(K, lp, classes);
	}

	protected class KNNPair implements Comparable<KNNPair> {
		public Double distance;
		public Double label;

		@Override
		public int compareTo(KNNPair arg0) {
			return distance.compareTo(arg0.distance);
		}
	}
}