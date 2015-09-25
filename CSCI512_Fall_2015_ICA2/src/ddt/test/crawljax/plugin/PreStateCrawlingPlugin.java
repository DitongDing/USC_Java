package ddt.test.crawljax.plugin;

import java.io.PrintWriter;

import com.crawljax.core.CandidateElement;
import com.crawljax.core.CrawlerContext;
import com.crawljax.core.state.StateVertex;
import com.google.common.collect.ImmutableList;

public class PreStateCrawlingPlugin implements com.crawljax.core.plugin.PreStateCrawlingPlugin {
	private PrintWriter pw;

	public PreStateCrawlingPlugin(PrintWriter pw) {
		this.pw = pw;
	}

	public void preStateCrawling(CrawlerContext context, ImmutableList<CandidateElement> candidateElements,
			StateVertex state) {
		pw.println(state.getUrl() + "\t" + candidateElements.size());
	}
}