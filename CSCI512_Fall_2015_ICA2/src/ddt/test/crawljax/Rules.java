package ddt.test.crawljax;

import java.util.concurrent.TimeUnit;

import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.configuration.InputSpecification;

import ddt.test.crawljax.condition.SearchResultPageCondition;
import ddt.test.crawljax.condition.SortedURLCondition;
import ddt.test.crawljax.form.UIForm;
import ddt.test.crawljax.form.UISForm;

public class Rules {
	private static final long WAIT_TIME_AFTER_RELOAD = 500;
	private static final long WAIT_TIME_AFTER_EVENT = 500;

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
		// Click all input with type submit|button
		rules.click("input").underXPath("//INPUT[@type='submit' or @type='button']");
		// Click all links with href
		rules.click("a").underXPath("//A[@href]");
		// Do not click the links that row position > 3 and row position != last.
		rules.dontClick("a").underXPath("//TBODY/TR[position()>3 and position()!=last()]");

		// Input
		// Input for forms, including search, department, empl edit.
		// Update/Insert/Search forms.
		input.setValuesInForm(new UISForm()).beforeClickElement("input")
				.underXPath("//INPUT[(@value='Update' or @value='Insert' or @value='Search') and @type='submit']");
		// Update/Insert forms.
		input.setValuesInForm(new UIForm()).beforeClickElement("input")
				.underXPath("//INPUT[(@value='Update' or @value='Insert') and @type='submit']");
		rules.setInputSpec(input);

		// Set timeouts
		builder.crawlRules().waitAfterReloadUrl(WAIT_TIME_AFTER_RELOAD, TimeUnit.MILLISECONDS);
		builder.crawlRules().waitAfterEvent(WAIT_TIME_AFTER_EVENT, TimeUnit.MILLISECONDS);
	}
}