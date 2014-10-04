package utils.GUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import myGL.CS580GL;
import myGL.Camera;
import myGL.Display;
import myGL.Matrix;
import myGL.Render;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private int GUIheight = 150;
	private int GUIwidth = 700;

	public Render render;
	public Display display;
	public JMenuBar menuBar;
	public JTextField inputPath;
	public JTextField outputPath;
	public JMenuItem runRender;
	public JMenuItem runAnimation;

	// For construction of Xwm
	public short XwmSize;
	public ArrayList<UIInput> XwmList;

	// For all actions, including transformation in object space, camera change
	public short transformSize;
	public ArrayList<UIInput> actionList;

	public MainWindow(String title, int hwNumber)
	{
		super(title);
		XwmList = new ArrayList<UIInput>();
		actionList = new ArrayList<UIInput>();
		render = new Render();
		display = new Display();
		XwmSize = 0;
		transformSize = 0;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(GUIwidth, GUIheight);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		int height = this.getContentPane().getSize().height;
		int width = this.getContentPane().getSize().width;

		menuBar = new JMenuBar();
		menuBar.setLocation(0, 0);
		menuBar.setSize(width, 20);
		add(menuBar);

		JMenu run = new JMenu("run");
		runRender = new JMenuItem("run render");
		run.add(runRender);

		if (hwNumber >= 3)
		{
			JMenu edit = new JMenu("edit");
			JMenuItem editXwm = new JMenuItem("edit Xwm"), editAction = new JMenuItem("edit action");
			editXwm.addActionListener(new XwmWindow.XWActionListener(this));
			editAction.addActionListener(new ActionWindow.AWActionListener(this));
			edit.add(editXwm);
			edit.add(editAction);
			menuBar.add(edit);
			// TODO object space TSR (given origin, give a message to show that the default orign is (0,0,0)), world space TSR, Camera, Animation(From, To, interval)
			// TODO action log: delete, add, edit
			// TODO action definition: except camera, all of them have a default value. For camera, should find previous camera in Log
			runAnimation = new JMenuItem("run animation");
			run.add(runAnimation);
		}

		menuBar.add(run);

		JPanel mainPanel = new JPanel();
		mainPanel.setLocation(5, menuBar.getSize().height);
		mainPanel.setSize(width - 10, height - menuBar.getSize().height);
		mainPanel.setLayout(new GridLayout(2, 1));
		add(mainPanel);

		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder("input path"));
		mainPanel.add(inputPanel);

		inputPath = new JTextField(60);
		inputPanel.add(inputPath);

		JPanel outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder("output path"));
		mainPanel.add(outputPanel);

		outputPath = new JTextField(60);
		outputPanel.add(outputPath);

		setVisible(true);
	}

	// Add later action to actionlist
	public boolean addAction(CS580GL method, UIInput input) throws Exception
	{
		try
		{
			if (input.type != UIInput.CAMERA && XwmSize + transformSize == Render.MATLEVELS - 3) // 3 means Xsp, Xpi, Xiw already in Ximage
				throw new Exception("Render Xforms stack overflow");

			if (input.type != UIInput.CAMERA)
			{
				// Pop until remains Xsp, Xpi and Xiw
				Stack<Matrix> temp = new Stack<Matrix>();
				Matrix matrix = null;

				matrix = new Matrix();
				if (input.type == UIInput.ROTATION_X)
					method.CreateRotationByXMatrix(input.rotation.x, matrix);
				else if (input.type == UIInput.ROTATION_Y)
					method.CreateRotationByYMatrix(input.rotation.y, matrix);
				else if (input.type == UIInput.ROTATION_Z)
					method.CreateRotationByZMatrix(input.rotation.z, matrix);
				else if (input.type == UIInput.TRANSLATION)
					method.CreateTranslationMatrix(input.translation, matrix);
				else if (input.type == UIInput.SCALE)
					method.CreateScaleMatrix(input.scale, matrix);
				else
					throw new Exception("input type error");

				for (; render.matlevel > 3 + XwmSize;)
				{
					Matrix matrixTemp = new Matrix();
					method.PopMatrix(render, matrixTemp);
					temp.push(matrixTemp);
				}
				if (input.space == UIInput.WORLD)
				{
					for (; render.matlevel > 3;)
					{
						Matrix matrixTemp = new Matrix();
						method.PopMatrix(render, matrixTemp);
						temp.push(matrixTemp);
					}
					XwmSize++;
					XwmList.add(input);
				}
				else
				{
					transformSize++;
					actionList.add(input);
				}
				method.PushMatrix(render, matrix);
				while (!temp.isEmpty())
					method.PushMatrix(render, temp.pop());
			}
			else
			{
				method.PutCamera(render, input.camera);
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
	public boolean editAction(CS580GL method, int index, double newTimePeriod) throws Exception
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

	// TODO MainWindow.deleteAction()
	// 0-based index in actionList or XwmList
	public boolean deleteAction(CS580GL method, int index) throws Exception
	{
		try
		{
			UIInput action = actionList.get(index);
			// Delete camere change action
			if (action.type == UIInput.CAMERA)
			{
				Camera prevCamera = null;
				ListIterator<UIInput> li = actionList.listIterator(index);
				while (li.hasPrevious())
				{
					UIInput input = li.previous();
					if (input.type == UIInput.CAMERA)
					{
						prevCamera = input.camera;
						break;
					}
				}
				if (prevCamera == null)
					prevCamera = new Camera();
				method.PutCamera(render, prevCamera);
				actionList.remove(index);
			}
			else
			{
				// Delete Xwm construction action
				// TODO MainWindow.deleteAction(): finish the part of delete Xwm construction action
				if (action.space == UIInput.WORLD)
				{

				}
				// Delete object space transformation
				else
				{
					ListIterator<UIInput> li = actionList.listIterator(index);
					int level = 3 + XwmSize+transformSize - index;
					while (li.hasPrevious())
						if (li.previous().type == UIInput.CAMERA)
							level++;

					Stack<Matrix> temp = new Stack<Matrix>();
					for (; render.matlevel > level;)
					{
						Matrix matrixTemp = new Matrix();
						method.PopMatrix(render, matrixTemp);
						temp.push(matrixTemp);
					}
					method.PopMatrix(render, new Matrix());
					while (!temp.isEmpty())
						method.PushMatrix(render, temp.pop());
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
