package test;

import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.openqa.selenium.ElementNotVisibleException;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.CandidateElement;
import com.crawljax.core.CrawlSession;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.ExitNotifier.ExitStatus;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.InputSpecification;
import com.crawljax.core.configuration.CrawlRules;
import com.crawljax.core.configuration.CrawlRules.CrawlRulesBuilder;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.plugin.OnBrowserCreatedPlugin;
import com.crawljax.core.plugin.OnNewStatePlugin;
import com.crawljax.core.plugin.OnUrlLoadPlugin;
import com.crawljax.core.plugin.PostCrawlingPlugin;
import com.crawljax.core.plugin.PreCrawlingPlugin;
import com.crawljax.core.plugin.PreStateCrawlingPlugin;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.Identification;
import com.crawljax.core.state.StateVertex;
import com.crawljax.forms.FormHandler;
import com.google.common.collect.ImmutableList;

import ddt.test.crawljax.form.FormData;
import ddt.test.crawljax.plugin.LoginPlugin;

public class Test {
	public static String URL = "http://localhost:8080/empldir/Default.jsp";
	public static String LOGIN_URL = "http://localhost:8080/empldir/Login.jsp";

	public static void main(String[] args) throws Exception {
		final PrintWriter pw = new PrintWriter("output/output.txt");

		CrawljaxConfigurationBuilder builder = CrawljaxConfiguration.builderFor(URL);
		CrawlRulesBuilder rules = builder.crawlRules();
		rules.insertRandomDataInInputForms(false);
		builder.setMaximumDepth(7);

		// Do not click logout
		rules.dontClick("input").underXPath("//INPUT[@type='submit' and @value='Logout']");
		// Click all input with type submit|button
		rules.click("input").underXPath("//INPUT[@type='submit' or @type='button']");
		// Click all links with href
		rules.click("a").underXPath("//A[@href]");
		// Do not click the links that row position > 3 and row position != last.
		rules.dontClick("a").underXPath("//TBODY/TR[position()>3 and position()!=last()]");

		builder.addPlugin(new PreStateCrawlingPlugin() {
			public void preStateCrawling(CrawlerContext context, ImmutableList<CandidateElement> candidateElements,
					StateVertex state) {
				pw.println(state.getUrl() + "\t" + candidateElements.size());
			}
		});

		builder.addPlugin(new LoginPlugin(new URI(LOGIN_URL)));

		CrawljaxRunner runner = new CrawljaxRunner(builder.build());
		runner.call();

		pw.close();
	}
}