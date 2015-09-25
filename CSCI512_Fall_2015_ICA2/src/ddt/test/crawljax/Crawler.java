package ddt.test.crawljax;

import java.io.PrintWriter;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

import ddt.test.crawljax.plugin.LoginPlugin;
import ddt.test.crawljax.plugin.PreStateCrawlingPlugin;

public class Crawler {
	// TODO: modify builder properties if needed. 200 20 1
	// TODO: unlogin check on adminMenu.jsp
	// TODO: control input for empl update and insert. (as they are using same value, insert cannot happen because of
	// duplicate.)
	private static final int MAX_STATES = 200;
	private static final int MAX_DEPTH = 10;
	private static final long MAX_RUNTIME = 10;
	private static final TimeUnit RUNTIME_UNIT = TimeUnit.MINUTES;
	private static final BrowserType BROWSER_TYPE = BrowserType.FIREFOX;
	private static final int BROWSER_NUM = 1;

	// Use PLUGIN_LIST to initiate plugin to builder, which contains the full
	// name of plugin classes.
	private CrawljaxConfigurationBuilder builder;
	private CrawljaxRunner runner;
	private PrintWriter pw;

	public Crawler(String URL, String loginURL) throws Exception {
		// Get init builder
		builder = CrawljaxConfiguration.builderFor(URL);

		// Add plugins
		builder.addPlugin(new LoginPlugin(new URI(loginURL)));
		pw = new PrintWriter("./output");
		builder.addPlugin(new PreStateCrawlingPlugin(pw));

		// Add rules
		Rules.addRules(builder);

		// Set browser, depth, runtime, state, auth, etc.
		generalSetting();

		// Finally, set runner
		runner = new CrawljaxRunner(builder.build());
	}

	public void run() {
		runner.call();
		pw.close();
	}

	private void generalSetting() {
		builder.setMaximumStates(MAX_STATES);
		builder.setMaximumDepth(MAX_DEPTH);
		builder.setMaximumRunTime(MAX_RUNTIME, RUNTIME_UNIT);
		builder.setBrowserConfig(new BrowserConfiguration(BROWSER_TYPE, BROWSER_NUM));
	}
}
