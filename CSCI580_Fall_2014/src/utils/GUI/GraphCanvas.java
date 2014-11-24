package utils.GUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

// use repaint() to refresh
public class GraphCanvas extends JPanel
{
	private static final long serialVersionUID = 1161482980454204125L;

	private BufferedImage[] bi = null;
	private int imageIndex = -1;

	public GraphCanvas(BufferedImage[] bi)
	{
		super();
		this.bi = bi;
		setPreferredSize(new Dimension(bi[0].getWidth(), bi[0].getHeight()));
	}

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
}
