package hw1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import utils.GUI.MainWindow;
import utils.GUI.ResultWindow;

import myGL.*;

public class Run
{
	public static MainWindow gui;
	public static ResultWindow rw;
	public static String defaultInput = "rects";
	public static String defaultOutput = "output1.ppm";
	public static int hwNumber = 1;
	public static short width = 512; // frame buffer and display width
	public static short height = 512; // frame buffer and display height
	public static Pixel defaultPixel = new Pixel((short) 0, (short) 0, (short) 0, (short) 1, Float.MAX_VALUE);
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
					try
					{
						String inFileName = gui.inputPath.getText();
						String outFileName = gui.outputPath.getText();
						
						display.Reset(defaultPixel);

						// I/O File open
						BufferedReader br = new BufferedReader(new FileReader(inFileName));
						FileOutputStream fos = new FileOutputStream(outFileName);

						int ulx, uly, lrx, lry;
						short r, g, b;
						String line = br.readLine();
						while (line != null)
						{
							StringTokenizer st = new StringTokenizer(line, " \t");
							if (st.countTokens() != 7)
								break;
							ulx = Integer.parseInt(st.nextToken());
							uly = Integer.parseInt(st.nextToken());
							lrx = Integer.parseInt(st.nextToken());
							lry = Integer.parseInt(st.nextToken());
							r = Short.parseShort(st.nextToken());
							g = Short.parseShort(st.nextToken());
							b = Short.parseShort(st.nextToken());
							for (int j = uly; j <= lry; j++)
								for (int i = ulx; i <= lrx; i++)
									display.setPixel(i, j, new Pixel(r, g, b, (short) 1, 0));
							line = br.readLine();
						}

						display.calculateGM();
						display.FlushToPPMFile(fos); // write out or update display to file
						BufferedImage[] biList = new BufferedImage[1];
						biList[0] = ResultWindow.Display2BufferedImage(display);
						new ResultWindow(biList);

						// Clean up and exit
						br.close();
						fos.close();
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
