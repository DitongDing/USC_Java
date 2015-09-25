package ddt.test.crawljax;

import java.util.concurrent.TimeUnit;

import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;

import ddt.test.crawljax.form.FormData;

// TODO: think about how to control click some button for only once.
public class Rules {
	// TODO: modify timeout for reload and event if needed.
	private static long WAIT_TIME_AFTER_RELOAD = 500;
	private static long WAIT_TIME_AFTER_EVENT = 500;

	public static void addRules(CrawljaxConfigurationBuilder builder) {
		CrawlRulesBuilder rules = builder.crawlRules();
		InputSpecification input = new InputSpecification();
		rules.insertRandomDataInInputForms(true);
		rules.clickElementsInRandomOrder(false);

		// Click
		// Do not click logout
		rules.dontClick("input").underXPath("//INPUT[@type='submit' and @value='Logout']");
		// Click all input with type submit|button
		rules.click("input").underXPath("//INPUT[@type='submit' or @type='button']");
		// Click all links with href
		rules.click("a").underXPath("//A[@href]");
		// Do not click the links that row position > 3 and row position != last.
		rules.dontClick("a").underXPath("//TBODY/TR[position()>3 and position()!=last()]");

		// Input
		// Input for forms, including search, department, user.
		input.setValuesInForm(new FormData()).beforeClickElement("input")
				.underXPath("//INPUT[(@value='Search' or @value='Update' or @value='Insert') and @type='submit']");
		rules.setInputSpec(input);

		// Set timeouts
		builder.crawlRules().waitAfterReloadUrl(WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);
	}
}