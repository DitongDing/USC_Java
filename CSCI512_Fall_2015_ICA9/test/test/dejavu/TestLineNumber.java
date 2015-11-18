package test.dejavu;

import java.io.File;
import java.util.Set;

import ddt.utils.ComUtils;

public class TestLineNumber {
	public static void main(String[] args) throws Exception {
		Set<Integer> lineNumbers = ComUtils.getExecutedLineNumbers(
				new File("./input/CoverageReport/Open default page with 0 weekly featured book(s)_false.xml"), "org.apache.jsp.Default_jsp.AdvMenu_Show()V");
		
		for(Integer ln : lineNumbers)
			System.out.println(ln);
	}
}