package myGL;

public class Render
{
	public static int MATLEVELS = 10; // how many matrix pushes allowed
	public static int MAX_LIGHTS = 10; // how many lights allowed

	public static int NULL_TOKEN = 0;// one type of nameList for putAttr and putTri. triangle vert attributes */
	public static int POSITION = 1;// one type of nameList for putAttr and putTri
	public static int NORMAL = 2;// one type of nameList for putAttr and putTri
	public static int TEXTURE_INDEX = 3;// one type of nameList for putAttr and putTri

	public static int Z_BUFFER_RENDER = 1; // one type of renderClass

	public static int RGB_COLOR = 99; // one type of nameList for putAttr and putTri
	public static int INTERPOLATE = 95; // define interpolation mode
	public static int SHADER = 96; // one type of nameList for putAttr and putTri

	// flags fields for value list attributes
	// shade mode flags combine the bit fields below
	public static int NONE = 0; // flat shading only
	public static int AMBIENT = 1; // can be selected or not
	public static int DIFFUSE = 2; // can be selected or not
	public static int SPECULAR = 4; // can be selected or not

	public static int DIRECTIONAL_LIGHT = 79; // directional light
	public static int AMBIENT_LIGHT = 78; // ambient light type

	public static int AMBIENT_COEFFICIENT = 1001; // Ka material property
	public static int DIFFUSE_COEFFICIENT = 1002; // Kd material property
	public static int SPECULAR_COEFFICIENT = 1003; // Ks material property
	public static int DISTRIBUTION_COEFFICIENT = 1004; // specular power of material

	public static int TEXTURE_MAP = 1010; // pointer to texture routine */

	// select interpolation mode of the shader (either one - not both)
	public static int FLAT = 0;
	public static int COLOR = 1; // interpolate vertex color
	public static int NORMALS = 2; // interpolate normals

	// coors constant
	public static int X = 0;
	public static int Y = 1;
	public static int Z = 2;

	// color constant
	public static int R = 0;
	public static int G = 1;
	public static int B = 2;

	// texture coords
	public static int U = 0;
	public static int V = 1;

	public int renderClass;
	public Display display;
	public boolean open = false;
	public Camera camera;
	public short matlevel = 0; // top of stack - current xform
	public Matrix[] Ximage = new Matrix[MATLEVELS]; // stack of xforms (Xsm)
	public Matrix[] Xnorm = new Matrix[MATLEVELS]; // xforms for norms (Xim)
	public Matrix Xsp = new Matrix(); // NDC to screen (pers-to-screen)
	public Color flatcolor; // color state for flat shaded triangles. Do not need to malloc, as every time we change value by "new"
	public int interp_mode = COLOR;
	public int numlights = 0;
	public Light[] lights = new Light[MAX_LIGHTS];
	public Light ambientlight;
	public Color Ka = new Color(), Kd = new Color(), Ks = new Color();
	public float spec; // specular power

	public Matrix[] MXimage = new Matrix[MATLEVELS]; // ***** Added by Ditong Ding, storing the multiplied matrix *****
	public Matrix[] MXnorm = new Matrix[MATLEVELS]; // ***** Added by Ditong Ding, storing the multiplied matrix *****
	public boolean begun = false; // ***** Added by Ditong Ding, to show if the begin function has been run *****

	public Render()
	{
		for (int i = 0; i < MXimage.length; i++)
			MXimage[i] = new Matrix();
		for (int i = 0; i < MXnorm.length; i++)
			MXnorm[i] = new Matrix();
	}

	public void tex_fun(float u, float v, float[] color)
	{
	}
}
