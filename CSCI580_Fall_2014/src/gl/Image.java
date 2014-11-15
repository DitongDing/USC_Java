package gl;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class Image
{
	protected short xres; // unsigned
	protected short yres; // unsigned
	private Pixel[][] fbuf; // frame buffer array, fbuf[yres][xres], for pixel in (x, y), it is fbuf[y][x]

	protected short global_max = Short.MIN_VALUE; // ******Added by Ditong Ding******can only be used after FlushToFile

	public Image(int xRes, int yRes)
	{
		xres = (short) xRes;
		yres = (short) yRes;
		fbuf = new Pixel[yres][xres];
	}

	public void setPixel(int x, int y, Pixel pixel)
	{
		x = x < 0 ? 0 : (x >= xres ? xres - 1 : x);
		y = y < 0 ? 0 : (y >= yres ? yres - 1 : y);
		fbuf[y][x] = pixel;
	}

	public Pixel getPixel(int x, int y)
	{
		x = x < 0 ? 0 : (x >= xres ? xres - 1 : x);
		y = y < 0 ? 0 : (y >= yres ? yres - 1 : y);
		return new Pixel(fbuf[y][x]);
	}

	public void Reset(Pixel defaultPixel) throws Exception
	{
		for (int i = 0; i < xres; i++)
			for (int j = 0; j < yres; j++)
				setPixel(i, j, defaultPixel);
		global_max = Short.MIN_VALUE;
	}

	public void FlushToPPMFile(FileOutputStream outfile) throws Exception
	{
		PrintWriter pw = new PrintWriter(outfile);
		pw.println("P3");
		pw.println("" + xres + " " + yres);
		StringBuffer output = new StringBuffer("");
		for (int j = 0; j < yres; j++)
		{
			for (int i = 0; i < xres; i++)
			{
				Pixel pixel = getPixel(i, j);
				output.append("" + pixel.red + " " + pixel.green + " " + pixel.blue + " ");
			}
			output.deleteCharAt(output.length() - 1);
			output.append("\n");
		}
		output.deleteCharAt(output.length() - 1);
		pw.println(global_max);
		pw.println(output);
		pw.close();
	}

	public void setGM(short gm)
	{
		global_max = gm;
	}

	public void calculateGM()
	{
		global_max = Short.MIN_VALUE;
		for (int j = 0; j < yres; j++)
			for (int i = 0; i < xres; i++)
			{
				Pixel pixel = getPixel(i, j);
				if (pixel.red > global_max)
					global_max = pixel.red;
				if (pixel.green > global_max)
					global_max = pixel.green;
				if (pixel.blue > global_max)
					global_max = pixel.blue;
			}
	}

	public short getXres()
	{
		return xres;
	}

	public short getYres()
	{
		return yres;
	}

	public short getGlobal_max()
	{
		return global_max;
	}

	public boolean isChangable()
	{
		return global_max != Short.MIN_VALUE;
	}
}
