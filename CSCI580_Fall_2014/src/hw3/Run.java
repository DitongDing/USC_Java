package hw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import myGL.Camera;
import myGL.Coord;
import myGL.Display;
import myGL.Pixel;

import utils.RunAnimation;
import utils.RunRender;
import utils.GUI.MainWindow;
import utils.GUI.ActionInput;

public class Run
{
	public static MainWindow gui;
	public static String defaultInput = "pot4.asc";
	public static String defaultOutput = "output.ppm";
	public static Pixel defaultPixel = new Pixel((short) 1000, (short) 1000, (short) 1000, (short) 1, Float.MAX_VALUE);
	public static int hwNumber = 3;
	public static int width = 256; // frame buffer and display width
	public static int height = 256; // frame buffer and display height
	public static Display display = null;

	public static Coord translation = new Coord(0f, -3.25f, 3.5f, 1f);
	public static Coord scale = new Coord(3.25f, 3.25f, 3.25f, 1.0f);
	public static Coord rotateY = new Coord(0f, 330f, 0f, 0f);
	public static Coord rotateX = new Coord(315f, 0f, 0f, 0f);
	public static Coord position = new Coord(13.2f, -8.7f, -14.8f, 1f);
	public static Coord lookat = new Coord(0.8f, 0.7f, 4.5f, 1f);
	public static Coord worldup = new Coord(-0.2f, 1.0f, 0.0f, 0);
	public static float FOV = 53.7f;

	public static void main(String[] args)
	{
		try
		{
			display = new Display(Display.RGBAZ_DISPLAY, width, height);

			// Do not put it in run render, as it will force render to add default action every time it runs.
			gui = new MainWindow("homework" + hwNumber, hwNumber);

			// initialize Xwm and camera
			gui.addAction(new ActionInput(ActionInput.CAMERA, ActionInput.WORLD, new Camera(position, lookat, worldup, FOV)));
			gui.addAction(new ActionInput(ActionInput.ROTATION_X, ActionInput.WORLD, rotateX));
			gui.addAction(new ActionInput(ActionInput.ROTATION_Y, ActionInput.WORLD, rotateY));
			gui.addAction(new ActionInput(ActionInput.SCALE, ActionInput.WORLD, scale));
			gui.addAction(new ActionInput(ActionInput.TRANSLATION, ActionInput.WORLD, translation));

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