package utils.GUI;

import java.awt.image.BufferedImage;

import javax.swing.*;

public class ResultWindow extends JFrame
{
	private static final long serialVersionUID = 1L;
	public static int defaultFPS = 50; // Param for animation

	public JPanel canvas;

	public ResultWindow(BufferedImage[] biList)
	{
		if (biList == null)
			return;

		canvas = new GraphCanvas(biList);
		add(canvas);

		pack();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
}