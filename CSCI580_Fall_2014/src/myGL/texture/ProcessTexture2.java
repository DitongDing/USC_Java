package myGL.texture;

import myGL.Color;

public class ProcessTexture2 implements TextureFunction
{
	public Color getColor(float u, float v)
	{
		// clip u and v
		u = u < 0 ? 0 : (u > 1 ? 1 : u);
		v = v < 0 ? 0 : (v > 1 ? 1 : v);
		float size = 0.01f;
		float length = 150;
		float divide = 5f;
		for (int k = 0; k < length; k++)
			if (Math.abs(v - u * k * divide / length) < size)
				return new Color(Math.abs(v - u * k * divide / length) * 100, Math.abs(v - u * k * divide / length) * 100,
						Math.abs(v - u * k * divide / length) * 100);
		return new Color(0.9f, 0.9f, 0.9f);
	}
}
