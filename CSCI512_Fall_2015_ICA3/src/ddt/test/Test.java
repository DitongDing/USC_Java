package ddt.test;

import ddt.utils.bean.Data;

public class Test {
	public static void main(String[] args) {
		Data set1 = new Data(new String[][] { { "Login", "guest" }, { "Password", "guest" }, { "FormAction", "login" }, { "FormName", "Login" } });
		Data set2 = new Data(new String[][] { { "Login", "guest" }, { "Password", "guest" }, { "FormAction", "login" }, { "FormName", "Login" } });
		Data set3 = new Data(new String[][] { { "Login", "admin" }, { "Password", "guest" }, { "FormAction", "login" }, { "FormName", "Login" } });
		Data set4 = new Data(
				new String[][] { { "Password", "guest" }, { "Login", "guest" }, { "FormAction", "login" }, { "FormName", "Login" }, { "Test", "Test" } });

		System.out.println(set1.equals(set2));
		System.out.println(set1.equals(set3));
		System.out.println(set1.equals(set4));
	}
}