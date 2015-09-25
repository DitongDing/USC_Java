package ddt.test.crawljax;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import com.crawljax.browser.EmbeddedBrowser.BrowserType;
import com.crawljax.core.CrawljaxRunner;
import com.crawljax.core.configuration.BrowserConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration;
import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;

import ddt.test.crawljax.plugin.LoginPlugin;

public class Crawler {
	// TODO: modify builder properties if needed. 200 20 1
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

	public Crawler(String URL, String loginURL) throws URISyntaxException {
		// Get init builder
		builder = CrawljaxConfiguration.builderFor(URL);

		// Add plugins
		builder.addPlugin(new LoginPlugin(new URI(loginURL)));

		// Add rules
		Rules.addRules(builder);

		// Set browser, depth, runtime, state, auth, etc.
		generalSetting();

		// Finally, set runner
		runner = new CrawljaxRunner(builder.build());
	}

	public void run() {
		runner.call();
	}

	private void generalSetting() {
		builder.setMaximumStates(MAX_STATES);
		builder.setMaximumDepth(MAX_DEPTH);
		builder.setMaximumRunTime(MAX_RUNTIME, RUNTIME_UNIT);
		builder.setBrowserConfig(new BrowserConfiguration(BROWSER_TYPE, BROWSER_NUM));
	}
}
