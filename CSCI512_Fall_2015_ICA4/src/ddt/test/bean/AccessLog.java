package ddt.test.bean;

import java.util.StringTokenizer;

public class AccessLog {
	private String sessionID;
	private String method;
	private String URL;
	private String postData;
	private String responseCode;
	private String responseSize;

	public AccessLog(String log) {
		StringTokenizer st = new StringTokenizer(log, "|");
		assert(st.countTokens() == 6);

		sessionID = trim(st.nextToken());
		method = trim(st.nextToken());
		URL = trim(st.nextToken());
		postData = trim(st.nextToken());
		responseCode = trim(st.nextToken());
		responseSize = trim(st.nextToken());

		assert(responseCode.equals("404") || responseCode.equals("302") || responseCode.equals("200"));
	}

	public String getExpectedResult() {
		return responseCode + "\t" + responseSize;
	}

	private String trim(String s) {
		return s.substring(1, s.length() - 1);
	}

	@SuppressWarnings("unused")
	private boolean isValid() {
		return !responseCode.equals("302");
	}

	public String getSessionID() {
		return sessionID;
	}

	public String getMethod() {
		return method;
	}

	public String getURL() {
		return URL;
	}

	public String getPostData() {
		return postData;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public String getResponseSize() {
		return responseSize;
	}
}