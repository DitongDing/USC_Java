package myGL;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class CS580GL
{

	// Allocates memory for the frame buffer, an array of 32-bit RGBA pixels (8-bits per channel) and a 32-bit depth value
	// TODO CS580GL.NewFrameBuffer(): remaining test
	public boolean NewFrameBuffer(FrameBuffer framebuffer, int width, int height)
	{
		try
		{
			framebuffer.fbuf = new Pixel[width * height];
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.NewFrameBuffer()");
			e.printStackTrace();
		}
		return false;
	}

	// Allocates memory for a new Display object
	public boolean NewDisplay(Display display, int dispClass, int xRes, int yRes)
	{
		try
		{
			// if xRes or yRes is smaller than 0, than xres or yres = 0
			// if xRes or y Res is larger than MAXXRES or MAXYRES, than xres = MAXXRES or yres = MAXYRES
			display.dispClass = dispClass;
			xRes = xRes < 0 ? 0 : (xRes > Display.MAXXRES ? Display.MAXXRES : xRes);
			yRes = yRes < 0 ? 0 : (yRes > Display.MAXYRES ? Display.MAXYRES : yRes);
			display.xres = (short) xRes;
			display.yres = (short) yRes;
			display.fbuf = new Pixel[display.xres * display.yres];
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.NewDisplay()");
			e.printStackTrace();
		}
		return false;
	}

	// Frees up the memory allocated to a frame buffer
	// TODO CS580GL.FreeFrameBuffer(): remaining test
	public boolean FreeFrameBuffer(FrameBuffer frameBuffer)
	{
		try
		{
			frameBuffer.fbuf = null;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.FreeFrameBuffer()");
			e.printStackTrace();
		}
		return false;
	}

	// Frees up the memory allocated to a Display object
	public boolean FreeDisplay(Display display)
	{
		try
		{
			display.fbuf = null;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.FreeDisplay()");
			e.printStackTrace();
		}
		return false;
	}

	// Resets a Display object's frame buffer to the desired values
	public boolean ClearDisplay(Display display, Pixel defaultPixel)
	{
		try
		{
			for (int i = 0; i < display.xres; i++)
				for (int j = 0; j < display.yres; j++)
					display.setPixel(i, j, defaultPixel);
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.ClearDisplay()");
			e.printStackTrace();
		}
		return false;
	}

	// Sets the corresponding pixel in a Display object's frame buffer to the desired values
	public boolean SetDisplayPixel(Display display, int i, int j, short r, short g, short b, short a, int z)
	{
		try
		{
			Pixel pixel = new Pixel(r, g, b, a, z);
			display.setPixel(i, j, pixel);
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.SetDisplayPixel()");
			e.printStackTrace();
		}
		return false;
	}

	// Gets the corresponding pixel in a Display object's frame buffer
	public boolean GetDisplayPixel(Display display, int i, int j, Pixel value)
	{
		try
		{
			Pixel pixel = display.getPixel(i, i);
			value.copy(pixel);
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.GetDisplayPixel()");
			e.printStackTrace();
		}
		return false;
	}

	// Writes the pixel values from a Display object's frame buffer to a PPM image file
	// TODO CS580GL.FlushDisplayToPPMFile(): optimize this function
	public boolean FlushDisplayToPPMFile(FileOutputStream outfile, Display display)
	{
		try
		{
			PrintWriter pw = new PrintWriter(outfile);
			pw.println("P3");
			pw.println("" + display.xres + " " + display.yres);
			short global_max = Short.MIN_VALUE;
			StringBuffer output = new StringBuffer("");
			for (int j = 0; j < display.yres; j++)
			{
				for (int i = 0; i < display.xres; i++)
				{
					Pixel pixel = display.getPixel(i, j);
					if (pixel.red > global_max)
						global_max = pixel.red;
					if (pixel.green > global_max)
						global_max = pixel.green;
					if (pixel.blue > global_max)
						global_max = pixel.blue;
					output.append("" + pixel.red + " " + pixel.green + " " + pixel.blue + " ");
				}
				output.deleteCharAt(output.length() - 1);
				output.append("\n");
			}
			output.deleteCharAt(output.length() - 1);
			pw.println(global_max);
			pw.println(output);
			pw.close();
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.FlushDisplayToPPMFile()");
			e.printStackTrace();
		}
		return false;
	}

	// Malloc a renderer struct
	// TODO CS580GL.NewRender(): finish this function
	public boolean NewRender(Render render, int renderClass, Display display)
	{
		return false;
	}

	// Free all renderer resources
	// TODO CS580GL.FreeRender(): finish this function
	public boolean FreeRender(Render render)
	{
		return false;
	}

	// Set up for start of each frame - init frame buffer
	// TODO CS580GL.BeginRender(): finish this function
	public boolean BeginRender(Render render)
	{
		return false;
	}

	// Set renderer attribute states (e.g.: GZ_RGB_COLOR default color)
	// TODO CS580GL.PutAttribute(): finish this function
	public boolean PutAttribute(Render render, int numAttributes, int[] nameList, Object[] valueList)
	{
		return false;
	}

	// Invoke the scan converter and return an error code
	// TODO CS580GL.PutTriangle(): finish this function
	public boolean PutTriangle(Render render, int numParts, int[] nameList, Object[] valueList)
	{
		return false;
	}
}
