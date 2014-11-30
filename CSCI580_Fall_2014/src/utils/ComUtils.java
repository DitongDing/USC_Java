package utils;

import gl.Coord;
import gl.Image;
import gl.Pixel;
import gl.Vertex;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

public class ComUtils
{
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

	public static ArrayList<Vertex[]> readModelFile(String inputFileName) throws Exception
	{
		ArrayList<Vertex[]> triList = new ArrayList<Vertex[]>();
		triList = readASCFile(inputFileName);
		if (triList.size() == 0)
			triList = readOBJFile(inputFileName);
		return triList;
	}

	// TODO Improve readOBJFile
	public static ArrayList<Vertex[]> readOBJFile(String inputFileName) throws Exception
	{
		ArrayList<Vertex[]> triList = new ArrayList<Vertex[]>();

		com.obj.WavefrontObject obj = new com.obj.WavefrontObject(inputFileName);

		for (com.obj.Group group : obj.getGroups())
			for (com.obj.Face face : group.getFaces())
			{
				Vertex[] tri = new Vertex[3];
				com.obj.Vertex[] vertexes = face.getVertices();
				com.obj.Vertex[] normals = face.getNormals();
				com.obj.TextureCoordinate[] textures = face.getTextures();
				if (vertexes.length > 3)
					System.out.println();
				for (int i = 0; i < tri.length; i++)
				{

					float x, y, z, w = 1, nx, ny, nz, u, v;
					x = vertexes[i].getX();
					y = vertexes[i].getY();
					z = vertexes[i].getZ();
					nx = normals[i].getX();
					ny = normals[i].getY();
					nz = normals[i].getZ();
					u = textures[i].getU();
					v = textures[i].getV();
					tri[i] = new Vertex(x, y, z, w, new Coord(nx, ny, nz, 0), u, v);
				}
				triList.add(tri);
			}

		return triList;
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

	public static Image edgeDetector(Image image, float ZMax)
	{
		Image re = new Image(image.getXres(), image.getYres());

		double threshold = 0.25;

		for (int x = 0; x < re.getXres(); x++)
			for (int y = 0; y < re.getYres(); y++)
			{
				re.setPixel(x, y, new Pixel(image.getPixel(x, y)));
				re.getPixel(x, y).z = 1 / (1 / re.getPixel(x, y).z - 1 / ZMax);
			}

		float min = Float.MAX_VALUE;
		float max = -1;

		for (int x = 0; x < re.getXres(); x++)
			for (int y = 0; y < re.getYres(); y++)
			{
				if (re.getPixel(x, y).z != Float.MAX_VALUE && re.getPixel(x, y).z > max)
					max = re.getPixel(x, y).z;
				else if (re.getPixel(x, y).z < min)
					min = re.getPixel(x, y).z;
			}

		Pixel black = new Pixel((short) 0, (short) 0, (short) 0, (short) 1, 0);

		for (int x = 1; x < re.getXres() - 1; x++)
			for (int y = 1; y < re.getYres() - 1; y++)
			{
				double Gx = ((image.getPixel(x + 1, y - 1).z + 2 * image.getPixel(x + 1, y).z + image.getPixel(x + 1, y + 1).z) - (image.getPixel(x - 1, y - 1).z
						+ 2 * image.getPixel(x - 1, y).z + image.getPixel(x - 1, y + 1).z));
				double Gy = ((image.getPixel(x - 1, y - 1).z + 2 * image.getPixel(x, y - 1).z + image.getPixel(x + 1, y - 1).z) - (image.getPixel(x - 1, y + 1).z
						+ 2 * image.getPixel(x, y + 1).z + image.getPixel(x + 1, y + 1).z));
				double G = Math.sqrt(Gx * Gx + Gy * Gy);
				if (G > threshold * (max - min))
					re.setPixel(x, y, black);
			}

		return re;
	}

	public static BufferedImage Display2BufferedImage(Image image)
	{
		if (image.isChangable())
		{
			BufferedImage bi = new BufferedImage(image.getXres(), image.getYres(), BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < bi.getWidth(); i++)
				for (int j = 0; j < bi.getHeight(); j++)
				{
					Pixel pixel = image.getPixel(i, j);
					bi.setRGB(i, j, new java.awt.Color(pixel.red / (float) image.getGlobal_max(), pixel.green / (float) image.getGlobal_max(), pixel.blue
							/ (float) image.getGlobal_max()).getRGB());
				}
			return bi;
		}
		else
		{
			System.out.println("Display global_max hasn't been initialized");
			return null;
		}
	}

	public static Image stippling(Image image, boolean isColorStippling)
	{
		Image re = new Image(image.getXres(), image.getYres());
		int size = re.getXres() * re.getYres();

		for (int x = 0; x < re.getXres(); x++)
			for (int y = 0; y < re.getYres(); y++)
				re.setPixel(x, y, new Pixel(image.getPixel(x, y)));

		if (isColorStippling)
		{
			for (int index = 0; index < size; index += 4)
			{
//				stipBL(index, image, re);
//				stipG(index, image, re);
//				stipR(index, image, re);
				if (index % re.getXres() == re.getXres() - 4)
				{
					index += (3 * re.getXres());
				}
			}
		}
		else
		{
			for (int index = 0; index < size; index += 4)
			{
				if (index % re.getXres() == re.getXres() - 4)
				{
					index += (3 * re.getXres());
				}
			}
		}

		return re;
	}
}
