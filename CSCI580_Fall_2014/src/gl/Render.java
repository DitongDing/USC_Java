package gl;

import gl.texture.TextureFunction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import gl.Pixel;
import utils.MathUtils;

public class Render
{
	// TODO: remove test flag
	public static boolean TEST_TOON = false;
	public static boolean TEST_STIPPLING = true;
	public static boolean TEST_STIPPLING_COLOR = true;
	public static boolean TEST_GOOCH = false;

	public static int MATLEVELS = 100; // how many matrix pushes allowed
	public static int MAX_LIGHTS = 10; // how many lights allowed

	public static int NULL_TOKEN = 0;// one type of nameList for putAttr and putTri. triangle vert attributes
	public static int POSITION = 1;// one type of nameList for putAttr and putTri
	public static int NORMAL = 2;// one type of nameList for putAttr and putTri
	public static int TEXTURE_INDEX = 3;// one type of nameList for putAttr and putTri

	public static int Z_BUFFER_RENDER = 1; // one type of renderClass

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

	public static int XPI_IN_TOP = 1;
	public static int XIW_IN_TOP = 2;

	private int renderClass;
	private Camera camera;
	private short matlevel = 0; // top of stack - current xform
	private Matrix[] Ximage = new Matrix[MATLEVELS]; // stack of xforms (Xpm)
	private Matrix[] Xnorm = new Matrix[MATLEVELS]; // xforms for norms (Xim)
	private int interp_mode = NORMALS;
	private int numlights = 0;
	private Light[] lights = new Light[MAX_LIGHTS];
	private Light ambientlight;
	private Color Ka = new Color(), Kd = new Color(), Ks = new Color();
	private float spec; // specular power
	private TextureFunction textureFunction = null;
	private float[][] aaOffset = { { 0, 0, 1 } }; // in (x, y, w) manner, (0, 0, 1) means no aa

	private Matrix[] MXimage = new Matrix[MATLEVELS]; // ***** Added by Ditong Ding, storing the multiplied matrix *****
	private Matrix[] MXnorm = new Matrix[MATLEVELS]; // ***** Added by Ditong Ding, storing the multiplied matrix *****

	public Render(int renderClass) throws Exception
	{
		if (renderClass != Render.Z_BUFFER_RENDER)
			throw new Exception("render class error in new render");

		this.renderClass = renderClass;
		camera = new Camera();

		for (int i = 0; i < MXimage.length; i++)
			MXimage[i] = new Matrix();
		for (int i = 0; i < MXnorm.length; i++)
			MXnorm[i] = new Matrix();

		PushMatrix(camera.getXpi());
		PushMatrix(camera.getXiw());
	}

	// Write render result to display
	// IMPORTANT: NO valid A, Z value in final result if use Multipass aa.
	public void runRender(ArrayList<Vertex[]> triList, Display display, Pixel defaultPixel) throws Exception
	{
		display.Reset(defaultPixel);

		float[][] r = new float[display.getXres()][display.getYres()];
		float[][] g = new float[display.getXres()][display.getYres()];
		float[][] b = new float[display.getXres()][display.getYres()];
		float[][] a = new float[display.getXres()][display.getYres()];
		float[][] z = new float[display.getXres()][display.getYres()];
		for (int i = 0; i < aaOffset.length; i++)
		{
			display.Reset(defaultPixel);
			// Walk through the list of triangles, set color and pass vert info to render/scan convert each triangle
			for (Vertex[] tri : triList)
				DrawTriangle(display, tri, aaOffset[i]);
			Image image = display;
			if (TEST_TOON || TEST_STIPPLING || TEST_GOOCH)
				image = edgeDetector(image, camera.getD());
			if (TEST_STIPPLING)
				image = stippling(image, TEST_STIPPLING_COLOR);
			for (int x = 0; x < display.getXres(); x++)
				for (int y = 0; y < display.getYres(); y++)
				{
					r[x][y] += image.getPixel(x, y).red * aaOffset[i][2];
					g[x][y] += image.getPixel(x, y).green * aaOffset[i][2];
					b[x][y] += image.getPixel(x, y).blue * aaOffset[i][2];
					a[x][y] += image.getPixel(x, y).alpha * aaOffset[i][2];
					z[x][y] += image.getPixel(x, y).z * aaOffset[i][2];
				}
		}

		for (int x = 0; x < display.getXres(); x++)
			for (int y = 0; y < display.getYres(); y++)
				display.setPixel(x, y, new Pixel((short) r[x][y], (short) g[x][y], (short) b[x][y], (short) a[x][y], z[x][y]));

		display.calculateGM();
	}

