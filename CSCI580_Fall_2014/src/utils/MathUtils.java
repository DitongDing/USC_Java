package utils;

public class MathUtils
{
	public static double DEGREE_2_RAD = Math.PI / 180;

	// Do matrix plus, without check
	public static float[][] Plus(float[][] matrix1, float[][] matrix2) throws Exception
	{
		float[][] re = new float[matrix1.length][matrix1[0].length];
		for (int i = 0; i < re.length; i++)
			for (int j = 0; j < re[i].length; j++)
				re[i][j] = matrix1[i][j] + matrix2[i][j];
		return re;
	}

	// Do matrix multiply, without check
	public static float[][] Multiply(float[][] matrix1, float[][] matrix2) throws Exception
	{
		float[][] re = new float[matrix1.length][matrix2[0].length];
		for (int i = 0; i < re.length; i++)
			for (int j = 0; j < re[i].length; j++)
			{
				float temp = 0;
				for (int k = 0; k < matrix1[0].length; k++)
					temp += matrix1[i][k] * matrix2[k][j];
				re[i][j] = temp;
			}
		return re;
	}

	// Do constant matrix multiply, without check
	public static float[][] Multiply(float constant, float[][] matrix) throws Exception
	{
		float[][] re = new float[matrix.length][matrix[0].length];
		for (int i = 0; i < re.length; i++)
			for (int j = 0; j < re[i].length; j++)
				re[i][j] = matrix[i][j] * constant;
		return re;
	}

	// Do matrix Transpose, without check
	public static float[][] Transpose(float[][] matrix) throws Exception
	{
		float[][] re = new float[matrix[0].length][matrix.length];
		for (int i = 0; i < re.length; i++)
			for (int j = 0; j < re[i].length; j++)
				re[i][j] = matrix[j][i];
		return re;
	}

	// Do vector multiply, without check
	public static float Multiply(float[] vector1, float[] vector2) throws Exception
	{
		float re = 0;
		for (int i = 0; i < vector1.length; i++)
			re += vector1[i] * vector2[i];
		return re;
	}

	// Do constant vector multiply, without check
	public static float[] Multiply(float constant, float[] vector) throws Exception
	{
		float[] re = new float[vector.length];
		for (int i = 0; i < vector.length; i++)
			re[i] = constant * vector[i];
		return re;
	}

	// Do vector plus, without check
	public static float[] Plus(float[] vector1, float[] vector2) throws Exception
	{
		float[] re = new float[vector1.length];
		for (int i = 0; i < vector1.length; i++)
			re[i] = vector1[i] + vector2[i];
		return re;
	}

	// Do vector normalize, without check
	public static float[] Normalize(float[] vector) throws Exception
	{
		float[] re = new float[vector.length];
		double length = 0;
		for (float value : vector)
			length += value * value;
		length = Math.sqrt(length);
		for (int i = 0; i < re.length; i++)
			re[i] = (float) (vector[i] / length);
		return re;
	}

	// Do vector cross product, with check (only for 3D vectors)
	public static float[] CrossProduct(float[] vector1, float[] vector2) throws Exception
	{
		if (vector1 == null || vector2 == null || vector1.length != 3 || vector2.length != 3)
			throw new Exception("Cross product vector error");
		float[] re = new float[3];
		re[0] = vector1[1] * vector2[2] - vector1[2] * vector2[1];
		re[1] = vector1[2] * vector2[0] - vector1[0] * vector2[2];
		re[2] = vector1[0] * vector2[1] - vector1[1] * vector2[0];
		return re;
	}

	// Do interpolate, with check. value in point is X, Y, V
	public static float InterpolateValueByXYAndNorm(float X, float Y, float[] point, float[] norm) throws Exception
	{
		if (point == null || norm == null || point.length != 3 || norm.length != 3)
			throw new Exception("Cross product vector error");
		return point[2] - ((X - point[0]) * norm[0] + (Y - point[1]) * norm[1]) / norm[2];
	}

	public static float interpolateFloat(float start, float end, float progress)
	{
		float left = 1 - progress;
		return start * left + end * progress;
	}

	public static int PixelJudge_LEE(float A, float B, float C, int X, int Y)
	{
		float value = A * X + B * Y + C;
		if (value > 0)
			return 1;
		else if (value == 0)
			return 0;
		return -1;
	}

	// convert float color to short color
	public static short ctoi(float color)
	{
		return (short) ((int) (color * ((1 << 12) - 1)));
	}
}
