package ddt.test.bean;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class AccessLogs extends ArrayList<AccessLog> {
	private static final long serialVersionUID = 409970429645560131L;

	public AccessLogs(String logpath) throws Exception {
		super();

		BufferedReader br = new BufferedReader(new FileReader(logpath));

		String line = br.readLine();
		while (line != null) {
			add(new AccessLog(line));
			line = br.readLine();
		}

		br.close();
	}
}