	// Push a matrix onto the Ximage, Xnorm stack and push a multipled matrix onto the MXimage, MXnorm stack
	public void PushMatrix(Matrix matrix) throws Exception
	{
		if (matlevel == Render.MATLEVELS)
			throw new Exception("Render matrix (Xsm) stack overflow");
		Ximage[matlevel] = matrix;
		if (matlevel == 0)
			MXimage[0] = matrix;
		else
			MXimage[matlevel].value = MathUtils.Multiply(MXimage[matlevel - 1].value, matrix.value);

		// Actually, do not need to consider unitary matrix for Xnorm, as we can do normalize after Xn.
		// But still need to consider non-uniform scaling.
		// Do not need to consider translation effect. As it only affects the last column of matrix, which will not be used in Xnorm cal.
		// Only change the value of matrix comes after Xiw.
		// For scaling matrix or rotation matrix with θ=90k. in case of non-uniform scaling
		// matelevel >= Xxx_IN_TOP means when push matrix, the status of stack is at or after Xxx_IN_TOP
		Matrix matrix4Xnorm = new Matrix(matrix);
		if (matlevel >= XIW_IN_TOP && matrix4Xnorm.value[0][1] == 0 && matrix4Xnorm.value[0][2] == 0 && matrix4Xnorm.value[1][2] == 0)
		{
			matrix4Xnorm.value[0][0] = 1 / matrix4Xnorm.value[0][0];
			matrix4Xnorm.value[1][1] = 1 / matrix4Xnorm.value[1][1];
			matrix4Xnorm.value[2][2] = 1 / matrix4Xnorm.value[2][2];
		}
		Xnorm[matlevel] = matrix4Xnorm;
		// Ignore Xpi
		if (matlevel < XIW_IN_TOP)
			MXnorm[matlevel] = matrix4Xnorm;
		else
			MXnorm[matlevel].value = MathUtils.Multiply(MXnorm[matlevel - 1].value, matrix4Xnorm.value);

		matlevel++;
	}

	// Pop a matrix off the Ximage stack
	public Matrix PopMatrix() throws Exception
	{
		if (matlevel == 0)
			throw new Exception("Render matrix (Xpm) stack underflow");
		return Ximage[--matlevel];
	}

	// Push light
	public void PushLight(Light light) throws Exception
	{
		if (numlights != Render.MAX_LIGHTS)
			lights[numlights++] = light;
		else
			throw new Exception("Light stack overflow");
	}

	// Pop light
	public Light PopLight(Light light) throws Exception
	{
		if (this.numlights == 0)
			throw new Exception("Light stack underflow");
		return lights[--numlights];
	}

