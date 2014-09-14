package hw1;

import java.io.*;
import java.util.StringTokenizer;

import myGL.*;

public class Run
{
	public static void main(String[] args)
	{
		try
		{
			String inFileName = "rects";
			String outFileName = "output1.ppm";
			CS580GL method = new CS580GL();
			Display m_pDisplay = new Display();
			boolean status;
			Pixel defaultPixel = new Pixel((short) 0, (short) 0, (short) 0, (short) 1, 0);

			status = true;

			// initialize the display and the renderer
			short m_nWidth = 512; // frame buffer and display width
			short m_nHeight = 512; // frame buffer and display height

			status &= method.NewDisplay(m_pDisplay, Display.GZ_RGBAZ_DISPLAY, m_nWidth, m_nHeight);
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

			// Clean up and exit
			br.close();
			fos.close();

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
}
