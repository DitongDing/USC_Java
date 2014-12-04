package utils.final580;

import gl.Camera;
import gl.Color;
import gl.Coord;
import gl.Display;
import gl.Light;
import gl.Pixel;

import javax.swing.JOptionPane;

import utils.GUI.ActionInput;

public class Final_Main
{
	public static int type;
	public static String inputFile;
	public static Coord position;

	public static int defaultFPS = 30;
	public static int cycleLength = 5;

	public static MainWindow gui;
	public static Pixel defaultPixel = new Pixel((short) (255 << 4), (short) (255 << 4), (short) (255 << 4), (short) 1, Float.MAX_VALUE);
	public static boolean status = true;
	public static int width = 512; // frame buffer and display width
	public static int height = 512; // frame buffer and display height
	public static Display display = null;

	public static Coord translation = new Coord(0f, -3.25f, 3.5f, 1f);
	public static Coord scale = new Coord(3.25f, 3.25f, 3.25f, 1.0f);
	public static Coord rotateY = new Coord(0f, 330f, 0f, 0f);
	public static Coord rotateX = new Coord(315f, 0f, 0f, 0f);
	public static Coord lookat = new Coord(0f, 0f, 0f, 1f);
	public static Coord worldup = new Coord(0f, 1.0f, 0.0f, 0);
	public static float FOV = 63.7f;

	public static Light light1 = new Light(new Coord(-0.7071f, 0.7071f, 0, 0), new Color(0.5f, 0.5f, 0.9f));
	public static Light light2 = new Light(new Coord(0, -0.7071f, -0.7071f, 0), new Color(0.9f, 0.2f, 0.3f));
	public static Light light3 = new Light(new Coord(0.7071f, 0, -0.7071f, 0), new Color(0.2f, 0.7f, 0.3f));
	public static Light ambientlight = new Light(new Coord(0, 0, 0, 0), new Color(0.3f, 0.3f, 0.3f));
	public static Color specularCoefficient = new Color(0.3f, 0.3f, 0.3f);
	public static Color ambientCoefficient = new Color(0.1f, 0.1f, 0.1f);
	public static Color diffuseCoefficient = new Color(0.7f, 0.7f, 0.7f);
	public static float specpower = 32;

	public static void main(String[] args) throws Exception
	{
		if (args.length == 0)
			System.exit(-1);
		type = Integer.parseInt(args[0]);
		if (type == 0)
		{
			inputFile = "pot4.asc";
			position = new Coord(0f, 0f, -30f, 1f); // for pot4.asc
		}
		else
		{
			inputFile = "data/Winnie-the-Pooh/Winnie-the-Pooh.obj";
			position = new Coord(0f, 0f, -70f, 1f); // for winnie
		}

		try
		{
			display = new Display(Display.RGBAZ_DISPLAY, width, height);

			// Initialize
			// Do not put it in run render, as it will force render to add default action every time it runs.
			gui = new MainWindow();

			// initialize Xwm and camera
			gui.actionManager.addAction(new ActionInput(ActionInput.CAMERA, ActionInput.WORLD, new Camera(position, lookat, worldup, FOV)));
			gui.actionManager.addAction(new ActionInput(ActionInput.ROTATION_X, ActionInput.WORLD, rotateX));
			gui.actionManager.addAction(new ActionInput(ActionInput.ROTATION_Y, ActionInput.WORLD, rotateY));
			gui.actionManager.addAction(new ActionInput(ActionInput.SCALE, ActionInput.WORLD, scale));
			gui.actionManager.addAction(new ActionInput(ActionInput.TRANSLATION, ActionInput.WORLD, translation));

			// Light
			gui.actionManager.render.PushLight(light1);
			gui.actionManager.render.PushLight(light2);
			gui.actionManager.render.PushLight(light3);
			gui.actionManager.render.setAmbientlight(ambientlight);

			// Material property
			gui.actionManager.render.setKa(ambientCoefficient);
			gui.actionManager.render.setKd(diffuseCoefficient);
			gui.actionManager.render.setKs(specularCoefficient);
			gui.actionManager.render.setSpec(specpower);

			gui.runAnimation = new RunAnimation(gui, display, defaultPixel);
			gui.runAnimation.start();
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(gui, "error", "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}