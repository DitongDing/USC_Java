package hw2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.StringTokenizer;

import myGL.*;

public class Run
{
	public static String inFileName = "pot4.screen.asc";
	public static String outFileName = "output.ppm";

	public static void main(String[] args)
	{
		try
		{
			CS580GL method = new CS580GL();
			Display m_pDisplay = new Display();
			Render m_pRender = new Render();
			Pixel defaultPixel = new Pixel((short) 0, (short) 0, (short) 0, (short) 1, 0);

			int[] nameListTriangle = new int[3]; // vertex attribute names
			Object[] valueListTriangle = new Object[3]; // vertex attribute pointers
			int[] nameListColor = new int[3]; // color type names
			Object[] valueListColor = new Object[3]; // color type rgb pointers
			float[] color = new float[3];
			float[][] vertexList = new float[3][3]; // vertex position coordinates
			float[][] normalList = new float[3][3]; // vertex normals
			float[][] uvList = new float[3][2]; // vertex texture map indices
			boolean status;
			String name = null;
			int count;

			status = true;

			// initialize the display and the renderer
			int m_nWidth = 256; // frame buffer and display width
			int m_nHeight = 256; // frame buffer and display height

			status &= method.NewDisplay(m_pDisplay, Display.GZ_RGBAZ_DISPLAY, m_nWidth, m_nHeight);
			status &= method.ClearDisplay(m_pDisplay, defaultPixel);
			status &= method.NewRender(m_pRender, Render.GZ_Z_BUFFER_RENDER, m_pDisplay);
			status &= method.BeginRender(m_pRender);

			if (status)
				System.exit(-1);

			// Tokens associated with triangle vertex values
			nameListTriangle[0] = Render.GZ_POSITION; // define vert coordinates only

			// I/O File open
			BufferedReader br = new BufferedReader(new FileReader(inFileName));
			FileOutputStream fos = new FileOutputStream(outFileName);

			// Walk through the list of triangles, set color and pass vert info to render/scan convert each triangle
			// read line by line
			while ((name = br.readLine()) != null && name.startsWith("triangle"))
			{
				StringTokenizer[] st = new StringTokenizer[3];
				for (count = 0; count < 3; count++)
				{
					String string = br.readLine();
					if (string == null)
						break;
					st[count] = new StringTokenizer(string, " ");
					if (st[count].countTokens() != 7)
						break;
				}
				// meets end of file
				if (count < 3)
					break;

				for (int i = 0; i < 3; i++)
				{
					for (int j = 0; j < 3; j++)
						vertexList[i][j] = Float.parseFloat(st[i].nextToken());
					for (int j = 0; j < 3; j++)
						normalList[i][j] = Float.parseFloat(st[i].nextToken());
					for (int j = 0; j < 2; j++)
						uvList[i][j] = Float.parseFloat(st[i].nextToken());
				}

				// Set up shading attributes for each triangle
				shade2(normalList[0], color);// shade based on the norm of vert0
				valueListColor[0] = color;
				nameListColor[0] = Render.GZ_RGB_COLOR;
				method.PutAttribute(m_pRender, 1, nameListColor, valueListColor);

				// Set the value pointers to the first vertex of the triangle, then feed it to the renderer
				valueListTriangle[0] = vertexList;

				method.PutTriangle(m_pRender, 1, nameListTriangle, valueListTriangle);
			}

			method.FlushDisplayToPPMFile(fos, m_pDisplay); /* write out or update display to file */

			// Clean up and exit
			br.close();
			fos.close();

			status &= method.FreeRender(m_pRender);
			status &= method.FreeDisplay(m_pDisplay);

			if (!status)
				System.exit(-1);

			System.out.println("finish!");
		}
		catch (FileNotFoundException e)
		{
			System.out.println("please check input file \"rects\"");
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

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
}