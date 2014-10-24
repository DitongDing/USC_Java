package myGL;

public class Display extends Image
{
	public static short MAXXRES = 1024; // put some bounds on size in case of error
	public static short MAXYRES = 1024;
	public static int RGBAZ_DISPLAY = 1; // one type of dispClass

	public int dispClass;
	public short open;
}
