package myGL;

import utils.ComUtils;

public class Camera
{
	public Matrix Xiw = new Matrix(); // xform from world to image space
	public Matrix Xpi = new Matrix(); // perspective projection xform
	private Coord position = new Coord(-10, 5, -10, 1); // position of image plane origin, default is (-10, 5, -10, 1)
	private Coord lookat = new Coord(0, 0, 0, 1); // position of look-at-point, default is (0, 0, 0, 1)
	private Coord worldup = new Coord(0, 1, 0, 1); // world up-vector (almost screen up), default is (0, 1, 0, 1)
	private float FOV = 35; // horizontal field of view in degree value, default is 35

	public Camera() throws Exception
	{
		calculateXforms();
	}

	public Camera(Coord position, Coord lookat, Coord worldup, float FOV) throws Exception
	{
		this.position = position;
		this.lookat = lookat;
		this.worldup = worldup;
		this.FOV = FOV;
		calculateXforms();
	}

	private void calculateXforms() throws Exception
	{
		float[] Z = { lookat.x - position.x, lookat.y - position.y, lookat.z - position.z }, UP = { worldup.x, worldup.y, worldup.z };
		Z = ComUtils.Normalize(Z);
		float[] Y = ComUtils.Normalize(ComUtils.Plus(UP, ComUtils.Multiply(-ComUtils.Multiply(UP, Z), Z)));
		float[] X = { Y[1] * Z[2] - Y[2] * Z[1], Y[2] * Z[0] - Y[0] * Z[2], Y[0] * Z[1] - Y[1] * Z[0] };
		float[][] XiwValue = { { X[0], Y[0], Z[0], position.x }, { X[1], Y[1], Z[1], position.y }, { X[2], Y[2], Z[2], position.z }, { 0, 0, 0, 1 } };
		Xiw.value = XiwValue;

		float[][] XpiValue = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, (float) Math.tan(FOV * ComUtils.DEGREE_2_RAD / 2), 1 } };
		Xpi.value = XpiValue;
	}
}