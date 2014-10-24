package test;

import java.util.Random;

public class Test
{
	public static void main(String[] args)
	{
		boolean[] test = new boolean[7];
		for (int i = 0; i < test.length; i++)
			test[i] = false;
		for (int i = 0; i < test.length; i++)
		{
			int r = new Random().nextInt(test.length - i);
			for (int j = 0; j < r + 1; j++)
				if (test[j])
					r++;
			test[r] = true;
			System.out.println(r + 1);
		}
	}
}