	public void DrawTriangle(Display display, Vertex[] tri, float[] aaOffset) throws Exception
	{
		if (tri == null || tri.length != 3)
			throw new Exception("Triangle data error");
		// Interpolate to X:[0, xRes], Y:[0, yRes];
		// Apply LEE, not SLR. Try SLR later
		float[][] vertexPosition = new float[3][3];
		float[][] vertexNorm = new float[3][3];
		float[][] vertexUV = new float[3][2];
		for (int i = 0; i < tri.length; i++)
		{
			vertexPosition[i][0] = tri[i].x;
			vertexPosition[i][1] = tri[i].y;
			vertexPosition[i][2] = tri[i].z;
			vertexNorm[i][0] = tri[i].norm.x;
			vertexNorm[i][1] = tri[i].norm.y;
			vertexNorm[i][2] = tri[i].norm.z;
			vertexUV[i][0] = tri[i].U;
			vertexUV[i][1] = tri[i].V;
		}

		int countOfTooNearVertices = 0;
		float[][] verticalVector = new float[4][1];
		for (int iTri = 0; iTri < vertexPosition.length; iTri++)
		{
			int iValue;
			for (iValue = 0; iValue < verticalVector.length - 1; iValue++)
				verticalVector[iValue][0] = vertexPosition[iTri][iValue];
			verticalVector[iValue][0] = 1;
			// ***** perform Xwm, Xiw, Xpi, Xsp and Homogeneous Coordinate *****
			Matrix matrix = MXimage[matlevel - 1];
			verticalVector = MathUtils.Multiply(matrix.value, verticalVector);
			verticalVector = MathUtils.Multiply(display.getXsp().value, verticalVector);
			// ***** detect off screen vertices *****
			if (verticalVector[verticalVector.length - 1][0] < 1)
				countOfTooNearVertices++;
			// TODO Render.DrawTriangle(): I think we need to consider verticalVector[verticalVector.length - 1][0] may be zero
			verticalVector = MathUtils.Multiply(1 / verticalVector[verticalVector.length - 1][0], verticalVector);
			for (iValue = 0; iValue < verticalVector.length - 1; iValue++)
				vertexPosition[iTri][iValue] = verticalVector[iValue][0];
		}
		if (countOfTooNearVertices == vertexPosition.length)
			return;

		for (float[] value : vertexPosition)
		{
			value[X] += aaOffset[X];
			value[Y] += aaOffset[Y];
		}

		// The way to implement:
		// Do not need to arrange vertex. To consider the sign of three judgment.
		// Calculate normal of plane, then calculate Z value with normal, X and Y. These two vector is vertical with each other.
		// It is faster, and comparison shows, the difference is plus-minus 0.00003%, which can be considered as the lose of float calculation.
		float[][] vertexList = vertexPosition;

		// check if three points in xOy plane are in the same line
		float[][] vectors = new float[2][3];
		for (int j = 0; j < 2; j++)
			for (int k = 0; k < 2; k++)
				vectors[j][k] = vertexList[j][k] - vertexList[j + 1][k];
		// They are in the same line
		if (vectors[0][0] * vectors[1][1] == vectors[0][1] * vectors[1][0])
			return;

		float[] VN = new float[3];
		float[] CrN = new float[3];
		float[] CgN = new float[3];
		float[] CbN = new float[3];
		float[] N1N = new float[3];
		float[] N2N = new float[3];
		float[] N3N = new float[3];
		float[] TUN = new float[3];
		float[] TVN = new float[3];
		Color[] vertexColor = new Color[3];

		// Calculate normal vector of plane v1,v2,v3 in screen space
		for (int j = 0; j < 2; j++)
			vectors[j][2] = vertexList[j][2] - vertexList[j + 1][2];
		VN = MathUtils.CrossProduct(vectors[0], vectors[1]);

		// Some assumptions for calculate color
		// Transform all to image space
		// Interpolate color/norms in screen space (ignore the fact that norms are in model space.)
		// Use constant E vector (0, 0, -1)
		if (interp_mode == Render.NORMALS)
		{
			// Calculate normal vector of plane v1,v2,v3 in XYN1 space
			for (int j = 0; j < 2; j++)
				vectors[j][2] = vertexNorm[j][0] - vertexNorm[j + 1][0];
			N1N = MathUtils.CrossProduct(vectors[0], vectors[1]);
			// Calculate normal vector of plane v1,v2,v3 in XYN2 space
			for (int j = 0; j < 2; j++)
				vectors[j][2] = vertexNorm[j][1] - vertexNorm[j + 1][1];
			N2N = MathUtils.CrossProduct(vectors[0], vectors[1]);
			// Calculate normal vector of plane v1,v2,v3 in XYN3 space
			for (int j = 0; j < 2; j++)
				vectors[j][2] = vertexNorm[j][2] - vertexNorm[j + 1][2];
			N3N = MathUtils.CrossProduct(vectors[0], vectors[1]);
		}
		else
		{
			if (tex_fun(0, 0) == null)
				for (int j = 0; j < 3; j++)
					vertexColor[j] = calColor(Ka, Kd, Ks, vertexNorm[j], new float[] { 0, 0, -1 });
			else
				for (int j = 0; j < 3; j++)
				{
					// Assume for HW5, flat color mode use Kt as Ka, Kd, Kd
					Color texture = tex_fun(vertexUV[j][0], vertexUV[j][1]);
					vertexColor[j] = calColor(texture, texture, texture, vertexNorm[j], new float[] { 0, 0, -1 });
				}
			if (interp_mode == Render.COLOR)
			{
				// Calculate normal vector of plane v1,v2,v3 in XYCr space
				for (int j = 0; j < 2; j++)
					vectors[j][2] = vertexColor[j].red - vertexColor[j + 1].red;
				CrN = MathUtils.CrossProduct(vectors[0], vectors[1]);
				// Calculate normal vector of plane v1,v2,v3 in XYCg space
				for (int j = 0; j < 2; j++)
					vectors[j][2] = vertexColor[j].green - vertexColor[j + 1].green;
				CgN = MathUtils.CrossProduct(vectors[0], vectors[1]);
				// Calculate normal vector of plane v1,v2,v3 in XYCb space
				for (int j = 0; j < 2; j++)
					vectors[j][2] = vertexColor[j].blue - vertexColor[j + 1].blue;
				CbN = MathUtils.CrossProduct(vectors[0], vectors[1]);
			}
		}
		if (interp_mode == Render.NORMALS && tex_fun(0, 0) != null)
		{
			// transform UV to perspective space
			for (int j = 0; j < 3; j++)
				for (int k = 0; k < 2; k++)
					vertexUV[j][k] = vertexUV[j][k] / (1 + vertexList[j][2] / (camera.getD() - vertexList[j][2]));
			for (int j = 0; j < 2; j++)
				vectors[j][2] = vertexUV[j][0] - vertexUV[j + 1][0];
			TUN = MathUtils.CrossProduct(vectors[0], vectors[1]);
			for (int j = 0; j < 2; j++)
				vectors[j][2] = vertexUV[j][1] - vertexUV[j + 1][1];
			TVN = MathUtils.CrossProduct(vectors[0], vectors[1]);
		}

		// calculate ulx, uly, lrx, lry
		float ulx = vertexList[0][Render.X], uly = vertexList[0][Render.Y], lrx = vertexList[0][Render.X], lry = vertexList[0][Render.Y];
		for (int j = 1; j < vertexList.length; j++)
		{
			if (vertexList[j][Render.X] < ulx)
				ulx = vertexList[j][Render.X];
			if (vertexList[j][Render.X] > lrx)
				lrx = vertexList[j][Render.X];
			if (vertexList[j][Render.Y] < uly)
				uly = vertexList[j][Render.Y];
			if (vertexList[j][Render.Y] > lry)
				lry = vertexList[j][Render.Y];
		}

		float[] A = new float[3];
		float[] B = new float[3];
		float[] C = new float[3];
		A[0] = vertexList[1][Render.Y] - vertexList[0][Render.Y];
		A[1] = vertexList[2][Render.Y] - vertexList[1][Render.Y];
		A[2] = vertexList[0][Render.Y] - vertexList[2][Render.Y];
		B[0] = vertexList[0][Render.X] - vertexList[1][Render.X];
		B[1] = vertexList[1][Render.X] - vertexList[2][Render.X];
		B[2] = vertexList[2][Render.X] - vertexList[0][Render.X];
		C[0] = -(A[0] * vertexList[0][Render.X] + B[0] * vertexList[0][Render.Y]);
		C[1] = -(A[1] * vertexList[1][Render.X] + B[1] * vertexList[1][Render.Y]);
		C[2] = -(A[2] * vertexList[2][Render.X] + B[2] * vertexList[2][Render.Y]);

		int x1 = (int) Math.floor(ulx), x2 = (int) Math.ceil(lrx), y1 = (int) Math.floor(uly), y2 = (int) Math.ceil(lry);
		x1 = x1 < 0 ? 0 : (x1 >= display.getXres() ? display.getXres() : x1);
		x2 = x2 < 0 ? 0 : (x2 >= display.getXres() ? display.getXres() : x2);
		y1 = y1 < 0 ? 0 : (y1 >= display.getYres() ? display.getYres() : y1);
		y2 = y2 < 0 ? 0 : (y2 >= display.getYres() ? display.getYres() : y2);
		for (int Y = y1; Y < y2; Y++)
			for (int X = x1; X < x2; X++)
				if (Math.abs(MathUtils.PixelJudge_LEE(A[0], B[0], C[0], X, Y) + MathUtils.PixelJudge_LEE(A[1], B[1], C[1], X, Y)
						+ MathUtils.PixelJudge_LEE(A[2], B[2], C[2], X, Y)) == 3)
				{
					float[] point = { vertexList[0][0], vertexList[0][1], vertexList[0][2] };
					float Z = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, VN);
					if (display.getPixel(X, Y).z > Z)
					{
						Color color = null;
						if (interp_mode == Render.FLAT)
							color = vertexColor[0];
						else if (interp_mode == Render.COLOR)
						{
							point[2] = vertexColor[0].red;
							float r = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, CrN);
							point[2] = vertexColor[0].green;
							float g = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, CgN);
							point[2] = vertexColor[0].blue;
							float b = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, CbN);
							color = new Color(r, g, b);
						}
						else if (interp_mode == Render.NORMALS)
						{
							point[2] = vertexNorm[0][0];
							float N1 = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, N1N);
							point[2] = vertexNorm[0][1];
							float N2 = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, N2N);
							point[2] = vertexNorm[0][2];
							float N3 = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, N3N);
							if (tex_fun(0, 0) == null)
								color = calColor(Ka, Kd, Ks, new float[] { N1, N2, N3 }, new float[] { 0, 0, -1 });
							else
							{
								float multiplier = 1 + Z / (camera.getD() - Z);
								point[2] = vertexUV[0][0];
								float U = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, TUN);
								point[2] = vertexUV[0][1];
								float V = MathUtils.InterpolateValueByXYAndNorm(X, Y, point, TVN);
								Color texture = tex_fun(U * multiplier, V * multiplier);
								color = calColor(texture, texture, Ks, new float[] { N1, N2, N3 }, new float[] { 0, 0, -1 });
							}
						}
						else
							throw new Exception("render's color interpolate mode error");

						display.setPixel(X, Y, new Pixel(MathUtils.ctoi(color.red), MathUtils.ctoi(color.green), MathUtils.ctoi(color.blue), (short) 1, Z));
					}
				}
	}

	// NULL means do not add texture
	public Color tex_fun(float u, float v)
	{
		return textureFunction == null ? null : textureFunction.getColor(u, v);
	}

	private Color calColor(Color KA, Color KD, Color KS, float[] Nm, float[] E) throws Exception
	{
		Color color = new Color(0, 0, 0);

		float[] Ka = KA.getVector();
		float[] la = ambientlight.color.getVector();
		float[] Kd = KD.getVector();
		float[][] le = new float[numlights][];
		// Convert N to image space
		float[][] NMatrix = MathUtils.Multiply(MXnorm[matlevel - 1].value, new float[][] { { Nm[0] }, { Nm[1] }, { Nm[2] }, { 0 } });
		float[] N = MathUtils.Normalize(new float[] { NMatrix[0][0], NMatrix[1][0], NMatrix[2][0] });
		float[][] L = new float[numlights][];
		float[][] R = new float[numlights][];
		for (int i = 0; i < L.length; i++)
		{
			le[i] = lights[i].color.getVector();
			L[i] = new float[] { lights[i].direction.x, lights[i].direction.y, lights[i].direction.z };
			R[i] = MathUtils.Plus(MathUtils.Multiply(2 * MathUtils.Multiply(N, L[i]), N), MathUtils.Multiply(-1, L[i]));
		}
		float[] Ks = KS.getVector();
		float s = spec;

		if (TEST_GOOCH)
		{
			// flag
			int fg_lightnum = 1; // light number, normally 1, need to set light source to upper right or left with the same side of camera
									// need to take care of brightness
			int fg_lightcolor = 0; // enable light color, if disabled all lights are white, resulting yellow to blue color

			int fg_multblend = 0; // multiplication blending mode, outside the scope of original gooch shading
			float blend_addxmult = 1.0f; // blending factor between normal and multiplication

			int fg_Specular = 1; // switch of Specular light
			int fg_Ambient = 1; // switch of Ambient light

			// color factor of kBlue = {0,0,b} and kYellow = {y,y,0}
			float y = 0.3f;
			float b = 0.55f;

			// color factor of kCool = kBlue + alpha * render->Kd and kWarm = kYellow + beta * render->Kd
			float alpha = 0.35f;
			float beta = 0.5f;

			float[] LSpeculars = { 0, 0, 0 }, LDResult = { 0, 0, 0 };
			float[] LDiffusesAdd = { 0, 0, 0 }, LDiffusesMult = { 0, 0, 0 };

			float RE, NL;
			Color kBlue = new Color(0, 0, b);
			Color kYellow = new Color(y, y, 0);
			Color kWarm = new Color(), kCool = new Color();
			String[] fieldName = { "red", "green", "blue" };

			// compute kCool and kWarm
			for (int j = 0; j < 3; j++)
			{
				Field field = new Color().getClass().getDeclaredField(fieldName[j]);
				field.set(kCool, field.getFloat(kBlue) + alpha * Kd[j]);
				field.set(kWarm, field.getFloat(kYellow) + beta * Kd[j]);
			}

			// check number of lights
			fg_lightnum = (fg_lightnum > numlights) ? numlights : fg_lightnum;

			for (int i = 0; i < fg_lightnum; i++)
			{
				float[] Ldir = L[i];
				float[] Lcolor = le[i];

				// Speculars
				RE = MathUtils.Multiply(R[i], E);

				if (RE > 0)
					LSpeculars = MathUtils.Plus(LSpeculars, MathUtils.Multiply((float) Math.pow(RE, s), Lcolor));

				// Diffuses
				NL = MathUtils.Multiply(N, Ldir);

				if (NL * MathUtils.Multiply(N, E) > 0)
				{
					if (NL < 0)
					{
						NL = -NL;
						N = MathUtils.Multiply(-1, N);
					}
					// LDiffuses = LDiffuses + Lcolor * NL;

					float cokcool = (1 - MathUtils.Multiply(N, Ldir)) / 2;

					if (fg_lightcolor == 1)
					{
						for (int j = 0; j < 3; j++)
						{
							Field field = new Color().getClass().getDeclaredField(fieldName[j]);
							LDiffusesAdd[j] += (cokcool * field.getFloat(kCool) + (1 - cokcool) * field.getFloat(kWarm)) * Lcolor[j];
						}

						if (fg_multblend == 1)
						{
							if (LDiffusesMult[0] == 0)
								LDiffusesMult[0] += (cokcool * kCool.red + (1 - cokcool) * kWarm.red) * Lcolor[0];
							else
								LDiffusesMult[0] *= (cokcool * kCool.red + (1 - cokcool) * kWarm.red) * Lcolor[0];
							if (LDiffusesMult[0] == 0)
								LDiffusesMult[1] += (cokcool * kCool.green + (1 - cokcool) * kWarm.green) * Lcolor[1];
							else
								LDiffusesMult[1] *= (cokcool * kCool.green + (1 - cokcool) * kWarm.green) * Lcolor[1];
							if (LDiffusesMult[0] == 0)
								LDiffusesMult[2] += (cokcool * kCool.blue + (1 - cokcool) * kWarm.blue) * Lcolor[2];
							else
								LDiffusesMult[2] *= (cokcool * kCool.blue + (1 - cokcool) * kWarm.blue) * Lcolor[2];
						}
					}
					else
					{
						for (int j = 0; j < 3; j++)
						{
							Field field = new Color().getClass().getDeclaredField(fieldName[j]);
							LDiffusesAdd[j] += (cokcool * field.getFloat(kCool) + (1 - cokcool) * field.getFloat(kWarm));
						}

						if (fg_multblend == 1)
						{
							if (LDiffusesMult[0] == 0)
								LDiffusesMult[0] += (cokcool * kCool.red + (1 - cokcool) * kWarm.red);
							else
								LDiffusesMult[0] *= (cokcool * kCool.red + (1 - cokcool) * kWarm.red);
							if (LDiffusesMult[0] == 0)
								LDiffusesMult[1] += (cokcool * kCool.green + (1 - cokcool) * kWarm.green);
							else
								LDiffusesMult[1] *= (cokcool * kCool.green + (1 - cokcool) * kWarm.green);
							if (LDiffusesMult[0] == 0)
								LDiffusesMult[2] += (cokcool * kCool.blue + (1 - cokcool) * kWarm.blue);
							else
								LDiffusesMult[2] *= (cokcool * kCool.blue + (1 - cokcool) * kWarm.blue);
						}
					}
				}
			}

			LDResult = (fg_multblend == 1) ? MathUtils.Plus(MathUtils.Multiply(blend_addxmult, LDiffusesAdd),
					MathUtils.Multiply((1 - blend_addxmult), LDiffusesMult)) : LDiffusesAdd;

			color.red = LDResult[0];
			color.green = LDResult[1];
			color.blue = LDResult[2];

			if (fg_Specular == 1)
			{
				color.red += Ks[0] * LSpeculars[0];
				color.green += Ks[1] * LSpeculars[1];
				color.blue += Ks[2] * LSpeculars[2];
			}

			if (fg_Ambient == 1)
			{
				color.red += Ka[0] * la[0];
				color.green += Ka[1] * la[1];
				color.blue += Ka[2] * la[2];
			}

			color.red = color.red < 0 ? 0 : (color.red > 1 ? 1 : color.red);
			color.green = color.green < 0 ? 0 : (color.green > 1 ? 1 : color.green);
			color.blue = color.blue < 0 ? 0 : (color.blue > 1 ? 1 : color.blue);
		}
		else
		{
			if (MathUtils.Multiply(N, E) < 0)
				N = MathUtils.Multiply(-1, N);

			float[] tempColor = { 0, 0, 0 };

			if (TEST_TOON)
			{
				float[] Dle = { 0, 0, 0 };
				for (int i = 0; i < numlights; i++)
				{
					float NL = MathUtils.Multiply(N, L[i]);
					if (NL < 0)
						continue;
					NL = NL < 0.3 ? 0.3f : (NL < 0.7 ? 0.6f : 0.9f);
					Dle = MathUtils.Plus(Dle, MathUtils.Multiply(NL, le[i]));
				}
				for (int i = 0; i < tempColor.length; i++)
					tempColor[i] += Kd[i] * Dle[i];
			}
			else
			{
				float[] Sle = { 0, 0, 0 };
				for (int i = 0; i < numlights; i++)
				{
					float RE = MathUtils.Multiply(R[i], E);
					if (RE <= 0)
						continue;
					RE = (float) Math.pow(RE, s);
					Sle = MathUtils.Plus(Sle, MathUtils.Multiply(RE, le[i]));
				}
				for (int i = 0; i < tempColor.length; i++)
					tempColor[i] += Ks[i] * Sle[i];

				if (tempColor[0] < 1 || tempColor[1] < 1 || tempColor[2] < 1)
				{
					float[] Dle = { 0, 0, 0 };
					for (int i = 0; i < numlights; i++)
					{
						float NL = MathUtils.Multiply(N, L[i]);
						if (NL < 0)
							continue;
						Dle = MathUtils.Plus(Dle, MathUtils.Multiply(NL, le[i]));
					}
					for (int i = 0; i < tempColor.length; i++)
						tempColor[i] += Kd[i] * Dle[i];
				}

				if (tempColor[0] < 1 || tempColor[1] < 1 || tempColor[2] < 1)
					for (int i = 0; i < tempColor.length; i++)
						tempColor[i] += Ka[i] * la[i];
			}

			color.red = tempColor[0] > 1 ? 1 : tempColor[0];
			color.green = tempColor[1] > 1 ? 1 : tempColor[1];
			color.blue = tempColor[2] > 1 ? 1 : tempColor[2];
		}

		return color;
	}

	// Design for toon & stippling
	public Image edgeDetector(Image image, float ZMax)
	{
		Image re = new Image(image.getXres(), image.getYres());

		double threshold = 0.25;

		for (int x = 0; x < re.getXres(); x++)
			for (int y = 0; y < re.getYres(); y++)
			{
				re.setPixel(x, y, new Pixel(image.getPixel(x, y)));
				re.getPixel(x, y).z = 1 / (1 / re.getPixel(x, y).z - 1 / ZMax);
			}

		float min = Float.MAX_VALUE;
		float max = -1;

		for (int x = 0; x < re.getXres(); x++)
			for (int y = 0; y < re.getYres(); y++)
			{
				if (re.getPixel(x, y).z != Float.MAX_VALUE && re.getPixel(x, y).z > max)
					max = re.getPixel(x, y).z;
				else if (re.getPixel(x, y).z < min)
					min = re.getPixel(x, y).z;
			}

		Pixel black = new Pixel((short) 0, (short) 0, (short) 0, (short) 1, 0);

		for (int x = 1; x < re.getXres() - 1; x++)
			for (int y = 1; y < re.getYres() - 1; y++)
			{
				double Gx = ((image.getPixel(x + 1, y - 1).z + 2 * image.getPixel(x + 1, y).z + image.getPixel(x + 1, y + 1).z) - (image.getPixel(x - 1, y - 1).z
						+ 2 * image.getPixel(x - 1, y).z + image.getPixel(x - 1, y + 1).z));
				double Gy = ((image.getPixel(x - 1, y - 1).z + 2 * image.getPixel(x, y - 1).z + image.getPixel(x + 1, y - 1).z) - (image.getPixel(x - 1, y + 1).z
						+ 2 * image.getPixel(x, y + 1).z + image.getPixel(x + 1, y + 1).z));
				double G = Math.sqrt(Gx * Gx + Gy * Gy);
				if (G > threshold * (max - min))
					re.setPixel(x, y, black);
			}

		return re;
	}

	// design for stippling -- start --
	public Image stippling(Image image, boolean isColorStippling)
	{
		Image re = new Image(image.getXres(), image.getYres());
		int size = re.getXres() * re.getYres();

		for (int x = 0; x < re.getXres(); x++)
			for (int y = 0; y < re.getYres(); y++)
			{
				Pixel pixel = image.getPixel(x, y);
				pixel.red = pixel.green = pixel.blue = 255 << 4;
				re.setPixel(x, y, pixel);
			}

		Random random = new Random();

		int reserve = 0;
		int reserveBL = 0;
		int reserveG = 0;
		int reserveR = 0;
		if (!isColorStippling)
		{// black
			long mean = 0;
			for (int index = 0; index < size; ++index)
			{
				Pixel temp = image.getPixel(index);
				short blue = temp.blue;
				short green = temp.green;
				short red = temp.red;
				short grey = (short) ((red * 30 + green * 59 + blue * 11) / 100);
				mean += grey;
			}
			mean = mean / size;
			mean = mean >> 4;
			reserve = (int) (mean / 10 + 5);
			for (int index = 0; index < size; ++index)
			{
				Pixel temp = image.getPixel(index);
				short blue = temp.blue;
				short green = temp.green;
				short red = temp.red;
				short grey = (short) ((red * 30 + green * 59 + blue * 11) / 100);
				grey = (short) (grey >> 4);
				int level = grey / 10 + 8;
				if (random.nextInt(reserve) >= level)
					re.setPixel(index, new Pixel((short) 0, (short) 0, (short) 0, (short) 1, 0));
			}
		}
		else
		{// color
			long meanBL = 0;
			long meanG = 0;
			long meanR = 0;
			for (int index = 0; index < size; ++index)
			{
				Pixel temp = image.getPixel(index);
				short blue = temp.blue;
				short green = temp.green;
				short red = temp.red;
				meanBL += blue;
				meanG += green;
				meanR += red;
			}
			meanBL = meanBL / size;
			meanBL = meanBL >> 4;
			reserveBL = (int) (meanBL / 10 + 5);
			meanG = meanG / size;
			meanG = meanG >> 4;
			reserveG = (int) (meanG / 10 + 5);
			meanR = meanR / size;
			meanR = meanR >> 4;
			reserveR = (int) (meanR / 10 + 5);
			for (int index = 0; index < size; ++index)
			{
				Pixel temp = image.getPixel(index);
				short blue = temp.blue;
				short green = temp.green;
				short red = temp.red;
				short b;
				short r;
				short g;
				b = blue;
				g = green;
				r = red;
				b = (short) (b >> 4);
				g = (short) (g >> 4);
				r = (short) (r >> 4);
				int levelBL = b / 10 + 8;
				int levelG = g / 10 + 8;
				int levelR = r / 10 + 8;
				Pixel pixel = new Pixel((short) 0, (short) 0, (short) 0, (short) 1, 0);
				if (random.nextInt(reserveBL) <= levelBL)
					pixel.blue = 255 << 4;
				if (random.nextInt(reserveG) <= levelG)
				{
					pixel.green = 255 << 4;
				}
				if (random.nextInt(reserveR) <= levelR)
					pixel.red = 255 << 4;
				re.setPixel(index, pixel);
			}
		}

		return re;
	}

	// design for stippling -- end --

	public static Matrix CreateRotationByXMatrix(float degree)
	{
		float sin = (float) Math.sin(degree * MathUtils.DEGREE_2_RAD);
		float cos = (float) Math.cos(degree * MathUtils.DEGREE_2_RAD);
		float[][] value = { { 1, 0, 0, 0 }, { 0, cos, -sin, 0 }, { 0, sin, cos, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(value);
	}

	public static Matrix CreateRotationByYMatrix(float degree)
	{
		float sin = (float) Math.sin(degree * MathUtils.DEGREE_2_RAD);
		float cos = (float) Math.cos(degree * MathUtils.DEGREE_2_RAD);
		float[][] value = { { cos, 0, sin, 0 }, { 0, 1, 0, 0 }, { -sin, 0, cos, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(value);
	}

	public static Matrix CreateRotationByZMatrix(float degree)
	{
		float sin = (float) Math.sin(degree * MathUtils.DEGREE_2_RAD);
		float cos = (float) Math.cos(degree * MathUtils.DEGREE_2_RAD);
		float[][] value = { { cos, -sin, 0, 0 }, { sin, cos, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(value);
	}

	public static Matrix CreateTranslationMatrix(Coord translate)
	{
		float[][] value = { { 1, 0, 0, translate.x }, { 0, 1, 0, translate.y }, { 0, 0, 1, translate.z }, { 0, 0, 0, 1 } };
		return new Matrix(value);
	}

	public static Matrix CreateScaleMatrix(Coord scale)
	{
		float[][] value = { { scale.x, 0, 0, 0 }, { 0, scale.y, 0, 0 }, { 0, 0, scale.z, 0 }, { 0, 0, 0, 1 } };
		return new Matrix(value);
	}

	public void PutCamera(Camera camera) throws Exception
	{
		this.camera = camera;
		Stack<Matrix> temp = new Stack<Matrix>();
		for (; matlevel > XIW_IN_TOP;)
			temp.push(PopMatrix());

		PopMatrix(); // pop Xiw
		PopMatrix(); // pop Xpi
		PushMatrix(camera.getXpi()); // push Xpi
		PushMatrix(camera.getXiw()); // push Xiw

		while (!temp.isEmpty())
			PushMatrix(temp.pop());
	}

	public int getRenderClass()
	{
		return renderClass;
	}

	public Camera getCamera()
	{
		return camera;
	}

	public int getInterp_mode()
	{
		return interp_mode;
	}

	public void setInterp_mode(int interp_mode)
	{
		this.interp_mode = interp_mode;
	}

	public void setNumlights(int numlights)
	{
		this.numlights = numlights;
	}

	public void setAmbientlight(Light ambientlight)
	{
		this.ambientlight = ambientlight;
	}

	public void setKa(Color ka)
	{
		Ka = ka;
	}

	public void setKd(Color kd)
	{
		Kd = kd;
	}

	public void setKs(Color ks)
	{
		Ks = ks;
	}

	public void setSpec(float spec)
	{
		this.spec = spec;
	}

	public TextureFunction getTextureFunction()
	{
		return textureFunction;
	}

	public void setTextureFunction(TextureFunction textureFunction)
	{
		this.textureFunction = textureFunction;
	}

	public short getMatlevel()
	{
		return matlevel;
	}

	public void setAaOffset(float[][] aaOffset) throws Exception
	{
		if (aaOffset == null)
			throw new Exception("AAOffset value error");
		float w = 0;
		for (float[] value : aaOffset)
		{
			if (value == null || value.length != 3)
				throw new Exception("AAOffset value error");
			w += value[2];
		}
		if (w != 1)
			throw new Exception("AAOffset value error");
		this.aaOffset = aaOffset;
	}
}