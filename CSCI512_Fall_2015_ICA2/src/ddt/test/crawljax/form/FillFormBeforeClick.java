package ddt.test.crawljax.form;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.condition.Condition;

import ddt.utils.ComUtils;

@Deprecated
public class FillFormBeforeClick implements Condition {
	private String[][] fieldNames = { {} };
	private String[][] fieldValues = { {} };

	public FillFormBeforeClick(String[][] fieldNames, String[][] fieldValues) {
		this.fieldNames = fieldNames;
		this.fieldValues = fieldValues;
	}

	public boolean check(EmbeddedBrowser browser) {
		ComUtils.fillInput(browser, fieldNames, ComUtils.nextInput(fieldValues));
		return true;
	}
}