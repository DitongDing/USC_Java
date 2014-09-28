package hw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import myGL.CS580GL;
import myGL.Camera;
import myGL.Coord;
import myGL.Display;
import myGL.Pixel;
import myGL.Render;
import myGL.UIInput;

import utils.ComUtils;
import utils.MainGUI;
import utils.ResultWindow;

public class Run
{
	public static MainGUI gui;
	public static ResultWindow rw;
	public static String defaultInput = "pot4.asc";
	public static String defaultOutput = "output.ppm";
	public static boolean status = true;
	public static int hwNumber = 3;
	public static CS580GL method = new CS580GL(hwNumber);

	public static void main(String[] args)
	{
		try
		{
			gui = new MainGUI("homework" + hwNumber, hwNumber);
			
			status &= method.NewRender(gui.render, Render.Z_BUFFER_RENDER, gui.display);
			status &= method.BeginRender(gui.render);

			Coord translation = new Coord(0f, -3.25f, 3.5f, 1f);
			Coord scale = new Coord(3.25f, 3.25f, 3.25f, 1.0f);
			Coord rotateY = new Coord(0f, 330f, 0f, 0f);
			Coord rotateX = new Coord(315f, 0f, 0f, 0f);
			Camera camera = new Camera(new Coord(13.2f, -8.7f, -14.8f, 0f), new Coord(0.8f, 0.7f, 4.5f, 0f), new Coord(-0.2f, 1.0f, 0.0f, 0), 53.7f);

			UIInput input = new UIInput(UIInput.TRANSLATION, UIInput.WORLD, translation);
			status &= gui.addAction(method, input);
			input = new UIInput(UIInput.SCALE, UIInput.WORLD, scale);
			status &= gui.addAction(method, input);
			input = new UIInput(UIInput.ROTATION_Y, UIInput.WORLD, rotateY);
			status &= gui.addAction(method, input);
			input = new UIInput(UIInput.ROTATION_X, UIInput.WORLD, rotateX);
			status &= gui.addAction(method, input);

			if (!status)
				throw new Exception("Initialize error");

			gui.inputPath.setText(defaultInput);
			gui.outputPath.setText(defaultOutput);
			gui.runRender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					try
					{
						String inFileName = gui.inputPath.getText();
						String outFileName = gui.outputPath.getText();
						Display m_pDisplay = gui.display;
						Render m_pRender = gui.render;
						Pixel defaultPixel = new Pixel((short) 1000, (short) 1000, (short) 1000, (short) 1, Float.MAX_VALUE);
						
						int m_nWidth = 256; // frame buffer and display width
						int m_nHeight = 256; // frame buffer and display height

						status &= method.NewDisplay(gui.display, Display.RGBAZ_DISPLAY, m_nWidth, m_nHeight);
						status &= method.ClearDisplay(gui.display, defaultPixel);

						int[] nameListTriangle = new int[3]; // vertex attribute names
						Object[] valueListTriangle = new Object[3]; // vertex attribute pointers
						int[] nameListColor = new int[3]; // color type names
						Object[] valueListColor = new Object[3]; // color type rgb pointers
						float[] color = new float[3];
						float[][] vertexList = new float[3][3]; // vertex position coordinates
						float[][] normalList = new float[3][3]; // vertex normals
						float[][] uvList = new float[3][2]; // vertex texture map indices
						String name = null;
						int count;

						// initialize the display and the renderer

						if (!status)
							throw new Exception("Initialize runRender error");

						// Tokens associated with triangle vertex values
						nameListTriangle[0] = Render.POSITION; // define vert coordinates only

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
								st[count] = new StringTokenizer(string, " \t");
								if (st[count].countTokens() != 8)
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
							ComUtils.shade2(normalList[0], color);// shade based on the norm of vert0
							valueListColor[0] = color;
							nameListColor[0] = Render.RGB_COLOR;
							method.PutAttribute(m_pRender, 1, nameListColor, valueListColor);

							// Set the value pointers to the first vertex of the triangle, then feed it to the renderer
							valueListTriangle[0] = vertexList;

							method.DrawTriangle(m_pRender, 1, nameListTriangle, valueListTriangle);
						}

						method.FlushDisplayToPPMFile(fos, m_pDisplay); /* write out or update display to file */
						rw = new ResultWindow(m_pDisplay);

						// Clean up and exit
						br.close();
						fos.close();

						status &= method.FreeRender(m_pRender);
						status &= method.FreeDisplay(m_pDisplay);

						if (!status)
							throw new Exception("Finalize error");

						System.out.println("finish!");
					}
					catch (FileNotFoundException e)
					{
						JOptionPane.showMessageDialog(gui, "I/O error, please check file", "I/O error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
					catch (Exception e)
					{
						JOptionPane.showMessageDialog(gui, "error", "error", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			});
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(gui, "error", "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}