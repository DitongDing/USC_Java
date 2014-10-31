package utils.GUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import myGL.Camera;
import myGL.Matrix;
import myGL.Render;
import myGL.texture.*;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private int GUIheight = 150;
	private int GUIwidth = 700;

	public Render render;
	public JMenuBar menuBar;
	public JTextField inputPath;
	public JTextField outputPath;
	public JMenuItem runRender;
	public JMenuItem runAnimation;
	public JRadioButtonMenuItem noTexture;

	// For construction of Xwm
	public short XwmSize;
	public ArrayList<ActionInput> XwmList;

	// For all actions, including transformation in object space, camera change
	public short transformSize;
	public ArrayList<ActionInput> actionList;

	public MainWindow(String title, int hwNumber) throws Exception
	{
		super(title);
		XwmList = new ArrayList<ActionInput>();
		actionList = new ArrayList<ActionInput>();
		render = new Render(Render.Z_BUFFER_RENDER, hwNumber);
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
			JMenuItem editXwm = new JMenuItem("Edit Xwm"), editAction = new JMenuItem("Edit action");
			editXwm.addActionListener(new XwmWindow.XWActionListener(this));
			editAction.addActionListener(new ActionWindow.AWActionListener(this));
			edit.add(editXwm);
			edit.add(editAction);

			if (hwNumber >= 4)
			{
				edit.addSeparator();
				JMenuItem editLight = new JMenuItem("Edit Light");
				editLight.addActionListener(new LightWindow.LWActionListener(this));
				edit.add(editLight);
				JMenu interpStyle = new JMenu("Color Style");
				JRadioButtonMenuItem flat = new JRadioButtonMenuItem("Flat shading");
				JRadioButtonMenuItem gouraud = new JRadioButtonMenuItem("Gouraud shading");
				JRadioButtonMenuItem phong = new JRadioButtonMenuItem("Phong shading");

				flat.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						render.setInterp_mode(Render.FLAT);
					}
				});
				gouraud.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						render.setInterp_mode(Render.COLOR);
					}
				});
				phong.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						render.setInterp_mode(Render.NORMALS);
					}
				});

				ButtonGroup bgI = new ButtonGroup();
				bgI.add(flat);
				bgI.add(gouraud);
				bgI.add(phong);
				if (render.getInterp_mode() == Render.FLAT)
					flat.setSelected(true);
				else if (render.getInterp_mode() == Render.COLOR)
					gouraud.setSelected(true);
				else if (render.getInterp_mode() == Render.NORMALS)
					phong.setSelected(true);
				else
					throw new Exception("Render default color interpolate mode error");

				interpStyle.add(flat);
				interpStyle.add(gouraud);
				interpStyle.add(phong);
				edit.add(interpStyle);

				if (hwNumber >= 5)
				{
					edit.addSeparator();
					JMenu editTexture = new JMenu("Edit Texture");
					noTexture = new JRadioButtonMenuItem("No texture");
					JRadioButtonMenuItem fileTexture = new JRadioButtonMenuItem("File texture");
					JRadioButtonMenuItem processTexture1 = new JRadioButtonMenuItem("Process texture - chess board");
					JRadioButtonMenuItem processTexture2 = new JRadioButtonMenuItem("Process texture - simple ray");

					noTexture.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0)
						{
							render.setTextureFunction(null);
						}
					});
					fileTexture.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0)
						{
							FileTexture texture = null;
							try
							{
								texture = new FileTexture(JOptionPane.showInputDialog(null, "Please input texture file path", "texture"));
							}
							catch (Exception e)
							{
								JOptionPane.showMessageDialog(null, "Texture file read error, reset to no texture", "error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							render.setTextureFunction(texture);
							if (texture == null)
								noTexture.setSelected(true);
						}
					});
					processTexture1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0)
						{
							render.setTextureFunction(new ProcessTexture1());
						}
					});
					processTexture2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0)
						{
							render.setTextureFunction(new ProcessTexture2());
						}
					});

					ButtonGroup bgT = new ButtonGroup();
					bgT.add(noTexture);
					bgT.add(fileTexture);
					bgT.add(processTexture1);
					bgT.add(processTexture2);
					if (render.getTextureFunction() == null)
						noTexture.setSelected(true);
					else if (render.getTextureFunction() instanceof FileTexture)
						fileTexture.setSelected(true);
					else if (render.getTextureFunction() instanceof ProcessTexture1)
						processTexture1.setSelected(true);
					else if (render.getTextureFunction() instanceof ProcessTexture2)
						processTexture2.setSelected(true);
					else
						throw new Exception("Render default texture function error");

					editTexture.add(noTexture);
					editTexture.add(fileTexture);
					editTexture.add(processTexture1);
					editTexture.add(processTexture2);
					edit.add(editTexture);
				}
			}

			menuBar.add(edit);
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
				// TODO MainWindow.addAction(): finish the part of add Xwm construction action
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
				// TODO MainWindow.deleteAction(): finish the part of delete Xwm construction action
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
