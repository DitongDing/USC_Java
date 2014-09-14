package myGL;

public class Render
{
	public static int MATLEVELS = 10; // how many matrix pushes allowed
	public static int MAX_LIGHTS = 10; // how many lights allowed
	public static int GZ_NULL_TOKEN = 0;// one type of nameList for putAttr and putTri. triangle vert attributes */
	public static int GZ_POSITION = 1;// one type of nameList for putAttr and putTri
	public static int GZ_NORMAL = 2;// one type of nameList for putAttr and putTri
	public static int GZ_TEXTURE_INDEX = 3;// one type of nameList for putAttr and putTri
	public static int GZ_Z_BUFFER_RENDER = 1; // one type of renderClass
	public static int GZ_RGB_COLOR = 99; // one type of nameList for putAttr and putTri
	public static int GZ_SHADER = 96; // one type of nameList for putAttr and putTri

	// flags fields for value list attributes
	// shade mode flags combine the bit fields below
	public static int GZ_NONE = 0; // flat shading only
	public static int GZ_AMBIENT = 1; // can be selected or not
	public static int GZ_DIFFUSE = 2; // can be selected or not
	public static int GZ_SPECULAR = 4; // can be selected or not

	// coors constant
	public static int X = 0;
	public static int Y = 1;
	public static int Z = 2;
	
	// color constant
	public static int R = 0;
	public static int G = 1;
	public static int B = 2;

	public int renderClass;
	public Display display;
	public boolean open;
	public Camera camera;
	public short matlevel; // top of stack - current xform
	public float[][][] Ximage = new float[MATLEVELS][4][4]; // stack of xforms (Xsm)
	public float[][][] Xnorm = new float[MATLEVELS][4][4]; // xforms for norms (Xim)
	public float[][] Xsp = new float[4][4]; // NDC to screen (pers-to-screen)
	public float[] flatcolor = new float[3]; // color state for flat shaded triangles
	public int interp_mode;
	public int numlights;
	public Light[] lights = new Light[MAX_LIGHTS];
	public Light ambientlight;
	public float[] Ka = new float[3], Kd = new float[3], Ks = new float[3];
	public float spec; // specular power
	// z_buf added by Ditong Ding, ***** not part of original API *****
	public float[][] z_buf;

	public void tex_fun(float u, float v, float[] color)
	{
	}
}
