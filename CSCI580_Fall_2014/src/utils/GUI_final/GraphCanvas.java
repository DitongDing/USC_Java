package utils.GUI_final;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

// use repaint() to refresh
public class GraphCanvas extends JPanel
{
	private static final long serialVersionUID = 1161482980454204125L;

	public static int TOON = 0;
	public static int STIPPLING = 1;
	public static int STIPPLING_COLOR = 2;
	public static int GOOCH = 3;

	private BufferedImage[][] bi = null;
	private int imageIndex = 0;
	private int imageType = 0;
	public boolean go = true;

	public void setBI(BufferedImage[][] bi)
	{
		this.bi = bi;
	}

	public void setType(int type)
	{
		if (type == TOON || type == STIPPLING || type == STIPPLING_COLOR || type == GOOCH)
			this.imageType = type;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if (bi != null)
		{
			g.drawImage(bi[imageType][imageIndex], 0, 0, null);
			if (go)
				imageIndex = (imageIndex + 1) % bi[0].length;
			else
				go = true;
		}
	}
}
