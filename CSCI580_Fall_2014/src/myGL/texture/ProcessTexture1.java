package myGL.texture;

import myGL.Color;

public class ProcessTexture1 implements TextureFunction
{
	public Color getColor(float u, float v)
	{
		// TODO Finish ProcessTexture1
		// clip u and v
		u = u < 0 ? 0 : (u > 1 ? 1 : u);
		v = v < 0 ? 0 : (v > 1 ? 1 : v);
		return null;
	}

}
