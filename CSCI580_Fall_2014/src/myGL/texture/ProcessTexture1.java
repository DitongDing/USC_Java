package myGL.texture;

import myGL.Color;

public class ProcessTexture1 implements TextureFunction
{
	public Color getColor(float u, float v)
	{
		// clip u and v
		u = u < 0 ? 0 : (u > 1 ? 1 : u);
		v = v < 0 ? 0 : (v > 1 ? 1 : v);
		int number = 13;
		int total = (int) (u * number) + (int) (v * number);
		if (total % 2 == 0)
			return new Color(0.1f, 0.1f, 0.1f);
		return new Color((u * v + (float) Math.sin(v)) / 2, (float) Math.pow(u, v), u * u);
	}
}