package inf552.utils;

import java.util.List;

import inf552.utils.ml.Model;
import inf552.utils.ml.bean.Data;

// Image->Emotion
public class Recognizer {
	private Model model;

	public Recognizer(Model model) {
		this.model = model;
	}

	public List<Data> recognize(List<String> filePaths) {
		List<Data> result = null;

		// TODO: Finish recognizer

		return result;
	}
}