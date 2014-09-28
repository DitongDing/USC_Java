package myGL;

public class UIInput
{
	public static short ROTATION_X = 0; // incremental, only x can be non-zero;
	public static short ROTATION_Y = 1; // incremental, only y can be non-zero;
	public static short ROTATION_Z = 2; // incremental, only z can be non-zero;
	public static short TRANSLATION = 3; // incremental;
	public static short SCALE = 4; // incremental;
	public static short CAMERA = 5; // not incremental;

	public static short OBJECT = 0;
	public static short WORLD = 1;

	public static int defaultFPS = 20; // Param for animation

	public short space = -1;
	public short type = -1;
	public Coord rotation = null;
	public Coord translation = null;
	public Coord scale = null;
	public Camera camera = null;
	public double period = 0; // the length of interval to perform this input. Zero means no animation.

	public UIInput(short type, short space, Object action)
	{
		if (type == ROTATION_X || type == ROTATION_Y || type == ROTATION_Z)
			rotation = (Coord) action;
		else if (type == TRANSLATION)
			translation = (Coord) action;
		else if (type == SCALE)
			scale = (Coord) action;
		else if (type == CAMERA)
			camera = (Camera) action;
		this.type = type;
		this.space = space;
	}
}