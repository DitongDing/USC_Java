package hw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import myGL.CS580GL;
import myGL.Camera;
import myGL.Coord;
import myGL.Display;
import myGL.Pixel;
import myGL.Render;
import myGL.Vertex;

import utils.ComUtils;
import utils.GUI.MainWindow;
import utils.GUI.ResultWindow;
import utils.GUI.UIInput;

public class Run
{
	public static MainWindow gui;
	public static String defaultInput = "pot4.asc";
	public static String defaultOutput = "output.ppm";
	public static Pixel defaultPixel = new Pixel((short) 1000, (short) 1000, (short) 1000, (short) 1, Float.MAX_VALUE);
	public static boolean status = true;
	public static int hwNumber = 3;
	public static CS580GL method = new CS580GL(hwNumber);

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

	// Write render result to gui.display
	public static void runRender(ArrayList<Vertex[]> triList) throws Exception
	{
		status &= method.ClearDisplay(gui.display, defaultPixel);

		int[] nameListTriangle = new int[3]; // vertex attribute names
		Object[] valueListTriangle = new Object[3]; // vertex attribute pointers
		int[] nameListColor = new int[3]; // color type names
		Object[] valueListColor = new Object[3]; // color type rgb pointers
		float[] color = new float[3];
		float[][] vertexList = new float[3][3]; // vertex position coordinates
		float[][] normalList = new float[3][3]; // vertex normals
		float[][] uvList = new float[3][2]; // vertex texture map indices

		// initialize the display and the renderer

		if (!status)
			throw new Exception("Initialize display error");

		// Tokens associated with triangle vertex values
		nameListTriangle[0] = Render.POSITION; // define vert coordinates only

		// Walk through the list of triangles, set color and pass vert info to render/scan convert each triangle
		// read line by line
		for (Vertex[] tri : triList)
		{
			for (int i = 0; i < 3; i++)
			{
				vertexList[i][0] = tri[i].x;
				vertexList[i][1] = tri[i].y;
				vertexList[i][2] = tri[i].z;
				normalList[i][0] = tri[i].norm.x;
				normalList[i][1] = tri[i].norm.y;
				normalList[i][2] = tri[i].norm.z;
				uvList[i][0] = tri[i].U;
				uvList[i][1] = tri[i].V;
			}

			// Set up shading attributes for each triangle
			ComUtils.shade2(normalList[0], color);// shade based on the norm of vert0
			valueListColor[0] = color;
			nameListColor[0] = Render.RGB_COLOR;
			method.PutAttribute(gui.render, 1, nameListColor, valueListColor);

			// Set the value pointers to the first vertex of the triangle, then feed it to the renderer
			valueListTriangle[0] = vertexList;

			method.DrawTriangle(gui.render, 1, nameListTriangle, valueListTriangle);
		}
	}

