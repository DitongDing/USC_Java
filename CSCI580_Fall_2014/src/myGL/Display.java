package myGL;

public class Display extends Image
{
	public static int RGBAZ_DISPLAY = 1; // one type of dispClass. DONOT consider other types right now

	private static short MAXXRES = 1024; // put some bounds on size in case of error
	private static short MAXYRES = 1024;

	private int dispClass;
	private Matrix Xsp = new Matrix(); // NDC to screen (pers-to-screen)
	
	public Display(Display display)
	{
		super(display.xres, display.yres);
		dispClass = display.dispClass;
		Xsp = display.Xsp;
	}

	public Display(int dispClass, int xRes, int yRes)
	{
		// if xRes or yRes is smaller than 0, than xres or yres = 0
		// if xRes or y Res is larger than MAXXRES or MAXYRES, than xres = MAXXRES or yres = MAXYRES
		super(xRes < 0 ? 0 : (xRes > Display.MAXXRES ? Display.MAXXRES : xRes), yRes < 0 ? 0 : (yRes > Display.MAXYRES ? Display.MAXYRES : yRes));
		this.dispClass = dispClass;
		float xs2 = xres / 2.0f;
		float ys2 = yres / 2.0f;
		float[][] value = { { xs2, 0, 0, xs2 }, { 0, -ys2, 0, ys2 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		Xsp.value = value;
	}

	public static int getRGBAZ_DISPLAY()
	{
		return RGBAZ_DISPLAY;
	}

	public int getDispClass()
	{
		return dispClass;
	}

	public Matrix getXsp()
	{
		return Xsp;
	}
}
