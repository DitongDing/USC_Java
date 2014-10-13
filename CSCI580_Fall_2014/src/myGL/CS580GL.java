package myGL;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Stack;

import utils.ComUtils;

public class CS580GL
{
	public int hwNumber;

	public CS580GL(int hwNumber)
	{
		this.hwNumber = hwNumber;
	}

	// HW1 Start
	// Allocates memory for the frame buffer, an array of 32-bit RGBA pixels (8-bits per channel) and a 32-bit depth value
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
			display.changable = false;
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
	public boolean GetDisplayPixel(Display display, int x, int y, Pixel value)
	{
		try
		{
			Pixel pixel = display.getPixel(x, y);
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
			StringBuffer output = new StringBuffer("");
			for (int j = 0; j < display.yres; j++)
			{
				for (int i = 0; i < display.xres; i++)
				{
					Pixel pixel = display.getPixel(i, j);
					if (pixel.red > display.global_max)
						display.global_max = pixel.red;
					if (pixel.green > display.global_max)
						display.global_max = pixel.green;
					if (pixel.blue > display.global_max)
						display.global_max = pixel.blue;
					output.append("" + pixel.red + " " + pixel.green + " " + pixel.blue + " ");
				}
				output.deleteCharAt(output.length() - 1);
				output.append("\n");
			}
			output.deleteCharAt(output.length() - 1);
			pw.println(display.global_max);
			pw.println(output);
			pw.close();
			display.changable = true;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.FlushDisplayToPPMFile()");
			e.printStackTrace();
		}
		return false;
	}

	// HW2 Start
	// Malloc a renderer struct, init camera, init Xsp
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
			render.camera = new Camera();

