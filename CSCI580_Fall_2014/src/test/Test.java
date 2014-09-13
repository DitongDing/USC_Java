package test;

public class Test
{
	public static void main(String[] args)
	{
		Matrix m = new Matrix();
		System.out.println(m.value.length);
	}

}

class Matrix
{
	public float[][] value = new float[4][4];
}