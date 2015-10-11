package ddt.test.bean;

import java.util.StringTokenizer;

public class AccessLog {
	private String sessionID;
	private String method;
	private String action;
	private String postData;
	private String responseCode;
	private String responseSize;

	public AccessLog(String log) {
		StringTokenizer st = new StringTokenizer(log, "|");
		assert(st.countTokens() == 6);

		sessionID = trim(st.nextToken());
		method = trim(st.nextToken());
		action = trim(st.nextToken()).substring(1);
		action = action.substring(action.indexOf('/'));
		postData = trim(st.nextToken());
		responseCode = trim(st.nextToken());
		responseSize = trim(st.nextToken());

		assert(responseCode.equals("404") || responseCode.equals("302") || responseCode.equals("200"));
	}

	// Only file name
	private String getOutputFileName(int count) {
		String result = String.format("%05d", count) + "_";
		result += action.substring(1);
		int index = result.indexOf('?');
		if (index != -1)
			result = result.substring(0, index);
		return result.replace('/', '_');
	}

	public String getWget(String URLBase, String cookies, int count) {
		assert(URLBase.charAt(URLBase.length() - 1) != '/');

		if (URLBase == null || cookies == null)
			return "";

		String result = "";
		result += "wget --load-cookies " + cookies + " --save-cookies " + cookies + " --keep-session-cookies -O "
				+ getOutputFileName(count);

		if (getMethod().equals("POST"))
			result += " --post-data '" + getPostData() + "'";
		result += " \"" + URLBase + getAction() + "\"";

		return result;
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

	public String getAction() {
		return action;
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