			float xs2 = render.display.xres / 2.0f;
			float ys2 = render.display.yres / 2.0f;
			float[][] value = { { xs2, 0, 0, xs2 }, { 0, -ys2, 0, ys2 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
			render.Xsp.value = value;

			render.open = true;
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

	// Set up for start of each frame, push Xsp, Xpi, Xiw to stack
	public boolean BeginRender(Render render)
	{
		try
		{
			PushMatrix(render, render.Xsp);
			PushMatrix(render, render.camera.Xpi);
			PushMatrix(render, render.camera.Xiw);
			render.begun = true;
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
			if (render.open && render.begun)
				for (int i = 0; i < numAttributes; i++)
				{
					if (nameList[i] == Render.RGB_COLOR)
					{
						float[] value = (float[]) valueList[i];
						render.flatcolor = new Color(value[Render.R], value[Render.G], value[Render.B]);
					}
					else if (nameList[i] == Render.DIRECTIONAL_LIGHT)
					{
						if (render.numlights != Render.MAX_LIGHTS)
							render.lights[render.numlights++] = (Light) valueList[i];
						else
							throw new Exception("Light stack overflow");
					}
					else if (nameList[i] == Render.AMBIENT_LIGHT)
						render.ambientlight = (Light) valueList[i];
					else if (nameList[i] == Render.DIFFUSE_COEFFICIENT)
						render.Kd = (Color) valueList[i];
					else if (nameList[i] == Render.INTERPOLATE)
						render.interp_mode = (Integer) valueList[i];
					else if (nameList[i] == Render.AMBIENT_COEFFICIENT)
						render.Ka = (Color) valueList[i];
					else if (nameList[i] == Render.SPECULAR_COEFFICIENT)
						render.Ks = (Color) valueList[i];
					else if (nameList[i] == Render.DISTRIBUTION_COEFFICIENT)
						render.spec = (Float) valueList[i];
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
	// Adapted for different homework
	public boolean DrawTriangle(Render render, int numParts, int[] nameList, Object[] valueList)
	{
		try
		{
			if (render.open && render.begun)
			{
				// Interpolate to X:[0, xRes], Y:[0, yRes];
				// Apply LEE, not SLR. Try SLR later
				float[][] vertexPosition = null;
				float[][] vertexNorm = null;
				if (numParts == 1 && nameList[0] == Render.POSITION) // Only vertex position
					vertexPosition = (float[][]) valueList[0];
				else if (numParts == 2 && nameList[0] == Render.POSITION && nameList[1] == Render.NORMAL) // Vertex position and vertex norms
				{
					vertexPosition = (float[][]) valueList[0];
					vertexNorm = (float[][]) valueList[1];
				}
				else
					throw new Exception("Draw Triangle numPart error");

				if (hwNumber >= 3)
				{
					int countOfTooNearVertices = 0;
					float[][] verticalVector = new float[4][1];
					for (int iTri = 0; iTri < vertexPosition.length; iTri++)
					{
						int iValue;
						for (iValue = 0; iValue < verticalVector.length - 1; iValue++)
							verticalVector[iValue][0] = vertexPosition[iTri][iValue];
						verticalVector[iValue][0] = 1;
						// ***** perform Xwm, Xiw, Xpi, Xsp and Homogeneous Coordinate *****
						Matrix matrix = render.MXimage[render.matlevel - 1];
						verticalVector = ComUtils.Multiply(matrix.value, verticalVector);
						// ***** detect off screen vertices *****
						if (verticalVector[verticalVector.length - 1][0] < 1)
							countOfTooNearVertices++;
						// TODO CS580GL.DrawTriangle(): I think we need to consider verticalVector[verticalVector.length - 1][0] may be zero
						verticalVector = ComUtils.Multiply(1 / verticalVector[verticalVector.length - 1][0], verticalVector);
						for (iValue = 0; iValue < verticalVector.length - 1; iValue++)
							vertexPosition[iTri][iValue] = verticalVector[iValue][0];
					}
					if (countOfTooNearVertices == vertexPosition.length)
						return true;
				}

				// The way to implement:
				// Do not need to arrange vertex. To consider the sign of three judgment.
				// Calculate normal of plane, then calculate Z value with normal, X and Y. These two vector is vertical with each other.
				// It is faster, and comparison shows, the difference is plus-minus 0.00003%, which can be considered as the lose of float calculation.
				float[][] vertexList = vertexPosition;

				// check if three points in xOy plane are in the same line
				float[][] vectors = new float[2][3];
				for (int j = 0; j < 2; j++)
					for (int k = 0; k < 2; k++)
						vectors[j][k] = vertexList[j][k] - vertexList[j + 1][k];
				// They are in the same line
				if (vectors[0][0] * vectors[1][1] == vectors[0][1] * vectors[1][0])
					return true;

				float[] VN = new float[3];
				float[] CrN = new float[3];
				float[] CgN = new float[3];
				float[] CbN = new float[3];
				float[] N1N = new float[3];
				float[] N2N = new float[3];
				float[] N3N = new float[3];
				Color[] vertexColor = new Color[3];

				// Calculate normal vector of plane v1,v2,v3 in screen space
				for (int j = 0; j < 2; j++)
					vectors[j][2] = vertexList[j][2] - vertexList[j + 1][2];
				VN[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
				VN[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
				VN[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];

				if (hwNumber >= 4)
				{
					// TODO CS580GL.DrawTriangle(): Some assumptions for calculate color
					// Transform all to image space
					// Interpolate color/norms in screen space (ignore the fact that norms are in model space.)
					// Use constant E vector
					for (int j = 0; j < 3; j++)
						vertexColor[j] = this.calColor(render, vertexNorm[j], new float[] { 0, 0, -1 });
					// Calculate normal vector of plane v1,v2,v3 in XYCr space
					for (int j = 0; j < 2; j++)
						vectors[j][2] = vertexColor[j].red - vertexColor[j + 1].red;
					CrN[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
					CrN[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
					CrN[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];
					// Calculate normal vector of plane v1,v2,v3 in XYCg space
					for (int j = 0; j < 2; j++)
						vectors[j][2] = vertexColor[j].green - vertexColor[j + 1].green;
					CgN[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
					CgN[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
					CgN[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];
					// Calculate normal vector of plane v1,v2,v3 in XYCb space
					for (int j = 0; j < 2; j++)
						vectors[j][2] = vertexColor[j].blue - vertexColor[j + 1].blue;
					CbN[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
					CbN[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
					CbN[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];
					// Calculate normal vector of plane v1,v2,v3 in XYN1 space
					for (int j = 0; j < 2; j++)
						vectors[j][2] = vertexNorm[j][0] - vertexNorm[j + 1][0];
					N1N[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
					N1N[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
					N1N[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];
					// Calculate normal vector of plane v1,v2,v3 in XYN2 space
					for (int j = 0; j < 2; j++)
						vectors[j][2] = vertexNorm[j][1] - vertexNorm[j + 1][1];
					N2N[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
					N2N[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
					N2N[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];
					// Calculate normal vector of plane v1,v2,v3 in XYN3 space
					for (int j = 0; j < 2; j++)
						vectors[j][2] = vertexNorm[j][2] - vertexNorm[j + 1][2];
					N3N[0] = vectors[0][1] * vectors[1][2] - vectors[0][2] * vectors[1][1];
					N3N[1] = vectors[0][2] * vectors[1][0] - vectors[0][0] * vectors[1][2];
					N3N[2] = vectors[0][0] * vectors[1][1] - vectors[0][1] * vectors[1][0];
				}

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
						if (Math.abs(PixelJudge_LEE(A[0], B[0], C[0], X, Y) + PixelJudge_LEE(A[1], B[1], C[1], X, Y) + PixelJudge_LEE(A[2], B[2], C[2], X, Y)) == 3)
						{
							float Z = vertexList[0][2] - ((X - vertexList[0][0]) * VN[0] + (Y - vertexList[0][1]) * VN[1]) / VN[2];
							if (render.display.getPixel(X, Y).z > Z)
							{
								Color color = null;
								if (hwNumber <= 4)
									color = render.flatcolor;
								else
								{
									if (render.interp_mode == Render.FLAT)
										color = vertexColor[0];
									else if (render.interp_mode == Render.COLOR)
									{
										// TODO CS580GL.DrawTriangle(): Haven't test Gouraud shading
										float r = vertexColor[0].red - ((X - vertexList[0][0]) * CrN[0] + (Y - vertexList[0][1]) * CrN[1]) / CrN[2];
										float g = vertexColor[0].green - ((X - vertexList[0][0]) * CgN[0] + (Y - vertexList[0][1]) * CgN[1]) / CgN[2];
										float b = vertexColor[0].blue - ((X - vertexList[0][0]) * CbN[0] + (Y - vertexList[0][1]) * CbN[1]) / CbN[2];
										color = new Color(r, g, b);
									}
									else if (render.interp_mode == Render.NORMALS)
									{
										// TODO CS580GL.DrawTriangle(): Haven't test Phong shading
										float N1 = vertexNorm[0][0] - ((X - vertexList[0][0]) * N1N[0] + (Y - vertexList[0][1]) * N1N[1]) / N1N[2];
										float N2 = vertexNorm[0][1] - ((X - vertexList[0][0]) * N2N[0] + (Y - vertexList[0][1]) * N2N[1]) / N2N[2];
										float N3 = vertexNorm[0][2] - ((X - vertexList[0][0]) * N3N[0] + (Y - vertexList[0][1]) * N3N[1]) / N3N[2];
										color = this.calColor(render, new float[] { N1, N2, N3 }, new float[] { 0, 0, -1 });
									}
									else
										throw new Exception("render's color interpolate mode error");
								}
								SetDisplayPixel(render.display, X, Y, ctoi(color.red), ctoi(color.green), ctoi(color.blue), (short) 1, Z);
							}
						}
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

	// convert float color to short color
	private short ctoi(float color)
	{
		return (short) ((int) (color * ((1 << 12) - 1)));
	}

	// HW3 Start
	// Overwrite renderer camera structure with new camera definition
	public boolean PutCamera(Render render, Camera camera)
	{
		try
		{
			// To ensure that there is at least 3 matrix in stack
			if (render.open && render.begun)
			{
				render.camera = camera;
				Stack<Matrix> temp = new Stack<Matrix>();
				for (; render.matlevel > 3;)
				{
					Matrix matrix = new Matrix();
					PopMatrix(render, matrix);
					temp.push(matrix);
				}

				PopMatrix(render, new Matrix()); // pop Xiw
				PopMatrix(render, new Matrix()); // pop Xpi
				PushMatrix(render, camera.Xpi); // push Xpi
				PushMatrix(render, camera.Xiw); // push Xiw

				while (!temp.isEmpty())
					PushMatrix(render, temp.pop());
			}
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.PutCamera()");
			e.printStackTrace();
		}
		return false;
	}

	// Push a matrix onto the Ximage stack
	// ***** Added by Ditong Ding ***** Push a multipled matrix onto the MXimage stack
	public boolean PushMatrix(Render render, Matrix matrix)
	{
		try
		{
			if (render.matlevel == Render.MATLEVELS)
				throw new Exception("Render matrix (Xsm) stack overflow");
			render.Ximage[render.matlevel] = matrix;
			if (render.matlevel == 0)
				render.MXimage[0] = matrix;
			else
				render.MXimage[render.matlevel].value = ComUtils.Multiply(render.MXimage[render.matlevel - 1].value, matrix.value);
			render.matlevel++;

			// TODO remain untested
			// Ignore Xsp and Xpi, skip translation matrix for Xnorm
			if (render.matlevel > 2 && matrix.value[0][3] == 0 && matrix.value[1][3] == 0 && matrix.value[2][3] == 0)
			{
				if (render.normmatlevel == Render.MATLEVELS)
					throw new Exception("Render matrix (Xnorm) stack overflow");
				// TODO remain untested
				// Actually, do not need to consider unitary matrix for Xnorm, as we can do normalize after Xn.
				// But still need to consider non-uniform scaling.
				Matrix matrix4Xnorm = matrix;
				// For scaling matrix or rotation matrix with θ=90k. in case of non-uniform scaling
				if (matrix4Xnorm.value[0][1] == 0 && matrix4Xnorm.value[0][2] == 0 && matrix4Xnorm.value[1][2] == 0)
				{
					matrix4Xnorm.value[0][0] = 1 / matrix4Xnorm.value[0][0];
					matrix4Xnorm.value[1][1] = 1 / matrix4Xnorm.value[1][1];
					matrix4Xnorm.value[2][2] = 1 / matrix4Xnorm.value[2][2];
				}
				render.Xnorm[render.normmatlevel] = matrix4Xnorm;
				if (render.normmatlevel == 0)
					render.MXnorm[0] = matrix4Xnorm;
				else
					render.MXnorm[render.normmatlevel].value = ComUtils.Multiply(render.MXnorm[render.normmatlevel - 1].value, matrix4Xnorm.value);
				render.normmatlevel++;
			}
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.PushMatrix()");
			e.printStackTrace();
		}
		return false;
	}

	// Pop a matrix off the Ximage stack
	// ***** Edited by Ditong Ding ***** add one paramater in function, get the top element in stack
	public boolean PopMatrix(Render render, Matrix matrix)
	{
		try
		{
			if (render.matlevel == 0)
				throw new Exception("Render matrix (Xsm) stack underflow");
			matrix.value = render.Ximage[--render.matlevel].value;

			// TODO remain untested
			// Ignore Xsp and Xpi, skip translation matrix for Xnorm
			if (render.matlevel >= 2 && matrix.value[0][3] == 0 && matrix.value[1][3] == 0 && matrix.value[2][3] == 0)
			{
				if (render.normmatlevel == 0)
					throw new Exception("Render matrix (Xnorm) stack underflow");
				render.normmatlevel--;
			}
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.PopMatrix()");
			e.printStackTrace();
		}
		return false;
	}

	public boolean CreateRotationByXMatrix(float degree, Matrix matrix)
	{
		try
		{
			float sin = (float) Math.sin(degree * ComUtils.DEGREE_2_RAD);
			float cos = (float) Math.cos(degree * ComUtils.DEGREE_2_RAD);
			float[][] value = { { 1, 0, 0, 0 }, { 0, cos, -sin, 0 }, { 0, sin, cos, 0 }, { 0, 0, 0, 1 } };
			matrix.value = value;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.CreateRotationByXMatrix()");
			e.printStackTrace();
		}
		return false;
	}

	public boolean CreateRotationByYMatrix(float degree, Matrix matrix)
	{
		try
		{
			float sin = (float) Math.sin(degree * ComUtils.DEGREE_2_RAD);
			float cos = (float) Math.cos(degree * ComUtils.DEGREE_2_RAD);
			float[][] value = { { cos, 0, sin, 0 }, { 0, 1, 0, 0 }, { -sin, 0, cos, 0 }, { 0, 0, 0, 1 } };
			matrix.value = value;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.CreateRotationByYMatrix()");
			e.printStackTrace();
		}
		return false;
	}

	public boolean CreateRotationByZMatrix(float degree, Matrix matrix)
	{
		try
		{
			float sin = (float) Math.sin(degree * ComUtils.DEGREE_2_RAD);
			float cos = (float) Math.cos(degree * ComUtils.DEGREE_2_RAD);
			float[][] value = { { cos, -sin, 0, 0 }, { sin, cos, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
			matrix.value = value;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.CreateRotationByZMatrix()");
			e.printStackTrace();
		}
		return false;
	}

	public boolean CreateTranslationMatrix(Coord translate, Matrix matrix)
	{
		try
		{
			float[][] value = { { 1, 0, 0, translate.x }, { 0, 1, 0, translate.y }, { 0, 0, 1, translate.z }, { 0, 0, 0, 1 } };
			matrix.value = value;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.CreateTranslationMatrix()");
			e.printStackTrace();
		}
		return false;
	}

	public boolean CreateScaleMatrix(Coord scale, Matrix matrix)
	{
		try
		{
			float[][] value = { { scale.x, 0, 0, 0 }, { 0, scale.y, 0, 0 }, { 0, 0, scale.z, 0 }, { 0, 0, 0, 1 } };
			matrix.value = value;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in CS580GL.CreateScaleMatrix()");
			e.printStackTrace();
		}
		return false;
	}

	// HW4 start
	// TODO CS580GL.calColor(): Finish calColor. need to convert N from model space to image space
	private Color calColor(Render render, float[] Nm, float[] E) throws Exception
	{
		Color color = new Color(0, 0, 0);

		float[] Ka = render.Ka.getVector();
		float[] la = render.ambientlight.color.getVector();
		float[] Kd = render.Kd.getVector();
		float[][] le = new float[render.numlights][];
		// Convert N to image space
		float[][] NMatrix = ComUtils.Multiply(render.MXnorm[render.normmatlevel - 1].value, new float[][] { { Nm[0] }, { Nm[1] }, { Nm[2] }, { 0 } });
		float[] N = ComUtils.Normalize(new float[] { NMatrix[0][0], NMatrix[1][0], NMatrix[2][0] });
		float[][] L = new float[render.numlights][];
		float[][] R = new float[render.numlights][];
		for (int i = 0; i < L.length; i++)
		{
			le[i] = render.lights[i].color.getVector();
			L[i] = new float[] { render.lights[i].direction.x, render.lights[i].direction.y, render.lights[i].direction.z };
			R[i] = ComUtils.Plus(ComUtils.Multiply(2 * ComUtils.Multiply(N, L[i]), N), ComUtils.Multiply(-1, L[i]));
		}
		float[] Ks = render.Ks.getVector();
		float s = render.spec;

		if (ComUtils.Multiply(N, E) < 0)
			N = ComUtils.Multiply(-1, N);

		float[] tempColor = { 0, 0, 0 };

		float[] Sle = { 0, 0, 0 };
		for (int i = 0; i < render.numlights; i++)
		{
			float RE = ComUtils.Multiply(R[i], E);
			if (RE <= 0)
				continue;
			RE = (float) Math.pow(RE, s);
			Sle = ComUtils.Plus(Sle, ComUtils.Multiply(RE, le[i]));
		}
		for (int i = 0; i < tempColor.length; i++)
			tempColor[i] = Ks[i] * Sle[i];

		if (tempColor[0] < 1 || tempColor[1] < 1 || tempColor[2] < 1)
		{
			float[] Dle = { 0, 0, 0 };
			for (int i = 0; i < render.numlights; i++)
			{
				float NL = ComUtils.Multiply(N, L[i]);
				if (NL < 0)
					continue;
				Dle = ComUtils.Plus(Dle, ComUtils.Multiply(NL, le[i]));
			}
			for (int i = 0; i < tempColor.length; i++)
				tempColor[i] = Kd[i] * Dle[i];
		}

		if (tempColor[0] < 1 || tempColor[1] < 1 || tempColor[2] < 1)
			for (int i = 0; i < tempColor.length; i++)
				tempColor[i] = Ka[i] * la[i];

		color.red = color.red > 1 ? 1 : color.red;
		color.green = color.green > 1 ? 1 : color.green;
		color.blue = color.blue > 1 ? 1 : color.blue;

		return color;
	}
}
