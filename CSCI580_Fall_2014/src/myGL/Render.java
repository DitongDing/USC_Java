package myGL;

public class Render
{
	public static int MATLEVELS = 10; // how many matrix pushes allowed
	public static int MAX_LIGHTS = 10; // how many lights allowed

	public int renderClass;
	public Display display;
	public boolean open;
	public Camera camera;
	public short matlevel; // top of stack - current xform
	public Matrix[] Ximage; // stack of xforms (Xsm)
	public Matrix[] Xnorm; // xforms for norms (Xim)
	public Matrix Xsp; // NDC to screen (pers-to-screen)
	public Color flatcolor; // color state for flat shaded triangles
	public int interp_mode;
	public int numlights;
	public Light[] lights;
	public Light ambientlight;
	public Color Ka, Kd, Ks;
	public float spec; // specular power

	public void tex_fun(float u, float v, Color color)
	{
	}

	public static class Matrix
	{
		public float[][] value = new float[4][4];
	}

	public static class Color
	{
		public float[] value = new float[3];
	}

	public Render()
	{
		Ximage = new Matrix[MATLEVELS];
		Xnorm = new Matrix[MATLEVELS];
		for (int i = 0; i < MATLEVELS; i++)
		{
			Ximage[i] = new Matrix();
			Xnorm[i] = new Matrix();
		}
		lights = new Light[MAX_LIGHTS];
		for (int i = 0; i < lights.length; i++)
			lights[i] = new Light();
		Xsp = new Matrix();
		flatcolor = new Color();
		Ka = new Color();
		Kd = new Color();
		Ks = new Color();

	}
}
