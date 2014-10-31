package utils;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import myGL.Display;
import myGL.Pixel;
import myGL.Vertex;
import utils.GUI.MainWindow;
import utils.GUI.ResultWindow;

public class RunRender extends Thread
{
	private MainWindow gui;
	private Display display;
	private Pixel defaultPixel;

	public RunRender(MainWindow gui, Display display, Pixel defaultPixel)
	{
		this.gui = gui;
		this.display = display;
		this.defaultPixel = defaultPixel;
	}

	public void run()
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(gui.outputPath.getText());
			ArrayList<Vertex[]> triList = ComUtils.readASCFile(gui.inputPath.getText());
			gui.render.runRender(triList, display, defaultPixel);

			display.FlushToPPMFile(fos); /* write out or update display to file */
			BufferedImage[] biList = new BufferedImage[1];
			biList[0] = ResultWindow.Display2BufferedImage(display);
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
}
