package gl.texture;

import gl.Color;

public interface TextureFunction
{
	public Color getColor(float u, float v);
}