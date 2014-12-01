package test;

import java.lang.reflect.*;
import java.util.regex.*;

public class Test
{
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
	{
		Class1 test = new Class1(1, 2);
		Field field = test.getClass().getDeclaredField("b");
		
		System.out.println(field.get(test));
	}
}

class Class1
{
	public int a;
	public int b;
	
	public Class1(int A, int B)
	{
		a = A;
		b = B;
	}
}