package test;

import java.awt.image.BufferedImage;

import utils.ComUtils;
import utils.GUI.ResultWindow;

import myGL.Image;

public class TestReadTexture
{
	public static void main(String[] args) throws Exception
	{
		Image result = ComUtils.readTextureFile("00561.png");
		BufferedImage[] biList = new BufferedImage[1];
		biList[0] = ResultWindow.Display2BufferedImage(result);
		new ResultWindow(biList);
	}
}
