package myGL.texture;

import utils.ComUtils;
import myGL.CS580GL;
import myGL.Color;
import myGL.Image;

public class FileTexture implements TextureFunction
{
	private Image textureImage = null;

	public FileTexture(CS580GL method, String filename) throws Exception
	{
		textureImage = ComUtils.readTextureFile(method, filename);
	}

	public Color getColor(float u, float v)
	{
		Color result = null;
		// clip u and v
		u = u < 0 ? 0 : (u > 1 ? 1 : u);
		v = v < 0 ? 0 : (v > 1 ? 1 : v);
		float x = u * (textureImage.xres - 1);
		float y = v * (textureImage.yres - 1);
		int x1 = (int) x;
		int y1 = (int) y;
		if (x == x1 && y == y1)
			result = new Color(textureImage.getPixel(x1, y1), textureImage.global_max);
		else if (x == x1)
		{
			// y interpolate
			Color start = new Color(textureImage.getPixel(x1, y1), textureImage.global_max);
			Color end = new Color(textureImage.getPixel(x1, y1 + 1), textureImage.global_max);
			result = Color.interpolateColor(start, end, y - y1);
		}
		else if (y == y1)
		{
			// x interpolate
			Color start = new Color(textureImage.getPixel(x1, y1), textureImage.global_max);
			Color end = new Color(textureImage.getPixel(x1 + 1, y1), textureImage.global_max);
			result = Color.interpolateColor(start, end, x - x1);
		}
		else
		{
			// x interpolate then y interpolate
			Color start = Color.interpolateColor(new Color(textureImage.getPixel(x1, y1), textureImage.global_max), new Color(
					textureImage.getPixel(x1 + 1, y1), textureImage.global_max), x - x1);
			Color end = Color.interpolateColor(new Color(textureImage.getPixel(x1, y1 + 1), textureImage.global_max),
					new Color(textureImage.getPixel(x1 + 1, y1 + 1), textureImage.global_max), x - x1);
			result = Color.interpolateColor(start, end, y - y1);
		}

		return result;
	}
}
