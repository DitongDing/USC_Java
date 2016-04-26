package inf552.utils.ml.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import inf552.bean.ml.Data;
import inf552.utils.ml.Classifier;

@Deprecated
// TODO: <3 LOW> Finish NNClassifier
// Feature -> [0, 1]
public class NNClassifier extends Classifier {
	private MultiLayerPerceptron neuralNetwork;
	private List<Integer> hiddenLayers;

	private static final String NN_FILE = "nn.file";

	public NNClassifier() {

	}

	public NNClassifier(List<Integer> hiddenLayers, Set<Double> classes) {
		this.hiddenLayers = hiddenLayers;
		this.classes = classes;
	}

	@Override
	public List<Data> predict(List<Data> dataSet) {
		List<Data> result = Data.clone(dataSet);

		for (Data data : result) {
			double[] input = new double[data.getFeature().length];
			for (int i = 0; i < input.length; i++)
				input[i] = data.getFeature()[i];

			neuralNetwork.setInput(input);
			double[] output = neuralNetwork.getOutput();

			Double label = null;
			double maxValue = Double.NEGATIVE_INFINITY;
			for (int i = 0; i < output.length; i++)
				if (maxValue < output[i]) {
					maxValue = output[i];
					label = (double) i;
				}

			data.setLabel(label);
		}

		return result;
	}

	@Override
	public void train(List<Data> dataSet) {
		int inputSize = dataSet.get(0).getFeature().length;
		int outputSize = classes.size();
		List<Integer> layers = new ArrayList<Integer>(hiddenLayers);
		layers.add(0, inputSize);
		layers.add(outputSize);

		neuralNetwork = new MultiLayerPerceptron(layers);
		DataSet trainData = translateToDataSet(inputSize, outputSize, dataSet);
		neuralNetwork.learn(trainData);
	}

	@Override
	public void save(String path) {
	}

	@Override
	public void load(String path) {
	}

	private DataSet translateToDataSet(int inputSize, int outputSize, List<Data> dataSet) {
		DataSet result = new DataSet(inputSize, outputSize);

		for (Data data : dataSet) {
			double[] input = new double[inputSize];
			double[] output = new double[outputSize];

			for (int i = 0; i < inputSize; i++)
				input[i] = data.getFeature()[i];
			output[(int) Math.floor(data.getLabel())] = 1;

			result.addRow(input, output);
		}

		return result;
	}
}