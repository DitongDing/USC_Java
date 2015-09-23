package ddt.test.crawljax;

import com.crawljax.core.configuration.CrawljaxConfiguration.CrawljaxConfigurationBuilder;
import com.crawljax.core.plugin.Plugin;

public class Plugins {
	// TODO: finish plugins and fill the PLUGIN_LIST.
	private static final String[] PLUGIN_LIST = {};

	public static void addPlugins(CrawljaxConfigurationBuilder builder) {
		try {
			for (String plugin : PLUGIN_LIST)
				builder.addPlugin((Plugin) Class.forName(plugin).newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}