package utils;

import myGL.Coord;

public class ComUtils
{
	public static double DEGREE_2_RAD = Math.PI / 180;

	// For HW 1~3
	public static void shade2(float[] norm, float[] color)
	{

		float[] light = new float[3];
		float coef;

		light[0] = 0.707f;
		light[1] = 0.5f;
		light[2] = 0.5f;

		coef = light[0] * norm[0] + light[1] * norm[1] + light[2] * norm[2];
		if (coef < 0)
			coef *= -1;

		if (coef > 1.0)
			coef = 1.0f;
		color[0] = coef * 0.95f;
		color[1] = coef * 0.65f;
		color[2] = coef * 0.88f;
	}

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

	public static Coord interpolateCoord(Coord start, Coord end, float progress)
	{
		float left = 1 - progress;
		return new Coord(start.x * left + end.x * progress, start.y * left + end.y * progress, start.z * left + end.z * progress, start.w * left + end.w
				* progress);
	}
	public static float interpolateFloat(float start, float end, float progress)
	{
		float left = 1 - progress;
		return start * left + end * progress;
	}
}
