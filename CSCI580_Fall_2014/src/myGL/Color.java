package myGL;

import utils.ComUtils;

// Different from color in class Pixel. The latter one is short type, for writing to file. This one is for calculating.
public class Color
{
	public float red;
	public float green;
	public float blue;

	public Color()
	{

	}

	public Color(float red, float green, float blue)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color(Pixel pixel, short global_max)
	{
		this.red = pixel.red / (float) global_max;
		this.green = pixel.green / (float) global_max;
		this.blue = pixel.blue / (float) global_max;
	}

	// Return vector represent.
	public float[] getVector()
	{
		float[] re = { red, green, blue };
		return re;
	}

	public static Color interpolateColor(Color start, Color end, float progress)
	{
		return new Color(ComUtils.interpolateFloat(start.red, end.red, progress), ComUtils.interpolateFloat(start.green, end.green, progress),
				ComUtils.interpolateFloat(start.blue, end.blue, progress));
	}
}