	public static void main(String[] args)
	{
		try
		{
			// Do not put it in run render, as it will force render to add default action every time it runs.
			gui = new MainWindow("homework" + hwNumber, hwNumber);

			int m_nWidth = 256; // frame buffer and display width
			int m_nHeight = 256; // frame buffer and display height

			status &= method.NewDisplay(gui.display, Display.RGBAZ_DISPLAY, m_nWidth, m_nHeight);

			status &= method.NewRender(gui.render, Render.Z_BUFFER_RENDER, gui.display);
			status &= method.BeginRender(gui.render);

			Coord translation = new Coord(0f, -3.25f, 3.5f, 1f);
			Coord scale = new Coord(3.25f, 3.25f, 3.25f, 1.0f);
			Coord rotateY = new Coord(0f, 330f, 0f, 0f);
			Coord rotateX = new Coord(315f, 0f, 0f, 0f);
			Camera camera = new Camera(new Coord(13.2f, -8.7f, -14.8f, 1f), new Coord(0.8f, 0.7f, 4.5f, 1f), new Coord(-0.2f, 1.0f, 0.0f, 0), 53.7f);

			UIInput input = new UIInput(UIInput.CAMERA, UIInput.WORLD, camera);
			status &= gui.addAction(method, input);
			input = new UIInput(UIInput.ROTATION_X, UIInput.WORLD, rotateX);
			status &= gui.addAction(method, input);
			input = new UIInput(UIInput.ROTATION_Y, UIInput.WORLD, rotateY);
			status &= gui.addAction(method, input);
			input = new UIInput(UIInput.SCALE, UIInput.WORLD, scale);
			status &= gui.addAction(method, input);
			input = new UIInput(UIInput.TRANSLATION, UIInput.WORLD, translation);
			status &= gui.addAction(method, input);

			if (!status)
				throw new Exception("Initialize error");

			gui.inputPath.setText(defaultInput);
			gui.outputPath.setText(defaultOutput);
			gui.runRender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					new Thread() {
						public void run()
						{
							try
							{
								ArrayList<Vertex[]> triList = readASCFile(gui.inputPath.getText());

								runRender(triList);

								String outFileName = gui.outputPath.getText();
								FileOutputStream fos = new FileOutputStream(outFileName);

								method.FlushDisplayToPPMFile(fos, gui.display); /* write out or update display to file */
								BufferedImage[] biList = new BufferedImage[1];
								biList[0] = ResultWindow.Display2BufferedImage(gui.display);
								new ResultWindow(biList);

								// Clean up and exit
								fos.close();

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
					}.start();
				}
			});
			gui.runAnimation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					new Thread() {
						public void run()
						{
							try
							{
								ArrayList<Vertex[]> triList = readASCFile(gui.inputPath.getText());
								ArrayList<BufferedImage> biList = new ArrayList<BufferedImage>();

								ArrayList<UIInput> actionList = new ArrayList<UIInput>(gui.actionList);

								// Remove all action for using addAction and deleteAction
								for (int i = 0; i < actionList.size(); i++)
									gui.deleteAction(method, 0);

								// Render frames for every action and recover actionlist
								int count = 0;
								for (UIInput action : actionList)
								{
									int frameNum = (int) (action.period * ResultWindow.defaultFPS);

									// Get previous camera
									Camera prevCamera = null;
									ListIterator<UIInput> li = actionList.listIterator(count);
									while (li.hasPrevious())
									{
										UIInput input = li.previous();
										if (input.type == UIInput.CAMERA)
										{
											prevCamera = input.camera;
											break;
										}
									}
									if (prevCamera == null)
										prevCamera = new Camera();

									for (int j = 0; j < frameNum; j++)
									{
										UIInput input = null;
										if (action.type == UIInput.ROTATION_X || action.type == UIInput.ROTATION_Y || action.type == UIInput.ROTATION_Z)
											input = new UIInput(action.type, action.space, (Object) ComUtils.interpolateCoord(new Coord(0, 0, 0, 0),
													action.rotation, j / (float) frameNum));
										else if (action.type == UIInput.TRANSLATION)
											input = new UIInput(action.type, action.space, (Object) ComUtils.interpolateCoord(new Coord(0, 0, 0, 0),
													action.translation, j / (float) frameNum));
										else if (action.type == UIInput.SCALE)
											input = new UIInput(action.type, action.space, (Object) ComUtils.interpolateCoord(new Coord(1, 1, 1, 0),
													action.scale, j / (float) frameNum));
										else if (action.type == UIInput.CAMERA)
										{
											Camera thisCamera = action.camera;
											input = new UIInput(action.type, action.space, (Object) new Camera(ComUtils.interpolateCoord(
													prevCamera.getPosition(), thisCamera.getPosition(), j / (float) frameNum), ComUtils.interpolateCoord(
													prevCamera.getLookat(), thisCamera.getLookat(), j / (float) frameNum), ComUtils.interpolateCoord(
													prevCamera.getWorldup(), thisCamera.getWorldup(), j / (float) frameNum), ComUtils.interpolateFloat(
													prevCamera.getFOV(), thisCamera.getFOV(), j / (float) frameNum)));
										}
										else
											throw new Exception("action type error in run animation");
										gui.addAction(method, input);
										runRender(triList);
										gui.display.calculateGM();
										biList.add(ResultWindow.Display2BufferedImage(gui.display));
										gui.deleteAction(method, count);
									}
									// assume last frame for every action is the previous action, for recovering actionlist.
									gui.addAction(method, action);
									runRender(triList);
									gui.display.calculateGM();
									biList.add(ResultWindow.Display2BufferedImage(gui.display));
									count++;
								}

								// For last frame. In case there is no action
								runRender(triList);
								gui.display.calculateGM();
								biList.add(ResultWindow.Display2BufferedImage(gui.display));

								ResultWindow rw = new ResultWindow(biList.toArray(new BufferedImage[0]));
								for (int i = 1; i < biList.size(); i++)
								{
									sleep(1000 / ResultWindow.defaultFPS);
									rw.repaint();
								}

								// Clean up and exit

								System.out.println("finish!");
							}
							catch (Exception e)
							{
								JOptionPane.showMessageDialog(gui, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
						}
					}.start();

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