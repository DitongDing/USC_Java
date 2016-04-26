package inf552.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import inf552.bean.ml.Data;
import inf552.utils.ml.Model;

// Image->Emotion
public class Recognizer {
	private Model model;

	public Recognizer(Model model) {
		this.model = model;
	}

	public Recognizer(String path) {
		this.model = new Model();
		this.model.load(path);
	}

	public List<Data> recognize(List<String> filePaths) {
		List<Data> result = new ArrayList<Data>();

		for (String filePath : filePaths)
			result.add(FaceUtils.extractFacialLocation_ByFile(new File(filePath)).toData(null));

		result = model.predict(result);

		return result;
	}
}