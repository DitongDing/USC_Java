package ddt.utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ddt.utils.bean.DataTuple;
import ddt.utils.bean.Webapp;

// TODO: put login wget as the first.
public class WGETUtils {
	private static final String ADMIN_COOKIES = "cookies_admin";
	private static final String GUEST_COOKIES = "cookies_guest";

	public static void parse(String input, String output, String URLBase) throws Exception {
		if (input == null || output == null || URLBase == null)
			throw new Exception("Parameter of WGETUtils.parse error");

		PrintWriter pw = new PrintWriter(output);

		Webapp webapp = XMLUtils.parseWebapp(input);
		List<String> commands = new ArrayList<String>();

		// TODO: for session cookies

		// TODO: unlogin wget. guest login wget. admin login wget.

		// TODO: others.

		for (String command : commands)
			pw.println(command);
		pw.close();
	}

	// Command types: no cookies, s/l cookies (with session). URL = URLBase/target.
	private static String generateCommand(String URL, String method, List<DataTuple> data, String cookies) {
		if (URL == null || method == null || (!method.equals("POST") && !method.equals("GET")) || data == null)
			return "";

		String result = "wget";
		if (cookies != null)
			result += " --load-cookies " + cookies + " --save-cookies " + cookies + " --keep-session-cookies";

		String dataString = getData(data);
		if (method.equals("POST"))
			result += " --post-data '" + dataString + "' " + URL;
		else if (method.equals("GET"))
			result += " " + URL + "?" + dataString;

		return result;
	}

	private static String getData(List<DataTuple> data) {
		String result = "";

		Iterator<DataTuple> iterator = data.iterator();
		if (iterator.hasNext()) {
			result += iterator.next().toString();
			while (iterator.hasNext())
				result += "&" + iterator.next().toString();
		}

		return result;
	}
}