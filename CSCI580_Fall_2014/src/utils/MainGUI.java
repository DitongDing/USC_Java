package utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import myGL.CS580GL;
import myGL.Display;
import myGL.Matrix;
import myGL.Render;
import myGL.UIInput;

public class MainGUI extends JFrame
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
	public ArrayList<UIInput> inputLog;
	public ArrayList<Integer> cameraActionIndex;
	public short transformSize;

	public MainGUI(String title, int hwNumber)
	{
		super(title);
		inputLog = new ArrayList<UIInput>();
		cameraActionIndex = new ArrayList<Integer>();
		render = new Render();
		display = new Display();
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

		if (hwNumber >= 3)
		{
			// TODO object space TSR (given origin, give a message to show that the default orign is (0,0,0)), world space TSR, Camera, Animation(From, To, interval)
			// TODO action log: delete, add, move
			// TODO action definition: except camera, all of them have a default value. For camera, should find previous camera in Log
		}

		runRender = new JMenuItem("run");
		menuBar.add(runRender);

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
	// TODO consider to add Object space transform. Right now assume all of them is world space transformation
	public boolean addAction(CS580GL method, UIInput input) throws Exception
	{
		try
		{
			if (input.type != UIInput.CAMERA && transformSize == Render.MATLEVELS - 3) // 3 means Xsp, Xpi, Xiw already in Ximage
				throw new Exception("Render Xforms stack overflow");
			inputLog.add(input);
			Matrix matrix = null;
			
			// Pup until remains Xsp, Xpi and Xiw
			Stack<Matrix> temp = new Stack<Matrix>();
			for (; render.matlevel > 3;)
			{
				matrix = new Matrix();
				method.PopMatrix(render, matrix);
				temp.push(matrix);
			}

			if (input.type != UIInput.CAMERA)
			{
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
				{
					while (!temp.isEmpty())
						method.PushMatrix(render, temp.pop());
					throw new Exception("Add Action type error");
				}
				method.PushMatrix(render, matrix);
				transformSize++;
			}
			else
			{
				method.PutCamera(render, input.camera);
				method.PopMatrix(render, new Matrix()); // pop Xiw
				method.PopMatrix(render, new Matrix()); // pop Xpi
				method.PushMatrix(render, input.camera.Xpi); // push Xpi
				method.PushMatrix(render, input.camera.Xiw); // push Xiw
				cameraActionIndex.add(inputLog.size());
			}
			
			while (!temp.isEmpty())
				method.PushMatrix(render, temp.pop());
			inputLog.add(input);
			return true;
		}
		catch (Exception e)
		{
			System.out.println("Error in MainGUI.addAction()");
			e.printStackTrace();
			throw e;
		}
	}
}
