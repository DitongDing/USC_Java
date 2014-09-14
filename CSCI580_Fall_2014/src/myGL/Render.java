package myGL;

public class Render
{
	public static int MATLEVELS = 10; // how many matrix pushes allowed
	public static int MAX_LIGHTS = 10; // how many lights allowed
	public static int GZ_Z_BUFFER_RENDER = 1; // one type of renderClass
	public static int GZ_POSITION = 1; // one type of nameList for putAttr and putTri

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

	public void tex_fun(float u, float v, float[] color)
	{
	}
}
