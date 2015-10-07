package ddt.test.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

import ddt.test.bean.AccessLog;
import ddt.test.bean.AccessLogs;
import ddt.test.bean.WgetLog;

public class Oracle {
	public static void generate(String input, String wgetOutput, String expectedResultOutput) throws Exception {
		AccessLogs accessLogs = new AccessLogs(input);
		HashMap<String, String> sessionIDMap = new HashMap<String, String>();
		PrintWriter pwWget = new PrintWriter(wgetOutput);
		PrintWriter pwExpected = new PrintWriter(expectedResultOutput);

		for (AccessLog accessLog : accessLogs) {
			String cookies = sessionIDMap.get(accessLog.getSessionID());
			if (cookies == null) {
				cookies = Integer.toString(sessionIDMap.size());
				sessionIDMap.put(accessLog.getSessionID(), cookies);
			}

			String command = ComUtils.generateWget(accessLog, cookies);
			pwWget.println(command);

			pwExpected.println(accessLog.getExpectedResult());
		}

		pwWget.close();
		pwExpected.close();
	}

	public static void compare(String expected, String actual, String output) throws Exception {
		BufferedReader brE = new BufferedReader(new FileReader(expected));
		BufferedReader brA = new BufferedReader(new FileReader(actual));

		String lineA = brA.readLine();
		String lineE = brE.readLine();
		while (lineA != null && lineE != null) {
			WgetLog log = new WgetLog();

			while (lineA != null && !lineA.startsWith("HTTP request sent"))
				lineA = brA.readLine();
			log.setResponseCode(lineA);

			while (!lineE.startsWith(log.getResponseCode()))
				throw new Exception("not pass");

			// TODO: make sure if only need to check size with code 200.
			// if (log.getResponseCode().equals("200")) {
			// // TODO: change based on AccessLog.getExpectedResult
			// lineE = lineE.substring(lineE.indexOf('\t') + 1);
			// while (lineA != null && !lineA.contains("saved ["))
			// lineA = brA.readLine();
			// log.setResponseSize(lineA);
			// if (!lineE.startsWith(log.getResponseSize()))
			// throw new Exception("not pass");
			// }

			// To next section
			while (lineA != null && !lineA.equals(ComUtils.seperator))
				lineA = brA.readLine();
			lineE = brE.readLine();
		}

		brE.close();
		brA.close();
	}
}
