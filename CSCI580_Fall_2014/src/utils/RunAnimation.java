package utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JOptionPane;

import myGL.Camera;
import myGL.Coord;
import myGL.Display;
import myGL.Pixel;
import myGL.Vertex;
import utils.GUI.ActionInput;
import utils.GUI.MainWindow;
import utils.GUI.ResultWindow;

public class RunAnimation extends Thread
{
	private MainWindow gui;
	private Display display;
	private Pixel defaultPixel;

	public RunAnimation(MainWindow gui, Display display, Pixel defaultPixel)
	{
		this.gui = gui;
		this.display = display;
		this.defaultPixel = defaultPixel;
	}

	public void run()
	{
		try
		{
			ArrayList<Vertex[]> triList = ComUtils.readASCFile(gui.inputPath.getText());
			ArrayList<BufferedImage> biList = new ArrayList<BufferedImage>();

			ArrayList<ActionInput> actionList = new ArrayList<ActionInput>(gui.actionList);

			// Remove all action for using addAction and deleteAction
			for (int i = 0; i < actionList.size(); i++)
				gui.deleteAction(0);

			// Render frames for every action and recover actionlist
			int count = 0;
			for (ActionInput action : actionList)
			{
				int frameNum = (int) (action.period * ResultWindow.defaultFPS);

				// Get previous camera
				Camera prevCamera = null;
				ListIterator<ActionInput> li = actionList.listIterator(count);
				while (li.hasPrevious())
				{
					ActionInput input = li.previous();
					if (input.type == ActionInput.CAMERA)
					{
						prevCamera = input.camera;
						break;
					}
				}
				if (prevCamera == null)
					prevCamera = new Camera();

				for (int j = 0; j < frameNum; j++)
				{
					ActionInput input = null;
					if (action.type == ActionInput.ROTATION_X || action.type == ActionInput.ROTATION_Y || action.type == ActionInput.ROTATION_Z)
						input = new ActionInput(action.type, action.space, (Object) Coord.interpolateCoord(new Coord(0, 0, 0, 0), action.rotation, j
								/ (float) frameNum));
					else if (action.type == ActionInput.TRANSLATION)
						input = new ActionInput(action.type, action.space, (Object) Coord.interpolateCoord(new Coord(0, 0, 0, 0), action.translation, j
								/ (float) frameNum));
					else if (action.type == ActionInput.SCALE)
						input = new ActionInput(action.type, action.space, (Object) Coord.interpolateCoord(new Coord(1, 1, 1, 0), action.scale, j
								/ (float) frameNum));
					else if (action.type == ActionInput.CAMERA)
					{
						Camera thisCamera = action.camera;
						input = new ActionInput(action.type, action.space, (Object) new Camera(Coord.interpolateCoord(prevCamera.getPosition(),
								thisCamera.getPosition(), j / (float) frameNum), Coord.interpolateCoord(prevCamera.getLookat(), thisCamera.getLookat(), j
								/ (float) frameNum), Coord.interpolateCoord(prevCamera.getWorldup(), thisCamera.getWorldup(), j / (float) frameNum),
								MathUtils.interpolateFloat(prevCamera.getFOV(), thisCamera.getFOV(), j / (float) frameNum)));
					}
					else
						throw new Exception("action type error in run animation");
					gui.addAction(input);
					gui.render.runRender(triList, display, defaultPixel);
					biList.add(ResultWindow.Display2BufferedImage(display));
					gui.deleteAction(count);
				}
				// assume last frame for every action is the previous action, for recovering actionlist.
				gui.addAction(action);
				gui.render.runRender(triList, display, defaultPixel);
				biList.add(ResultWindow.Display2BufferedImage(display));
				count++;
			}

			// For last frame. In case there is no action
			gui.render.runRender(triList, display, defaultPixel);
			biList.add(ResultWindow.Display2BufferedImage(display));

			ResultWindow rw = new ResultWindow(biList.toArray(new BufferedImage[0]));
			for (int i = 1; i < biList.size(); i++)
			{
				sleep(1000 / ResultWindow.defaultFPS);
				rw.repaint();
			}

			// Clean up and exit

			System.out.println("finish!");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(gui, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
