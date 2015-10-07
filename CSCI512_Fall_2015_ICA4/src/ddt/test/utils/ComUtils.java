package ddt.test.utils;

import ddt.test.bean.AccessLog;

public class ComUtils {
	public static final String seperator = "==========";
	public static final String logfile = "log";
	public static final String server = "http://localhost:8080";

	public static String generateWget(AccessLog accessLog, String cookies) {
		if (accessLog == null || cookies == null)
			return "";

		String result = "echo \"" + seperator + "\" >> " + logfile + "\n";

		result += "wget -a " + logfile + " --load-cookies " + cookies + " --save-cookies " + cookies
				+ " --keep-session-cookies";

		if (accessLog.getMethod().equals("POST"))
			result += " --post-data '" + accessLog.getPostData() + "'";
		result += " \"" + server + accessLog.getURL() + "\"";

		return result;
	}
}
