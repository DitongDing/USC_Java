package ddt.utils;

import java.util.Random;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.state.Identification;

public class ComUtils {
	@Deprecated
	public static String[] nextInput(String[][] values) {
		String[] result = new String[values.length];
		Random r = new Random();

		for (int i = 0; i < values.length; i++) {
			int j = r.nextInt(values[i].length);
			result[i] = values[i][j];
		}

		return result;
	}

	@Deprecated
	public static void fillInput(EmbeddedBrowser browser, String[][] filedNames, String[] fieldValues) {
		for (int i = 0; i < filedNames.length; i++)
			for (int j = 0; j < filedNames[i].length; j++)
				browser.input(new Identification(Identification.How.name, filedNames[i][j]), fieldValues[i]);
	}

	public static class RepeatUnit {
		public String string;
		public int count;

		public RepeatUnit(String string, int count) {
			this.string = string;
			this.count = count;
		}
	}

	public static String[] getRepeatedInputs(RepeatUnit... repeatUnits) {
		String[] result = null;

		int count = 0;
		for (RepeatUnit repeatUnit : repeatUnits)
			count += repeatUnit.count;

		result = new String[count];
		int index = 0;
		for (RepeatUnit repeatUnit : repeatUnits)
			for (int i = 0; i < repeatUnit.count; i++)
				result[index++] = repeatUnit.string;

		return result;
	}

	public static String[] getDifferentInputs(int count) {
		String[] result = new String[count];

		for (int i = 0; i < count; i++)
			result[i] = Integer.toString(i);

		return result;
	}

	public static String[] combine(String[]... arrays) {
		String[] result = null;

		int count = 0;
		for (String[] array : arrays)
			count += array.length;

		result = new String[count];
		int index = 0;
		for (String[] array : arrays)
			for (String string : array)
				result[index++] = string;

		return result;
	}
}
