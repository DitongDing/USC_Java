package ddt.test.bean;

public class WgetLog {
	private String responseCode;
	private String responseSize;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String line) {
		responseCode = line.substring("HTTP request sent, awaiting response... ".length());
		responseCode = responseCode.substring(0, responseCode.indexOf(' '));
	}

	public String getResponseSize() {
		return responseSize;
	}

	public void setResponseSize(String line) {
		responseSize = line.substring(line.indexOf('['));
		responseSize = responseSize.substring(1, responseSize.indexOf(']'));
	}
}
