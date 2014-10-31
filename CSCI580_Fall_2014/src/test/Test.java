package test;

public class Test
{
	public static void main(String[] args)
	{
		float w = 0;
		float[][] test = new float[0][];
		for (float[] value : test)
			w += value[2];
		System.out.println(w);
	}
}