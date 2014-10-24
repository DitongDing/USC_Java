package utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import myGL.CS580GL;
import myGL.Coord;
import myGL.Image;
import myGL.Pixel;

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
		return new Coord(interpolateFloat(start.x, end.x, progress), interpolateFloat(start.y, end.y, progress), interpolateFloat(start.z, end.z, progress),
				interpolateFloat(start.w, end.w, progress));
	}

	public static float interpolateFloat(float start, float end, float progress)
	{
		float left = 1 - progress;
		return start * left + end * progress;
	}

	public static Image readTextureFile(CS580GL method, String filename) throws Exception
	{
		Image result = new Image();
		FileInputStream fis = new FileInputStream(filename);

		BufferedImage bi = ImageIO.read(fis);
		if (bi == null)
		{
			// reset FileInputStream
			fis.getChannel().position(0);
			result = readPPMTextureFile(method, fis);
		}
		else
			result = transformBufferedImage(method, bi);

		fis.close();
		return result;
	}

	public static Image transformBufferedImage(CS580GL method, BufferedImage bufferedImage)
	{
		Image result = new Image();

		// Read w & h
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		method.NewImage(result, width, height);
		// read global max line
		result.setGM((short) 255);
		// fill pixel
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
			{
				java.awt.Color color = new java.awt.Color(bufferedImage.getRGB(x, y));
				Pixel pixel = new Pixel();
				pixel.red = (short) color.getRed();
				pixel.green = (short) color.getGreen();
				pixel.blue = (short) color.getBlue();
				result.setPixel(x, y, pixel);
			}

		return result;
	}

	public static String readLine(DataInputStream in) throws Exception
	{
		StringBuffer sb = new StringBuffer();

		char c = (char) in.readByte();
		while (c != '\n')
		{
			sb.append(c);
			c = (char) in.readByte();
		}

		return sb.toString();
	}

	public static Image readPPMTextureFile(CS580GL method, FileInputStream fis) throws Exception
	{
		Image result = new Image();
		int width = 0, height = 0;
		DataInputStream in = new DataInputStream(new BufferedInputStream(fis));

		try
		{
			// read first line
			String type = readLine(in);
			if (type.startsWith("P6"))
			{
				// Read w & h
				width = Integer.parseInt(readLine(in));
				height = Integer.parseInt(readLine(in));
				method.NewImage(result, width, height);
				// read global max line
				result.setGM(Short.parseShort(readLine(in)));
				// fill pixel
				for (int y = 0; y < height; y++)
					for (int x = 0; x < width; x++)
					{
						Pixel pixel = new Pixel();
						pixel.red = (short) in.readUnsignedByte();
						pixel.green = (short) in.readUnsignedByte();
						pixel.blue = (short) in.readUnsignedByte();
						result.setPixel(x, y, pixel);
					}
			}
			else if (type.startsWith("P3"))
			{
				// Read w & h
				StringTokenizer st = new StringTokenizer(readLine(in));
				width = Integer.parseInt(st.nextToken());
				height = Integer.parseInt(st.nextToken());
				method.NewImage(result, width, height);
				// read global max line
				st = new StringTokenizer(readLine(in));
				result.setGM(Short.parseShort(st.nextToken()));
				// fill pixel
				for (int y = 0; y < height; y++)
				{
					st = new StringTokenizer(readLine(in));
					for (int x = 0; x < width; x++)
					{
						Pixel pixel = new Pixel();
						pixel.red = Short.parseShort(st.nextToken());
						pixel.green = Short.parseShort(st.nextToken());
						pixel.blue = Short.parseShort(st.nextToken());
						result.setPixel(x, y, pixel);
					}
				}
			}
			else
				throw new Exception("not supported format");
		}
		finally
		{
			in.close();
		}

		return result;
	}
}
