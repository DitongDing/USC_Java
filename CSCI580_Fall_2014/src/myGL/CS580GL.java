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
	// check for legal class GZ_Z_BUFFER_RENDER
	public boolean NewRender(Render render, int renderClass, Display display)
	{
		try
		{
			// check for legal. for more class, just add conditions
			if (renderClass != Render.GZ_Z_BUFFER_RENDER)
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

	// Set renderer attribute states (e.g.: GZ_RGB_COLOR default color)
	public boolean PutAttribute(Render render, int numAttributes, int[] nameList, Object[] valueList)
	{
		try
		{
			if (render.open)
				for (int i = 0; i < numAttributes; i++)
				{
					if (nameList[i] == Render.GZ_RGB_COLOR)
						render.flatcolor = (float[]) valueList[i];
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
	public boolean PutTriangle(Render render, int numParts, int[] nameList, Object[] valueList)
	{
		try
		{
			if (render.open)
				for (int i = 0; i < numParts; i++)
				{
					// TODO CS580GL.PutTriangle(): may be changed later. consider to interpolate Z to xOy plane now, no camera position, fov, etc.
					// Interpolate to X:[0, 255], Y:[0,255];
					// Apply LEE, not SLR. Try SLR later
					if (nameList[i] == Render.GZ_POSITION)
					{
						float[][] vertexList = (float[][]) valueList[i];
						// check if three points in xOy plane are in the same line
						float[][] vectors = new float[2][2];
						for (int j = 0; j < 2; j++)
							for (int k = 0; k < 2; k++)
								vectors[j][k] = vertexList[j][k] - vertexList[j + 1][k];
						// They are in the same line
						if (vectors[0][0] * vectors[1][1] == vectors[0][1] * vectors[1][0])
							break;
						
						// Assume CCW in x forward right and y forward down is first->second->third->first.
						// the first vertex is the vertex with minimum y value
						// if there are two vertexes with same y value, then mark the one with larger x value as firsts
						int first = 0, second = 0, third = 0;
						float ulx = vertexList[0][Render.X], uly = vertexList[0][Render.Y], lrx = vertexList[0][Render.X], lry = vertexList[0][Render.Y];
						// choose the vertex for first in CCW.
						for (int j = 1; j < vertexList.length; j++)
						{
							if (vertexList[j][Render.Y] < vertexList[first][Render.Y]
									|| (vertexList[j][Render.Y] == vertexList[first][Render.Y] && vertexList[j][Render.X] > vertexList[first][Render.X]))
								first = j;
							if (vertexList[j][Render.X] < ulx)
								ulx = vertexList[j][Render.X];
							if (vertexList[j][Render.X] > lrx)
								lrx = vertexList[j][Render.X];
							if (vertexList[j][Render.Y] < uly)
								uly = vertexList[j][Render.Y];
							if (vertexList[j][Render.Y] > lry)
								lry = vertexList[j][Render.Y];
						}
						// initialize second and third with different value;
						if (first == 0)
							second = 1;
						third = 3 - first - second;
						// judge the right sequence by vector.
						float dx1 = vertexList[second][Render.X] - vertexList[first][Render.X];
						float dy1 = vertexList[second][Render.Y] - vertexList[first][Render.Y];
						float dx2 = vertexList[third][Render.X] - vertexList[first][Render.X];
						float dy2 = vertexList[third][Render.Y] - vertexList[first][Render.Y];
						if (dx1 / Math.sqrt(dx1 * dx1 + dy1 * dy1) > dx2 / Math.sqrt(dx2 * dx2 + dy2 * dy2))
						{
							int j = second;
							second = third;
							third = j;
						}

						float[] A = new float[3];
						float[] B = new float[3];
						float[] C = new float[3];
						A[0] = vertexList[second][Render.Y] - vertexList[first][Render.Y];
						A[1] = vertexList[third][Render.Y] - vertexList[second][Render.Y];
						A[2] = vertexList[first][Render.Y] - vertexList[third][Render.Y];
						B[0] = vertexList[first][Render.X] - vertexList[second][Render.X];
						B[1] = vertexList[second][Render.X] - vertexList[third][Render.X];
						B[2] = vertexList[third][Render.X] - vertexList[first][Render.X];
						C[0] = -(A[0] * vertexList[first][Render.X] + B[0] * vertexList[first][Render.Y]);
						C[1] = -(A[1] * vertexList[second][Render.X] + B[1] * vertexList[second][Render.Y]);
						C[2] = -(A[2] * vertexList[third][Render.X] + B[2] * vertexList[third][Render.Y]);

						int x1 = (int) Math.floor(ulx), x2 = (int) Math.ceil(lrx), y1 = (int) Math.floor(uly), y2 = (int) Math.ceil(lry);
						x1 = x1 < 0 ? 0 : (x1 >= render.display.xres ? render.display.xres : x1);
						x2 = x2 < 0 ? 0 : (x2 >= render.display.xres ? render.display.xres : x2);
						y1 = y1 < 0 ? 0 : (y1 >= render.display.yres ? render.display.yres : y1);
						y2 = y2 < 0 ? 0 : (y2 >= render.display.yres ? render.display.yres : y2);
						for (int Y = y1; Y < y2; Y++)
						{
							float XL, XR, ZL, ZR;
							float alpha;
							if (vertexList[second][Render.Y] >= Y)
							{
								alpha = (Y - vertexList[first][Render.Y]) / A[0];
								XL = alpha * vertexList[second][Render.X] + (1 - alpha) * vertexList[first][Render.X];
								ZL = alpha * vertexList[second][Render.Z] + (1 - alpha) * vertexList[first][Render.Z];
							}
							else
							{
								alpha = (Y - vertexList[second][Render.Y]) / A[1];
								XL = alpha * vertexList[third][Render.X] + (1 - alpha) * vertexList[second][Render.X];
								ZL = alpha * vertexList[third][Render.Z] + (1 - alpha) * vertexList[second][Render.Z];
							}
							if (vertexList[third][Render.Y] >= Y)
							{
								alpha = (Y - vertexList[third][Render.Y]) / A[2];
								XR = alpha * vertexList[first][Render.X] + (1 - alpha) * vertexList[third][Render.X];
								ZR = alpha * vertexList[first][Render.Z] + (1 - alpha) * vertexList[third][Render.Z];
							}
							else
							{
								alpha = (Y - vertexList[second][Render.Y]) / A[1];
								XR = alpha * vertexList[third][Render.X] + (1 - alpha) * vertexList[second][Render.X];
								ZR = alpha * vertexList[third][Render.Z] + (1 - alpha) * vertexList[second][Render.Z];
							}
							for (int X = x1; X < x2; X++)
								if (PixelJudge_LEE(A[0], B[0], C[0], X, Y) && PixelJudge_LEE(A[1], B[1], C[1], X, Y) && PixelJudge_LEE(A[2], B[2], C[2], X, Y))
								{
									alpha = (X - XL) / (XR - XL);
									float Z = alpha * ZR + (1 - alpha) * ZL;
									if (render.display.getPixel(X, Y).z > Z)
										SetDisplayPixel(render.display, X, Y, ctoi(render.flatcolor[Render.R]), ctoi(render.flatcolor[Render.G]),
												ctoi(render.flatcolor[Render.B]), (short) 1, Z);
								}
						}
					}
					else if (nameList[i] == Render.GZ_NULL_TOKEN)
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

	private boolean PixelJudge_LEE(float A, float B, float C, int X, int Y)
	{
		return A * X + B * Y + C > 0;
	}

	// convert float color to GzIntensity short
	private short ctoi(float color)
	{
		return (short) ((int) (color * ((1 << 12) - 1)));
	}
}
