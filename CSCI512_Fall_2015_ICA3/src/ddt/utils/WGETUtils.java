package ddt.utils;

import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import ddt.utils.bean.Component;
import ddt.utils.bean.Data;
import ddt.utils.bean.Interface;
import ddt.utils.bean.Webapp;

public class WGETUtils {
	private static final String ADMIN_COOKIES = "cookies_admin";
	private static final String GUEST_COOKIES = "cookies_guest";
	private static final String[][] GUEST_DATA = { { "Login", "guest" }, { "Password", "guest" }, { "FormAction", "login" }, { "FormName", "Login" } };
	private static final String[][] ADMIN_DATA = { { "Login", "admin" }, { "Password", "admin" }, { "FormAction", "login" }, { "FormName", "Login" } };
	// TODO: Add max count
	// private static final int MAX_COUNT = 5000;

	private String input;
	private String output;
	private String URLBase;
	private Data guestData;
	private Data adminData;
	private Data emptyData;

	public WGETUtils(String input, String output, String URLBase) {
		this.input = input;
		this.output = output;
		this.URLBase = URLBase;

		guestData = new Data(GUEST_DATA);
		adminData = new Data(ADMIN_DATA);
		emptyData = new Data();
	}

	public void parse() throws Exception {
		if (input == null || output == null || URLBase == null)
			throw new Exception("Parameter of WGETUtils.parse error");

		PrintWriter pw = new PrintWriter(output);

		Webapp webapp = XMLUtils.parseWebapp(input);
		// May cause NullPointerException, which will be thrown.
		String loginTarget = webapp.getLoginComponent().getTarget();
		Set<String> commands = new HashSet<String>();

		// For session cookies
		commands.add(generateCommand(loginTarget, "POST", guestData, GUEST_COOKIES));
		commands.add(generateCommand(loginTarget, "POST", adminData, ADMIN_COOKIES));

		// Unlogin wget. guest login wget. admin login wget.
		for (Component component : webapp.getComponents()) {
			commands.add(generateCommand(component.getTarget(), "POST", emptyData, null));
			commands.add(generateCommand(component.getTarget(), "POST", emptyData, GUEST_COOKIES));
			commands.add(generateCommand(component.getTarget(), "POST", emptyData, ADMIN_COOKIES));
		}

		// Others.
		for (Component component : webapp.getComponents())
			for (Interface i : component.getInterfaces())
				for (Data data : i.getDataSet())
					commands.add(generateCommand(i.getTarget(), "POST", data, ADMIN_COOKIES));

		for (String command : commands)
			pw.println(command);
		pw.close();
	}

	// Command types: no cookies, s/l cookies (with session). URL = URLBase/target.
	private String generateCommand(String target, String method, Data data, String cookies) {
		if (target == null || method == null || (!method.equals("POST") && !method.equals("GET")) || data == null)
			return "";

		String URL = getURL(target);
		String result = "wget";
		if (cookies != null)
			result += " --load-cookies " + cookies + " --save-cookies " + cookies + " --keep-session-cookies";

		String dataString = data.toString();
		if (method.equals("POST"))
			result += " --post-data '" + dataString + "' \"" + URL + "\"";
		else if (method.equals("GET"))
			result += " \"" + URL + "?" + dataString + "\"";

		return result;
	}

	private String getURL(String target) {
		return URLBase + "/" + target;
	}
}