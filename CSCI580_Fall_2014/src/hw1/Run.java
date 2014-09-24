package hw1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import utils.MainGUI;
import utils.ResultWindow;

import myGL.*;

public class Run
{
	public static MainGUI gui;
	public static ResultWindow rw;
	public static String defaultInput = "rects";
	public static String defaultOutput = "output1.ppm";

	public static void main(String[] args)
	{
		gui = new MainGUI("homework1", 1);
		gui.inputPath.setText(defaultInput);
		gui.outputPath.setText(defaultOutput);
		gui.runRender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					String inFileName = gui.inputPath.getText();
					String outFileName = gui.outputPath.getText();
					CS580GL method = new CS580GL();
					Display m_pDisplay = new Display();
					boolean status;
					Pixel defaultPixel = new Pixel((short) 0, (short) 0, (short) 0, (short) 1, Float.MAX_VALUE);

					status = true;

					// initialize the display and the renderer
					short m_nWidth = 512; // frame buffer and display width
					short m_nHeight = 512; // frame buffer and display height

					status &= method.NewDisplay(m_pDisplay, Display.RGBAZ_DISPLAY, m_nWidth, m_nHeight);
					status &= method.ClearDisplay(m_pDisplay, defaultPixel); // init for new frame

					if (!status)
						System.exit(-1);

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
								method.SetDisplayPixel(m_pDisplay, i, j, r, g, b, (short) 1, 0);
						line = br.readLine();
					}

					method.FlushDisplayToPPMFile(fos, m_pDisplay); // write out or update display to file
					rw = new ResultWindow(m_pDisplay);

					// Clean up and exit
					br.close();
					fos.close();

					status &= method.FreeDisplay(m_pDisplay);

					if (!status)
						System.exit(-1);
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
}
