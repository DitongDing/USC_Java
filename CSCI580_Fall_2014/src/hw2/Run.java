package hw2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import utils.RunRender;
import utils.GUI.MainWindow;
import utils.GUI.ResultWindow;

import myGL.*;

public class Run
{
	public static MainWindow gui;
	public static ResultWindow rw;
	public static String defaultInput = "pot4.screen.asc";
	public static String defaultOutput = "output.ppm";
	public static int hwNumber = 2;
	public static Pixel defaultPixel = new Pixel((short) 1000, (short) 1000, (short) 1000, (short) 1, Float.MAX_VALUE);
	public static int width = 256; // frame buffer and display width
	public static int height = 256; // frame buffer and display height
	public static Display display = null;

	public static void main(String[] args)
	{
		try
		{
			display = new Display(Display.RGBAZ_DISPLAY, width, height);
			
			gui = new MainWindow("homework" + hwNumber, hwNumber);
			gui.inputPath.setText(defaultInput);
			gui.outputPath.setText(defaultOutput);
			gui.runRender.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					new RunRender(gui, display, defaultPixel).start();
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