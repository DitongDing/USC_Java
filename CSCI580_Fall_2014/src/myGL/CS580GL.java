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
	public boolean SetDisplayPixel(Display display, int x, int y, short r, short g, short b, short a, float z)
	{
		try
		{
			Pixel pixel = new Pixel(r, g, b, a, z);
			display.setPixel(x, y, pixel);
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

	// Malloc a renderer struct, keep closed until BeginRender inits are done
	// check for legal class Z_BUFFER_RENDER
	public boolean NewRender(Render render, int renderClass, Display display)
	{
		try
		{
			// check for legal. for more class, just add conditions
			if (renderClass != Render.Z_BUFFER_RENDER)
				throw new Exception("render class error in new render");

			render.renderClass = renderClass;
			render.display = display;
			render.open = false;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.NewRender()");
			e.printStackTrace();
		}
		return false;
	}

	// Free all renderer resources
	public boolean FreeRender(Render render)
	{
		try
		{
			render.display = null;
			render.open = false;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.FreeRender()");
			e.printStackTrace();
		}
		return false;
	}

	// Set up for start of each frame
	// TODO CS580GL.BeginRender(): do not understand this function
	public boolean BeginRender(Render render)
	{
		try
		{
			render.open = true;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.BeginRender()");
			e.printStackTrace();
		}
		return false;
	}

	// Set renderer attribute states (e.g.: RGB_COLOR default color)
	public boolean PutAttribute(Render render, int numAttributes, int[] nameList, Object[] valueList)
	{
		try
		{
			if (render.open)
				for (int i = 0; i < numAttributes; i++)
				{
					if (nameList[i] == Render.RGB_COLOR)
					{
						float[] value = (float[]) valueList[i];
						render.flatcolor = new Color(value[Render.R], value[Render.G], value[Render.B]);
					}
					else
						throw new Exception("attr name error in put attr for render");
				}
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.PutAttribute()");
			e.printStackTrace();
		}
		return false;
	}

	// Invoke the scan converter and return an error code
	// TODO CS580GL.PutTriangle(): There is still a little difference between the original pic. Why?
	public boolean DrawTriangle(Render render, int numParts, int[] nameList, Object[] valueList)
	{
		try
		{
			if (render.open)
				for (int i = 0; i < numParts; i++)
				{
					// TODO CS580GL.PutTriangle(): may be changed later. consider to interpolate Z to xOy plane now, no camera position, fov, etc.
					// Interpolate to X:[0, 255], Y:[0,255];
					// Apply LEE, not SLR. Try SLR later
					if (nameList[i] == Render.POSITION)
					{
						// The way to implement:
						// Do not need to arrange vertex. To consider the sign of three judgment.
						// Calculate normal of plane, then calculate Z value with normal, X and Y. These two vector is vertical with each other.
						// It is faster, and comparison shows, the difference is plus-minus 0.00003%, which can be considered as the lose of float calculation.

						float[][] vertexList = (float[][]) valueList[i];

						// check if three points in xOy plane are in the same line
						float[][] vectors = new float[2][3];
						for (int j = 0; j < 2; j++)
							for (int k = 0; k < 3; k++)
								vectors[j][k] = vertexList[j][k] - vertexList[j + 1][k];
						// They are in the same line
						if (vectors[0][0] * vectors[1][1] == vectors[0][1] * vectors[1][0])
							break;

						// Calculate normal vector of plane v1,v2,v3
						float[] normal = new float[3];
						normal[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
						normal[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
						normal[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];

						// calculate ulx, uly, lrx, lry
						float ulx = vertexList[0][Render.X], uly = vertexList[0][Render.Y], lrx = vertexList[0][Render.X], lry = vertexList[0][Render.Y];
						for (int j = 1; j < vertexList.length; j++)
						{
							if (vertexList[j][Render.X] < ulx)
								ulx = vertexList[j][Render.X];
							if (vertexList[j][Render.X] > lrx)
								lrx = vertexList[j][Render.X];
							if (vertexList[j][Render.Y] < uly)
								uly = vertexList[j][Render.Y];
							if (vertexList[j][Render.Y] > lry)
								lry = vertexList[j][Render.Y];
						}

						float[] A = new float[3];
						float[] B = new float[3];
						float[] C = new float[3];
						A[0] = vertexList[1][Render.Y] - vertexList[0][Render.Y];
						A[1] = vertexList[2][Render.Y] - vertexList[1][Render.Y];
						A[2] = vertexList[0][Render.Y] - vertexList[2][Render.Y];
						B[0] = vertexList[0][Render.X] - vertexList[1][Render.X];
						B[1] = vertexList[1][Render.X] - vertexList[2][Render.X];
						B[2] = vertexList[2][Render.X] - vertexList[0][Render.X];
						C[0] = -(A[0] * vertexList[0][Render.X] + B[0] * vertexList[0][Render.Y]);
						C[1] = -(A[1] * vertexList[1][Render.X] + B[1] * vertexList[1][Render.Y]);
						C[2] = -(A[2] * vertexList[2][Render.X] + B[2] * vertexList[2][Render.Y]);

						int x1 = (int) Math.floor(ulx), x2 = (int) Math.ceil(lrx), y1 = (int) Math.floor(uly), y2 = (int) Math.ceil(lry);
						x1 = x1 < 0 ? 0 : (x1 >= render.display.xres ? render.display.xres : x1);
						x2 = x2 < 0 ? 0 : (x2 >= render.display.xres ? render.display.xres : x2);
						y1 = y1 < 0 ? 0 : (y1 >= render.display.yres ? render.display.yres : y1);
						y2 = y2 < 0 ? 0 : (y2 >= render.display.yres ? render.display.yres : y2);
						for (int Y = y1; Y < y2; Y++)
							for (int X = x1; X < x2; X++)
								if (Math.abs(PixelJudge_LEE(A[0], B[0], C[0], X, Y) + PixelJudge_LEE(A[1], B[1], C[1], X, Y)
										+ PixelJudge_LEE(A[2], B[2], C[2], X, Y)) == 3)
								{
									float Z = vertexList[0][2] - ((X - vertexList[0][0]) * normal[0] + (Y - vertexList[0][1]) * normal[1]) / normal[2];
									if (render.display.getPixel(X, Y).z > Z)
										SetDisplayPixel(render.display, X, Y, ctoi(render.flatcolor.red), ctoi(render.flatcolor.green),
												ctoi(render.flatcolor.blue), (short) 1, Z);
								}
					}
					else if (nameList[i] == Render.NULL_TOKEN)
					{
						// Do nothing
					}
					else
						throw new Exception("attr name error in put tri for render");
				}
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.PutTriangle()");
			e.printStackTrace();
		}
		return false;
	}

	private int PixelJudge_LEE(float A, float B, float C, int X, int Y)
	{
		float value = A * X + B * Y + C;
		if (value > 0)
			return 1;
		else if (value == 0)
			return 0;
		return -1;
	}

	// convert float color to GzIntensity short
	private short ctoi(float color)
	{
		return (short) ((int) (color * ((1 << 12) - 1)));
	}
}
