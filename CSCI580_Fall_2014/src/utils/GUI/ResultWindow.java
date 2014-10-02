package utils.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import myGL.Display;
import myGL.Pixel;

public class ResultWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static int defaultFPS = 20; // Param for animation

	Display display;
	BufferedImage bi;

	public ResultWindow(Display display)
	{
		if (display == null)
			return;
		this.display = display;
		bi = new BufferedImage(display.xres, display.yres, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < bi.getWidth(); i++)
			for (int j = 0; j < bi.getHeight(); j++)
			{
				Pixel pixel = display.getPixel(i, j);
				bi.setRGB(i, j, new Color(pixel.red / (float) display.global_max, pixel.green / (float) display.global_max, pixel.blue
						/ (float) display.global_max).getRGB());
			}

		JPanel panel = new JPanel() {
			private static final long serialVersionUID = 1L;
			public void paintComponent(Graphics g)
			{
				super.paintComponent(g);
				g.drawImage(bi, 0, 0, null);
			}
		};
		panel.setPreferredSize(new Dimension(display.xres, display.yres));
		add(panel);

		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}