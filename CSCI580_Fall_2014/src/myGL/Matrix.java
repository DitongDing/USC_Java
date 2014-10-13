package myGL;

public class Matrix
{
	public float[][] value;

	public Matrix()
	{

	}

	public Matrix(Matrix matrix)
	{
		if (matrix != null && matrix.value != null)
		{
			value = new float[matrix.value.length][];
			for (int i = 0; i < value.length; i++)
				if (matrix.value[i] != null)
				{
					value[i] = new float[matrix.value[i].length];
					for (int j = 0; j < value[i].length; j++)
						value[i][j] = matrix.value[i][j];
				}
		}
	}
}
