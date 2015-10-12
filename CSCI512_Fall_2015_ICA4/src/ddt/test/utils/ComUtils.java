package ddt.test.utils;

import java.io.PrintWriter;
import java.util.HashMap;

import ddt.test.bean.AccessLog;
import ddt.test.bean.AccessLogs;

public class ComUtils {
	public static void generate(String input, String wgetOrg, String orgAppname, String wgetTest, String testAppname,
			String URLBase) throws Exception {
		AccessLogs accessLogs = new AccessLogs(input);
		HashMap<String, String> sessionIDMap = new HashMap<String, String>();
		PrintWriter pwWgetOrg = new PrintWriter(wgetOrg);
		PrintWriter pwWgetTest = new PrintWriter(wgetTest);
		int count = 0;

		for (AccessLog accessLog : accessLogs) {
			String cookies = sessionIDMap.get(accessLog.getSessionID());
			if (cookies == null) {
				cookies = Integer.toString(sessionIDMap.size());
				sessionIDMap.put(accessLog.getSessionID(), cookies);
			}

			String URL = URLBase + "/" + orgAppname;
			String command = accessLog.getWget(URL, cookies, count);
			pwWgetOrg.println(command);
			pwWgetTest.println(command.replaceAll(orgAppname, testAppname));

			count++;
		}

		pwWgetOrg.close();
		pwWgetTest.close();
	}
}
