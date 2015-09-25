package ddt.test.crawljax.condition;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.condition.Condition;

public class SortedURLCondition implements Condition {
	// Only two kind of page can be crawled: 1) initial page, 2) page sorted with first column (first time: Sorted=null,
	// Sorting=1)
	public boolean check(EmbeddedBrowser browser) {
		boolean result = false;
		try {
			// For sorting page
			URL URL = new URL(browser.getCurrentUrl());
			String query = URL.getQuery();
			if (query == null || query.length() == 0 || !query.contains("Sorted"))
				result = true;
			else {
				StringTokenizer st = new StringTokenizer(query, "&");
				boolean firstTime = true;
				while (st.hasMoreTokens() && firstTime) {
					String pair = st.nextToken();
					if (pair.contains("Sorted"))
						firstTime = pair.substring(pair.lastIndexOf('=') + 1).equals("");
					else if (pair.contains("Sorting"))
						firstTime = pair.substring(pair.lastIndexOf('=') + 1).equals("1");
				}
				result = firstTime;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result;
	}
}