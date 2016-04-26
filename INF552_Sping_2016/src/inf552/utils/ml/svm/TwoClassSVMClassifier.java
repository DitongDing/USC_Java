package inf552.utils.ml.svm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import inf552.bean.ml.Data;
import inf552.utils.ComUtils;
import inf552.utils.ml.Classifier;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

public class TwoClassSVMClassifier extends Classifier {
	private svm_model svmModel;
	private svm_parameter param;
	private boolean ifDefaultGamma;

	private static String MODEL_FILE = "svm.model";
	private static String PARAM_FILE = "svm.param";

	public TwoClassSVMClassifier(Double C, Double gamma, Set<Double> classes) {
		param = initializeSVMParameter(C, gamma);
		this.ifDefaultGamma = gamma == null;
		this.classes = classes;
		if (classes.size() != 2)
			throw new RuntimeException("TwoClassSVMClassifier classes error: must be two classes");
	}

	public TwoClassSVMClassifier() {

	}

	@Override
	public List<Data> predict(List<Data> dataSet) {
		dataSet = Data.clone(dataSet);

		for (Data data : dataSet) {
			svm_node[] x = translateDataToSVMNodes(data);
			data.setLabel(svm.svm_predict(svmModel, x));
		}

		return dataSet;
	}

	public Data predict(Data data) {
		data = (Data) data.clone();
		svm_node[] x = translateDataToSVMNodes(data);
		data.setLabel(svm.svm_predict(svmModel, x));
		return data;
	}

	@Override
	public void train(List<Data> dataSet) {
		svm_problem problem = translateDataSetToSVMProblem(dataSet);

		if (ifDefaultGamma)
			param.gamma = 1.0 / dataSet.get(0).getFeature().length;

		svmModel = svm.svm_train(problem, param);
	}

	@Override
	public void save(String path) {
		File dir = new File(path);
		dir.mkdir();
		try {
			svm.svm_save_model(ComUtils.getFilePath(dir.getPath(), MODEL_FILE), svmModel);
			saveParam(ComUtils.getFilePath(dir.getPath(), PARAM_FILE));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("SVM model save error");
		}
	}

	@Override
	public void load(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			try {
				svmModel = svm.svm_load_model(ComUtils.getFilePath(dir.getPath(), MODEL_FILE));
				loadParam(ComUtils.getFilePath(dir.getPath(), PARAM_FILE));
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("SVM model load error");
			}
		} else
			throw new RuntimeException("SVM model does not exist");
	}

	@Override
	public String toString() {
		return String.format("SVM(C=%f, gamma=%f)", param.C, param.gamma);
	}

	private svm_node[] translateDataToSVMNodes(Data data) {
		svm_node[] result = new svm_node[data.getFeature().length];

		for (int i = 0, j = 1; i < result.length; i++, j++) {
			result[i] = new svm_node();
			result[i].index = j;
			result[i].value = data.getFeature()[i];
		}

		return result;
	}

	private svm_problem translateDataSetToSVMProblem(List<Data> dataSet) {
		svm_problem result = new svm_problem();

		result.l = dataSet.size();
		result.x = new svm_node[result.l][];
		result.y = new double[result.l];

		int index = 0;
		for (Data data : dataSet) {
			result.x[index] = translateDataToSVMNodes(data);
			result.y[index] = data.getLabel();
			if (!classes.contains(data.getLabel()))
				throw new RuntimeException("TwoClassSVMClassifier class label error");
			index++;
		}

		return result;
	}

	private svm_parameter initializeSVMParameter(Double C, Double gamma) {
		svm_parameter result = new svm_parameter();
		result.svm_type = svm_parameter.C_SVC;
		result.kernel_type = svm_parameter.RBF;
		result.degree = 3;
		result.gamma = gamma == null ? Double.NaN : gamma;
		result.coef0 = 0;
		result.nu = 0.5;
		result.cache_size = 100;
		result.C = C;
		result.eps = 1e-3;
		result.p = 0.1;
		result.shrinking = 1;
		result.probability = 0;
		result.nr_weight = 0;
		result.weight_label = new int[0];
		result.weight = new double[0];
		return result;
	}

	private void saveParam(String path) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(path);

			pw.println(param.C);
			pw.println(param.gamma);
			pw.println(ifDefaultGamma);
			for (Double Class : classes)
				pw.println(Class);

			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadParam(String path) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(path));

		Double C = Double.valueOf(br.readLine());
		Double gamma = Double.valueOf(br.readLine());
		param = initializeSVMParameter(C, gamma);
		ifDefaultGamma = Boolean.valueOf(br.readLine());
		classes = new HashSet<Double>();
		String line = br.readLine();
		while (line != null) {
			classes.add(Double.valueOf(line));
			line = br.readLine();
		}

		br.close();
	}
}