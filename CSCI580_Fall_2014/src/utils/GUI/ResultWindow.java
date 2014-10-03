package utils.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import myGL.Display;
import myGL.Pixel;

public class ResultWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static int defaultFPS = 30; // Param for animation

	private BufferedImage[] bi = null;
	private int imageIndex = -1;
	public JPanel canvas;

	public static BufferedImage Display2BufferedImage(Display display)
	{
		if(display.changable)
		{
			BufferedImage bi = new BufferedImage(display.xres, display.yres, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < bi.getWidth(); i++)
				for (int j = 0; j < bi.getHeight(); j++)
				{
					Pixel pixel = display.getPixel(i, j);
					bi.setRGB(i, j, new Color(pixel.red / (float) display.global_max, pixel.green / (float) display.global_max, pixel.blue
							/ (float) display.global_max).getRGB());
				}
			return bi;
		}
		else
		{
			System.out.println("Display global_max hasn't been initialized");
			return null;
		}
	}

	public ResultWindow(BufferedImage[] biList)
	{
		if (biList == null)
			return;
		this.bi = biList;

		canvas = new JPanel() {
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				if (imageIndex < 0)
					imageIndex++;
				else if (imageIndex < bi.length)
					g.drawImage(bi[imageIndex++], 0, 0, null);
				else
					g.drawImage(bi[bi.length - 1], 0, 0, null);
			}
		};
		canvas.setPreferredSize(new Dimension(bi[0].getWidth(), bi[0].getHeight()));
		add(canvas);

		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}