package run;

import gl.Color;
import gl.Coord;
import gl.Display;
import gl.Light;
import gl.Pixel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import utils.RunAnimation;
import utils.RunRender;
import utils.GUI.MainWindow;
import utils.GUI.ActionInput;

public class Main
{
	public static MainWindow gui;
	public static String defaultInput = "data/Winnie-the-Pooh/Winnie-the-Pooh.obj";
	// public static String defaultInput = "pot4.asc";
	public static String defaultOutput = "output.ppm";
	public static Pixel defaultPixel = new Pixel((short) 1000, (short) 1000, (short) 1000, (short) 1, Float.MAX_VALUE);
	public static boolean status = true;
	public static int width = 1024; // frame buffer and display width
	public static int height = 1024; // frame buffer and display height
	public static Display display = null;

	public static Coord translation = new Coord(0f, -3.25f, 3.5f, 1f);
	public static Coord scale = new Coord(0.5f, 0.5f, 0.5f, 1.0f);
	public static Coord rotateY = new Coord(0f, 270f, 0f, 0f);
	public static Coord rotateX = new Coord(345f, 0f, 0f, 0f);

	public static Light light1 = new Light(new Coord(-0.7071f, 0.7071f, 0, 0), new Color(0.5f, 0.5f, 0.9f));
	public static Light light2 = new Light(new Coord(0, -0.7071f, -0.7071f, 0), new Color(0.9f, 0.2f, 0.3f));
	public static Light light3 = new Light(new Coord(0.7071f, 0, -0.7071f, 0), new Color(0.2f, 0.7f, 0.3f));
	public static Light ambientlight = new Light(new Coord(0, 0, 0, 0), new Color(0.3f, 0.3f, 0.3f));
	public static Color specularCoefficient = new Color(0.3f, 0.3f, 0.3f);
	public static Color ambientCoefficient = new Color(0.1f, 0.1f, 0.1f);
	public static Color diffuseCoefficient = new Color(0.7f, 0.7f, 0.7f);
	public static float specpower = 32;

	public static float[][] aaOffset = { { -0.52f, 0.38f, 0.128f }, { 0.41f, 0.56f, 0.119f }, { 0.27f, 0.08f, 0.294f }, { -0.17f, -0.29f, 0.249f },
			{ 0.58f, -0.55f, 0.104f }, { -0.31f, -0.71f, 0.106f } };

	public static void main(String[] args)
	{
		try
		{
			display = new Display(Display.RGBAZ_DISPLAY, width, height);

			// Do not put it in run render, as it will force render to add default action every time it runs.
			gui = new MainWindow("Final_Muse");

			// initialize Xwm and camera
			gui.addAction(new ActionInput(ActionInput.ROTATION_X, ActionInput.WORLD, rotateX));
			gui.addAction(new ActionInput(ActionInput.ROTATION_Y, ActionInput.WORLD, rotateY));
			gui.addAction(new ActionInput(ActionInput.SCALE, ActionInput.WORLD, scale));
			gui.addAction(new ActionInput(ActionInput.TRANSLATION, ActionInput.WORLD, translation));

			// Light
			gui.render.PushLight(light1);
			gui.render.PushLight(light2);
			gui.render.PushLight(light3);
			gui.render.setAmbientlight(ambientlight);

			// Material property
			gui.render.setKa(ambientCoefficient);
			gui.render.setKd(diffuseCoefficient);
			gui.render.setKs(specularCoefficient);
			gui.render.setSpec(specpower);

			// Set aaOffset.
			gui.render.setAaOffset(aaOffset);

			gui.inputPath.setText(defaultInput);
			gui.outputPath.setText(defaultOutput);
			gui.runRender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					new RunRender(gui, display, defaultPixel).start();
				}
			});
			gui.runAnimation.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					new RunAnimation(gui, display, defaultPixel).start();
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