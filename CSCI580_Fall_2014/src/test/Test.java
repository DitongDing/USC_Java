package test;

public class Test
{
	public static void main(String[] args)
	{
		int test = 1;
		Object Test = test;
		test = (Integer) Test;
		System.out.println(test);
	}
}
