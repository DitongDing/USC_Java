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

	public List<String> recognize(List<String> filePaths) {
		List<String> result = new ArrayList<String>();

		List<Data> dataSet = new ArrayList<Data>();

		for (String filePath : filePaths)
			dataSet.add(FaceUtils.extractFacialLocation_ByFile(new File(filePath)).toData(null));

		dataSet = model.predict(dataSet);

		for (Data data : dataSet)
			result.add(Constants.emotions[(int) Math.floor(data.getLabel())]);

		return result;
	}
}