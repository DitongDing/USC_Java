package utils.final580;

import gl.Camera;
import gl.Coord;
import gl.Display;
import gl.Pixel;
import gl.Render;
import gl.Vertex;
import gl.texture.FileTexture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import utils.ComUtils;
import utils.GUI.ActionInput;
import utils.final580.MainWindow;

public class RunAnimation extends Thread
{
	private MainWindow gui;
	private Display display;
	private Pixel defaultPixel;
	private boolean paused = false;

	public void setPaused(boolean paused)
	{
		this.paused = paused;
	}

	public boolean getPaused()
	{
		return paused;
	}

	public RunAnimation(MainWindow gui, Display display, Pixel defaultPixel)
	{
		this.gui = gui;
		this.display = display;
		this.defaultPixel = defaultPixel;
	}

	public BufferedImage[] getBiList(ArrayList<Vertex[]> triList) throws Exception
	{
		Coord point = triList.get(0)[0];
		Coord lookat = new Coord(point.x, 0, point.z, 0);
		Coord worldup = new Coord(0, 1, 0, 1);
		Coord position = gui.actionManager.render.getCamera().getPosition();
		float FOV = gui.actionManager.render.getCamera().getFOV();
		Camera defaultCamera = new Camera(position, lookat, worldup, FOV);

		gui.actionManager.addAction(new ActionInput(ActionInput.CAMERA, ActionInput.WORLD, defaultCamera));

		ArrayList<BufferedImage> biList = new ArrayList<BufferedImage>();

		int frameNum = Final_Main.defaultFPS * Final_Main.cycleLength;
		double radStep = Math.PI * 2 / frameNum;

		for (int i = 0; i < frameNum; i++)
		{
			Coord newPosition = new Coord();
			double degree = radStep * i;
			newPosition.x = (float) ((position.x - lookat.x) * Math.cos(degree) - (position.z - lookat.z) * Math.sin(degree) + lookat.x);
			newPosition.y = 0;
			newPosition.z = (float) ((position.x - lookat.x) * Math.sin(degree) + (position.z - lookat.z) * Math.cos(degree) + lookat.z);
			newPosition.w = 0;
			Camera newCamera = new Camera(newPosition, lookat, worldup, FOV);

			gui.actionManager.addAction(new ActionInput(ActionInput.CAMERA, ActionInput.WORLD, newCamera));
			gui.actionManager.render.runRender(triList, display, defaultPixel);
			biList.add(ComUtils.Display2BufferedImage(display));
			gui.actionManager.deleteAction(0);
		}

		// For last frame. In case there is no action
		gui.actionManager.render.runRender(triList, display, defaultPixel);
		biList.add(ComUtils.Display2BufferedImage(display));

		return biList.toArray(new BufferedImage[0]);
	}

	public void run()
	{
		try
		{
			if (Final_Main.type == 0)
			{
				FileTexture texture = null;
				texture = new FileTexture("texture");
				gui.actionManager.render.setTextureFunction(texture);
			}

			ArrayList<Vertex[]> triList = ComUtils.readModelFile(Final_Main.inputFile);
			BufferedImage[][] re = new BufferedImage[4][];

			Render.TEST_TOON = true;
			re[0] = getBiList(triList);
			Render.TEST_TOON = false;

			Render.TEST_STIPPLING = true;
			re[1] = getBiList(triList);

			Render.TEST_STIPPLING_COLOR = true;
			re[2] = getBiList(triList);
			Render.TEST_STIPPLING_COLOR = false;
			Render.TEST_STIPPLING = false;

			Render.TEST_GOOCH = true;
			re[3] = getBiList(triList);
			Render.TEST_GOOCH = false;

			// TODO setBI for canvas
			gui.canvas.setBI(re);

			System.out.println("finish!");
			gui.changeLoading();

			while (true)
			{
				sleep(1000 / Final_Main.defaultFPS);
				if (!paused)
					gui.canvas.repaint();
			}
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(gui, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
