package inf552.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.List;

import inf552.utils.ml.Classifier;
import inf552.utils.ml.bean.Data;
import inf552.utils.preprocessor.PreProcessor;

// Image->Emotion
public class Recognizer {
	public PreProcessor preProcessor;
	public Classifier classifier;
	private static final String RECOGNIZER_FILE = "recognizer.file";

	public Recognizer(PreProcessor preProcessor, Classifier classifier) {
		this.preProcessor = preProcessor;
		this.classifier = classifier;
	}

	public Recognizer(String path) {
		load(path);
	}
	
	public List<Data> recognize(List<String> filePaths) {
		List<Data> result = null;
		
		// TODO: Finish recognizer
		return result;
	}

	public void save(String path) {
		File dir = new File(path);
		dir.mkdir();
		try {
			PrintWriter pw = new PrintWriter(ComUtils.getFilePath(dir.getPath(), RECOGNIZER_FILE));

			pw.println(preProcessor.getClass().getName());
			pw.println(classifier.getClass().getName());
			classifier.save(path);

			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException("Recognizer save error");
		}
	}

	public void load(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(ComUtils.getFilePath(dir.getPath(), RECOGNIZER_FILE)));

				preProcessor = (PreProcessor) Class.forName(br.readLine()).newInstance();
				classifier = (Classifier) Class.forName(br.readLine()).newInstance();
				classifier.load(path);

				br.close();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("Recognizer load error");
			}
		} else
			throw new RuntimeException("Recognizer does not exist");
	}
}