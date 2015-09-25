package ddt.test.crawljax;

import java.util.concurrent.TimeUnit;

import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;

import ddt.test.crawljax.condition.SearchResultPageCondition;
import ddt.test.crawljax.condition.SortedURLCondition;
import ddt.test.crawljax.form.FillFormBeforeClick;
import ddt.test.crawljax.form.FormData;

// TODO: use when to control times of Search.
public class Rules {
	private static final long WAIT_TIME_AFTER_RELOAD = 500;
	private static final long WAIT_TIME_AFTER_EVENT = 500;

	private static final String[][] UPDATE_FIELD_NAMES = { { "emp_login" } };
	private static final String[][] UPDATE_FIELD_VALUES = { { "admin" } };
	private static final String[][] INSERT_FIELD_NAMES = { { "emp_login" } };
	private static final String[][] INSERT_FIELD_VALUES = { { "DDT" } };

	public static void addRules(CrawljaxConfigurationBuilder builder) {
		CrawlRulesBuilder rules = builder.crawlRules();
		InputSpecification input = new InputSpecification();
		rules.insertRandomDataInInputForms(false);
		rules.clickElementsInRandomOrder(false);

		// Condition
		// Do not crawl the sorted page.
		rules.addCrawlCondition("Do not crawl the sorted page", new SortedURLCondition());
		// Do not crawl the search result page for more than once.
		rules.addCrawlCondition("Do not crawl the search result page for more than once",
				new SearchResultPageCondition());

		// Click
		// Do not click logout
		rules.dontClick("input").underXPath("//INPUT[@type='submit' and @value='Logout']");
		// Fill Update Empl before click Update button. Only once
//		rules.click("input").underXPath("//INPUT[@type='submit' and @value='Update']")
//				.when(new FillFormBeforeClick(UPDATE_FIELD_NAMES, UPDATE_FIELD_VALUES));
//		// Fill Insert Empl before click Insert button. Only once
//		rules.click("input").underXPath("//INPUT[@type='submit' and @value='Insert']")
//				.when(new FillFormBeforeClick(INSERT_FIELD_NAMES, INSERT_FIELD_VALUES));
		// Click all input with type submit|button
		rules.click("input").underXPath("//INPUT[@type='submit' or @type='button']");
		// Click all links with href
		rules.click("a").underXPath("//A[@href]");
		// Do not click the links that row position > 3 and row position != last.
		rules.dontClick("a").underXPath("//TBODY/TR[position()>3 and position()!=last()]");

		// Input
		// Input for forms, including search, department, empl edit.
		// Remove befdoreClickElement(). it doesn't work.
		input.setValuesInForm(new FormData());
		// .beforeClickElement("input").underXPath(
		// "//INPUT[(@value='Search' or @value='Update' or @value='Insert' or @value='Login') and @type='submit']");
		rules.setInputSpec(input);

		// Set timeouts
		builder.crawlRules().waitAfterReloadUrl(WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);
	}
}