package test;

import java.util.regex.*;

public class Test
{
	private static String PATTERN = ".+\\(.+\\)";

	public static void main(String[] args)
	{
		Pattern p = Pattern.compile(Test.PATTERN);
		String input = "function(test,test)=tunction(test1)";
		Matcher m = p.matcher(input);
		while (m.find())
		{
			String g = m.group();
			System.out.println(g);
		}
	}
}