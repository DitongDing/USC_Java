package ddt.utils;

import java.util.Random;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.state.Identification;

public class ComUtils {
	public static String[] nextInput(String[][] values) {
		String[] result = new String[values.length];
		Random r = new Random();

		for (int i = 0; i < values.length; i++) {
			int j = r.nextInt(values[i].length);
			result[i] = values[i][j];
		}

		return result;
	}

	public static void fillInput(EmbeddedBrowser browser, String[][] filedNames, String[] fieldValues) {
		for (int i = 0; i < filedNames.length; i++)
			for (int j = 0; j < filedNames[i].length; j++)
				browser.input(new Identification(Identification.How.name, filedNames[i][j]), fieldValues[i]);
	}
}
