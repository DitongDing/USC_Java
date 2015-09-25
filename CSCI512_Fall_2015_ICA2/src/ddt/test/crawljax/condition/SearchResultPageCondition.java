package ddt.test.crawljax.condition;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import com.crawljax.browser.EmbeddedBrowser;
import com.crawljax.condition.Condition;

// Return true if it's not search result or it's the first time for getting search result.
public class SearchResultPageCondition implements Condition {
	private HashSet<String> searchResultPage;

	public SearchResultPageCondition() {
		searchResultPage = new HashSet<String>();
	}

	public boolean check(EmbeddedBrowser browser) {
		boolean result = true;

		try {
			URL URL = new URL(browser.getCurrentUrl());
			if (isSearchResult(URL.getQuery())) {
				if (searchResultPage.contains(URL.getPath()))
					result = false;
				else
					searchResultPage.add(URL.getPath());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return result;
	}

	private boolean isSearchResult(String query) {
		boolean result = false;

		if (contain(query, '=') == 3 && contain(query, '&') == 2) {
			boolean emp_login = query.contains("emp_login");
			boolean name = query.contains("name");
			boolean manmonth = query.contains("manmonth");
			boolean dep_id = query.contains("dep_id");
			boolean email = query.contains("email");

			// if query only contains (emp_login+name+manmonth)|(dep_id+name+email), then it's a search.
			if ((emp_login && name && manmonth) || (dep_id && name && email))
				result = true;
		}

		return result;
	}

	private int contain(String s, char c) {
		int result = 0;
		if (s != null && s.length() != 0)
			for (char ctmp : s.toCharArray())
				if (ctmp == c)
					result++;
		return result;
	}
}