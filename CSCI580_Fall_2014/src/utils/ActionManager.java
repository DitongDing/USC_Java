package utils;

import gl.Camera;
import gl.Matrix;
import gl.Render;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import utils.GUI.ActionInput;

public class ActionManager
{
	public Render render;

	// For construction of Xwm
	public short XwmSize;
	public ArrayList<ActionInput> XwmList;

	// For all actions, including transformation in object space, camera change
	public short transformSize;
	public ArrayList<ActionInput> actionList;

	public ActionManager() throws Exception
	{
		XwmList = new ArrayList<ActionInput>();
		actionList = new ArrayList<ActionInput>();
		XwmSize = 0;
		transformSize = 0;
		render = new Render(Render.Z_BUFFER_RENDER);
	}

	// Add later action to actionlist
	public boolean addAction(ActionInput input) throws Exception
	{
		try
		{
			// XIW_IN_TOP means Xpi, Xiw already in Ximage
			if (input.type != ActionInput.CAMERA && XwmSize + transformSize == Render.MATLEVELS - Render.XIW_IN_TOP)
				throw new Exception("Render Xforms stack overflow");

			if (input.type != ActionInput.CAMERA)
			{
				// Pop until remains Xsp, Xpi and Xiw
				Stack<Matrix> temp = new Stack<Matrix>();
				Matrix matrix = null;

				if (input.type == ActionInput.ROTATION_X)
					matrix = Render.CreateRotationByXMatrix(input.rotation.x);
				else if (input.type == ActionInput.ROTATION_Y)
					matrix = Render.CreateRotationByYMatrix(input.rotation.y);
				else if (input.type == ActionInput.ROTATION_Z)
					matrix = Render.CreateRotationByZMatrix(input.rotation.z);
				else if (input.type == ActionInput.TRANSLATION)
					matrix = Render.CreateTranslationMatrix(input.translation);
				else if (input.type == ActionInput.SCALE)
					matrix = Render.CreateScaleMatrix(input.scale);
				else
					throw new Exception("input type error");

				for (; render.getMatlevel() > Render.XIW_IN_TOP + XwmSize;)
					temp.push(render.PopMatrix());
				// TODO ActionManager.addAction(): finish the part of add Xwm construction action
				if (input.space == ActionInput.WORLD)
				{
					for (; render.getMatlevel() > Render.XIW_IN_TOP;)
						temp.push(render.PopMatrix());
					XwmSize++;
					XwmList.add(input);
				}
				else
				{
					transformSize++;
					actionList.add(input);
				}
				render.PushMatrix(matrix);
				while (!temp.isEmpty())
					render.PushMatrix(temp.pop());
			}
			else
			{
				render.PutCamera(input.camera);
				actionList.add(input);
			}

			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in MainGUI.addAction():" + e.getMessage());
			throw e;
		}
	}

	// Only permit to edit time period
	public boolean editAction(int index, double newTimePeriod) throws Exception
	{
		try
		{
			actionList.get(index).period = newTimePeriod;
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in MainGUI.editAction()");
			e.printStackTrace();
			throw e;
		}
	}

	// 0-based index in actionList or XwmList
	public boolean deleteAction(int index) throws Exception
	{
		try
		{
			ActionInput action = actionList.get(index);
			// Delete camere change action
			if (action.type == ActionInput.CAMERA)
			{
				actionList.remove(index);
				Camera prevCamera = null;
				ListIterator<ActionInput> li = actionList.listIterator(actionList.size());
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
				render.PutCamera(prevCamera);
			}
			else
			{
				// Delete Xwm construction action
				// TODO ActionManager.deleteAction(): finish the part of delete Xwm construction action
				if (action.space == ActionInput.WORLD)
				{

				}
				// Delete object space transformation
				else
				{
					ListIterator<ActionInput> li = actionList.listIterator(index);
					// As the index is in a reversed order of stack, so use the following euqation.
					int level = Render.XIW_IN_TOP + XwmSize + transformSize - index;
					while (li.hasPrevious())
						if (li.previous().type == ActionInput.CAMERA)
							level++;

					Stack<Matrix> temp = new Stack<Matrix>();
					for (; render.getMatlevel() > level;)
						temp.push(render.PopMatrix());
					render.PopMatrix();
					while (!temp.isEmpty())
						render.PushMatrix(temp.pop());
					actionList.remove(index);
					transformSize--;
				}
			}
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in MainGUI.deleteAction()");
			e.printStackTrace();
			throw e;
		}
	}
}
