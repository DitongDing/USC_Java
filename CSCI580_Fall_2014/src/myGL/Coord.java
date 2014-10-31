package myGL;

import utils.MathUtils;

public class Coord
{
	public float x;
	public float y;
	public float z;
	public float w;

	public Coord()
	{
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}

	public Coord(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Coord(float[] value)
	{
		this();
		if (value != null && value.length == 4)
		{
			this.x = value[0];
			this.y = value[1];
			this.z = value[2];
			this.w = value[3];
		}
	}

	// Return matrix represent.
	public float[][] getMatrix()
	{
		float[][] re = { { x }, { y }, { z }, { w } };
		return re;
	}

	// Return vector represent.
	public float[] getVector()
	{
		float[] re = { x, y, z, w };
		return re;
	}

	public static Coord interpolateCoord(Coord start, Coord end, float progress)
	{
		return new Coord(MathUtils.interpolateFloat(start.x, end.x, progress), MathUtils.interpolateFloat(start.y, end.y, progress),
				MathUtils.interpolateFloat(start.z, end.z, progress), MathUtils.interpolateFloat(start.w, end.w, progress));
	}
}
