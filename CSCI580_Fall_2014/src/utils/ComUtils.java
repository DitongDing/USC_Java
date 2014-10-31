package utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import myGL.Coord;
import myGL.Image;
import myGL.Pixel;
import myGL.Vertex;

public class ComUtils
{
	// For HW 1~3
	public static float[] shade2(float[] norm)
	{
		float[] color = new float[3];

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

		return color;
	}

	public static Image readTextureFile(String filename) throws Exception
	{
		Image result = null;
		FileInputStream fis = new FileInputStream(filename);

		BufferedImage bi = ImageIO.read(fis);
		if (bi == null)
		{
			// reset FileInputStream
			fis.getChannel().position(0);
			result = readPPMTextureFile(fis);
		}
		else
			result = transformBufferedImage(bi);

		fis.close();
		return result;
	}

	public static Image transformBufferedImage(BufferedImage bufferedImage)
	{
		Image result = null;

		// Read w & h
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		result = new Image(width, height);
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

	public static Image readPPMTextureFile(FileInputStream fis) throws Exception
	{
		Image result = null;
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
				result = new Image(width, height);
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
				result = new Image(width, height);
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

	// Read asc file
	public static ArrayList<Vertex[]> readASCFile(String inputFileName) throws Exception
	{
		ArrayList<Vertex[]> triList = new ArrayList<Vertex[]>();
		BufferedReader br = new BufferedReader(new FileReader(inputFileName));
		String name;
		int count;

		while ((name = br.readLine()) != null && name.startsWith("triangle"))
		{
			Vertex[] tri = new Vertex[3];
			StringTokenizer[] st = new StringTokenizer[3];
			for (count = 0; count < 3; count++)
			{
				String string = br.readLine();
				if (string == null)
					break;
				st[count] = new StringTokenizer(string, " \t");
				if (st[count].countTokens() != 8)
					break;
			}
			// meets end of file
			if (count < 3)
				break;

			for (int i = 0; i < 3; i++)
			{
				float x, y, z, w = 1, nx, ny, nz, u, v;
				x = Float.parseFloat(st[i].nextToken());
				y = Float.parseFloat(st[i].nextToken());
				z = Float.parseFloat(st[i].nextToken());
				nx = Float.parseFloat(st[i].nextToken());
				ny = Float.parseFloat(st[i].nextToken());
				nz = Float.parseFloat(st[i].nextToken());
				u = Float.parseFloat(st[i].nextToken());
				v = Float.parseFloat(st[i].nextToken());
				tri[i] = new Vertex(x, y, z, w, new Coord(nx, ny, nz, 0), u, v);
			}

			triList.add(tri);
		}

		br.close();

		return triList;
	}
}
