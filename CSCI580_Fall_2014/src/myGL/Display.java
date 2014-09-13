package myGL;

public class Display
{
	public static short MAXXRES = 1024; // put some bounds on size in case of error
	public static short MAXYRES = 1024;
	public static int GZ_RGBAZ_DISPLAY = 1; // one type of dispClass

	public short xres; // unsigned
	public short yres; // unsigned
	public int dispClass;
	public short open;
	public Pixel[] fbuf; // frame buffer array

	public void setPixel(int x, int y, Pixel pixel)
	{
		x = x < 0 ? 0 : (x >= xres ? xres - 1 : x);
		y = y < 0 ? 0 : (y >= yres ? yres - 1 : y);
		int index = x + yres * y;
		fbuf[index] = pixel;
	}

	public Pixel getPixel(int x, int y)
	{
		x = x < 0 ? 0 : (x >= xres ? xres - 1 : x);
		y = y < 0 ? 0 : (y >= yres ? yres - 1 : y);
		int index = x + yres * y;
		return fbuf[index];
	}
}
