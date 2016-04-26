package train;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import inf552.bean.ml.Data;

// 1) Move (random x, random y). -50~50
// 2) Scale ramdom rate. 0.5~3
// 3) Spin random angle around (0,0). 0~2π
// Cannot perform spin in 3D.
// For each data, perform 1), 2), 3) and 123)
public class GenerateMoreData {
	public static void main(String[] args) throws Exception {
		String input = "output/jaffe_SpaceALocation";
		String output = "output/jaffe_SpaceALocation_generated";
		List<Data> trainSet = new ArrayList<Data>();

		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = br.readLine();
		while (line != null) {
			trainSet.add(Data.valueOf(line));
			line = br.readLine();
		}
		br.close();

		Double[] moveRange = { -50.0, 50.0 };
		Double[] scaleRange = { 0.5, 3.0 };
		Double[] spinRange = { 0.0, Math.PI };
		Random r = new Random();

		List<Data> generatedData = new ArrayList<Data>();
		for (Data data : trainSet) {
			// Move
			generatedData.add(move(data, getRandom(r, moveRange), getRandom(r, moveRange)));
			// Scale
			generatedData.add(scale(data, getRandom(r, scaleRange)));
			// Spin
			generatedData.add(spin(data, getRandom(r, spinRange)));
			// Combined
			generatedData.add(spin(scale(move(data, getRandom(r, moveRange), getRandom(r, moveRange)), getRandom(r, scaleRange)), getRandom(r, spinRange)));
		}

		trainSet.addAll(generatedData);

		PrintWriter pw = new PrintWriter(output);

		for (Data data : trainSet)
			pw.println(data.toString());

		pw.close();
	}

	public static Double getRandom(Random r, Double[] range) {
		return r.nextDouble() * (range[1] - range[0]) + range[0];
	}

	public static Data move(Data original, double x, double y) {
		Data result = (Data) original.clone();

		for (int i = 0; i < result.getFeature().length;) {
			result.getFeature()[i++] += x;
			result.getFeature()[i++] += y;
		}

		return result;
	}

	public static Data scale(Data original, double rate) {
		Data result = (Data) original.clone();

		for (int i = 0; i < result.getFeature().length; i++)
			result.getFeature()[i] *= rate;

		return result;
	}

	public static Data spin(Data original, double angle) {
		Data result = (Data) original.clone();

		for (int i = 0; i < result.getFeature().length;) {
			double x = original.getFeature()[i];
			double y = original.getFeature()[i + 1];

			result.getFeature()[i] = x * Math.cos(angle) - y * Math.sin(angle);
			result.getFeature()[i + 1] = x * Math.sin(angle) + y * Math.cos(angle);

			i += 2;
		}

		return result;
	}
}