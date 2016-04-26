package inf552.utils.ml.svm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import inf552.bean.ml.Data;
import inf552.utils.ComUtils;
import inf552.utils.ml.Classifier;

// Only use one vs one classifier.
public class MultiClassSVMClassifier extends Classifier {
	private Map<String, TwoClassSVMClassifier> classifiers;

	// private static final String MULTICLASS_SVM_FILE = "multi.svm.file";
	private static final int C = 0;
	private static final int GAMMA = 1;

	public MultiClassSVMClassifier() {

	}

	public MultiClassSVMClassifier(Map<String, Double[]> CGammaMap, Set<Double> classes) {
		classifiers = new HashMap<String, TwoClassSVMClassifier>();
		for (Entry<String, Double[]> entry : CGammaMap.entrySet()) {
			classifiers.put(entry.getKey(), new TwoClassSVMClassifier(entry.getValue()[C], entry.getValue()[GAMMA], getClassifierClass(entry.getKey())));
		}
		this.classes = classes;
	}

	@Override
	public List<Data> predict(List<Data> dataSet) {
		dataSet = Data.clone(dataSet);

		for (Data data : dataSet) {
			Map<Double, Integer> labelCount = new HashMap<Double, Integer>();
			for (TwoClassSVMClassifier classifier : classifiers.values()) {
				Double label = classifier.predict(data).getLabel();
				Integer count = labelCount.get(label);
				if (count == null)
					count = 0;
				labelCount.put(label, count + 1);
			}

			// Get most frequent one.
			Double label = null;
			Integer count = null;
			for (Entry<Double, Integer> entry : labelCount.entrySet()) {
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
		Map<Double, List<Data>> dataSetCategory = new HashMap<Double, List<Data>>();
		for (Data data : dataSet) {
			List<Data> subDataSet = dataSetCategory.get(data.getLabel());
			if (subDataSet == null) {
				subDataSet = new ArrayList<Data>();
				dataSetCategory.put(data.getLabel(), subDataSet);
			}
			subDataSet.add(data);
		}

		Double[] classesArray = classes.toArray(new Double[0]);
		for (int i = 0; i < classesArray.length; i++)
			for (int j = i + 1; j < classesArray.length; j++) {
				Classifier classifier = classifiers.get(getClassifierName(classesArray[i], classesArray[j]));
				if (classifier == null)
					classifier = classifiers.get(getClassifierName(classesArray[j], classesArray[i]));
				if (classifier == null)
					throw new RuntimeException(
							String.format("MultiClassSVMClassifier train error: no classifier for %.0f and %.0f", classesArray[i], classesArray[j]));

				dataSet = new ArrayList<Data>();
				dataSet.addAll(dataSetCategory.get(classesArray[i]));
				dataSet.addAll(dataSetCategory.get(classesArray[j]));
				classifier.train(dataSet);
			}
	}

	@Override
	public void save(String path) {
		File dir = new File(path);
		dir.mkdir();
		try {
			for (Entry<String, TwoClassSVMClassifier> entry : classifiers.entrySet())
				entry.getValue().save(ComUtils.getFilePath(dir.getPath(), entry.getKey()));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("MultiClassSVMClassifier save error");
		}
	}

	@Override
	public void load(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			try {
				classifiers = new HashMap<String, TwoClassSVMClassifier>();
				for (File classifierDir : dir.listFiles()) {
					if (classifierDir.isDirectory()) {
						String classifierName = classifierDir.getName();
						TwoClassSVMClassifier classifier = new TwoClassSVMClassifier();
						classifier.load(classifierDir.getPath());
						classifiers.put(classifierName, classifier);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("MultiClassSVMClassifier load error");
			}
		}
	}

	private String getClassifierName(Double class0, Double class1) {
		return String.format("%.0f_%.0f", class0, class1);
	}

	private Set<Double> getClassifierClass(String classifierName) {
		StringTokenizer st = new StringTokenizer(classifierName, "_");
		Set<Double> result = new HashSet<Double>();

		while (st.hasMoreTokens())
			result.add(Double.valueOf(st.nextToken()));

		return result;
	}
}