package myGL;

public class Pixel
{
	public short red;
	public short green;
	public short blue;
	public short alpha;
	public int z; // signed z for clipping
	
	public Pixel()
	{
	}
	
	public Pixel(short red, short green, short blue, short alpha, int z)
	{
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
		this.z = z;
	}
	
	public void copy(Pixel pixel)
	{
		red = pixel.red;
		green = pixel.green;
		blue = pixel.blue;
		alpha = pixel.alpha;
		z = pixel.z;
	}
}
