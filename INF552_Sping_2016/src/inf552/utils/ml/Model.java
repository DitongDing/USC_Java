package inf552.utils.ml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import inf552.bean.ml.Data;
import inf552.bean.ml.ScaleModel;
import inf552.utils.ComUtils;
import inf552.utils.preprocessor.PreProcessor;

public final class Model {
	private PreProcessor preProcessor;
	private ScaleModel scaleModel;
	private boolean ifScale;
	private Classifier classifier;

	private static final String MODEL_FILE = "model.file";
	private static final String SCALE_FILE = "scale.file";
	private static final Double UPPER = 1.0;
	private static final Double LOWER = -1.0;

	public Model(PreProcessor preProcessor, boolean ifScale, Classifier classifier) {
		this.preProcessor = preProcessor;
		this.ifScale = ifScale;
		this.classifier = classifier;
	}

	public Model() {

	}

	public List<Data> predict(List<Data> dataSet) {
		// Pre-process
		List<Data> processedData = preProcessor.preProcess(dataSet);
		processedData = scaleModel.scale(processedData, ifScale);

		// Predict by classifier;
		List<Data> result = classifier.predict(processedData);

		return result;
	}

	public void train(List<Data> dataSet) {
		// Pre-process
		List<Data> processedData = preProcessor.preProcess(dataSet);
		scaleModel = new ScaleModel(processedData, UPPER, LOWER);
		processedData = scaleModel.scale(processedData, ifScale);

		// Train by classifier
		classifier.train(processedData);
	}

	public void save(String path) {
		File dir = new File(path);
		dir.mkdir();
		try {
			PrintWriter pw = new PrintWriter(ComUtils.getFilePath(dir.getPath(), MODEL_FILE));

			pw.println(preProcessor.getClass().getName());
			scaleModel.save(ComUtils.getFilePath(dir.getPath(), SCALE_FILE));
			pw.println(ifScale);
			pw.println(classifier.getClass().getName());

			pw.close();

			classifier.save(path);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Model save error");
		}
	}

	public void load(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(ComUtils.getFilePath(dir.getPath(), MODEL_FILE)));

				preProcessor = (PreProcessor) Class.forName(br.readLine()).newInstance();
				scaleModel = ScaleModel.load(ComUtils.getFilePath(dir.getPath(), SCALE_FILE));
				ifScale = Boolean.valueOf(br.readLine());
				classifier = (Classifier) Class.forName(br.readLine()).newInstance();

				br.close();

				classifier.load(path);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Model load error");
			}
		} else
			throw new RuntimeException("Model does not exist");
	}

	@Override
	public String toString() {
		return String.format("Model: preProcessor=%s, ifScale=%b, classifier=%s", preProcessor.getClass().getName(), ifScale, classifier.toString());
	}
}