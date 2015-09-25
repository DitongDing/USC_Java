package ddt.test.crawljax.plugin;

import java.net.URI;
import java.net.URISyntaxException;

import org.openqa.selenium.ElementNotVisibleException;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.core.plugin.OnBrowserCreatedPlugin;
import com.crawljax.core.state.Eventable;
import com.crawljax.core.state.Identification;

public class LoginPlugin implements OnBrowserCreatedPlugin {
	private static String[][] CREDENTIALS = { { "", "" }, { "X", "X" }, { "admin", "admin" } };
	private static String[] FINAL_CREDENTIALS = { "admin", "admin" };
	private static int USERNAME = 0;
	private static String[] UN_FIELD_NAME = { "Login" };
	private static int PASSWORD = 1;
	private static String[] PW_FIELD_NAME = { "Password" };

	private URI loginURL;

	public LoginPlugin(URI loginURL) {
		this.loginURL = loginURL;
	}

	public LoginPlugin(String loginURL) throws URISyntaxException {
		this(new URI(loginURL));
	}

	public void onBrowserCreated(EmbeddedBrowser newBrowser) {
		try {
			for (String[] credential : CREDENTIALS)
				login(newBrowser, credential);
			logout(newBrowser);
			login(newBrowser, FINAL_CREDENTIALS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void login(EmbeddedBrowser newBrowser, String[] credential)
			throws ElementNotVisibleException, InterruptedException {
		newBrowser.goToUrl(loginURL);
		for (String fieldName : UN_FIELD_NAME)
			newBrowser.input(new Identification(Identification.How.name, fieldName), credential[USERNAME]);
		for (String fieldName : PW_FIELD_NAME)
			newBrowser.input(new Identification(Identification.How.name, fieldName), credential[PASSWORD]);
		newBrowser.fireEventAndWait(new Eventable(
				new Identification(Identification.How.xpath, "//INPUT[@type='submit']"), Eventable.EventType.click));
	}

	private void logout(EmbeddedBrowser newBrowser) throws ElementNotVisibleException, InterruptedException {
		newBrowser.goToUrl(loginURL);
		newBrowser.fireEventAndWait(new Eventable(
				new Identification(Identification.How.xpath, "//INPUT[@type='submit']"), Eventable.EventType.click));
	}
}