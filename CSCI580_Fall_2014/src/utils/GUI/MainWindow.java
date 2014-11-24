package utils.GUI;

import gl.Render;
import gl.texture.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import utils.ActionManager;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private int GUIheight = 150;
	private int GUIwidth = 700;

	public JMenuBar menuBar;
	public JTextField inputPath;
	public JTextField outputPath;
	public JMenuItem runRender;
	public JMenuItem runAnimation;
	public JRadioButtonMenuItem noTexture;

	public ActionManager actionManager;

	public MainWindow(String title) throws Exception
	{
		super(title);
		actionManager = new ActionManager();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(GUIwidth, GUIheight);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		int height = this.getContentPane().getSize().height;
		int width = this.getContentPane().getSize().width;

		// For setting menu
		menuBar = new JMenuBar();
		menuBar.setLocation(0, 0);
		menuBar.setSize(width, 20);
		add(menuBar);

		// MenuItem Edit
		JMenu edit = new JMenu("edit");
		JMenuItem editXwm = new JMenuItem("Edit Xwm"), editAction = new JMenuItem("Edit action");
		editXwm.addActionListener(new XwmWindow.XWActionListener(this));
		editAction.addActionListener(new ActionWindow.AWActionListener(this));
		edit.add(editXwm);
		edit.add(editAction);
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
				actionManager.render.setInterp_mode(Render.FLAT);
			}
		});
		gouraud.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				actionManager.render.setInterp_mode(Render.COLOR);
			}
		});
		phong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				actionManager.render.setInterp_mode(Render.NORMALS);
			}
		});

		ButtonGroup bgI = new ButtonGroup();
		bgI.add(flat);
		bgI.add(gouraud);
		bgI.add(phong);
		if (actionManager.render.getInterp_mode() == Render.FLAT)
			flat.setSelected(true);
		else if (actionManager.render.getInterp_mode() == Render.COLOR)
			gouraud.setSelected(true);
		else if (actionManager.render.getInterp_mode() == Render.NORMALS)
			phong.setSelected(true);
		else
			throw new Exception("Render default color interpolate mode error");

		interpStyle.add(flat);
		interpStyle.add(gouraud);
		interpStyle.add(phong);
		edit.add(interpStyle);

		edit.addSeparator();
		JMenu editTexture = new JMenu("Edit Texture");
		noTexture = new JRadioButtonMenuItem("No texture");
		JRadioButtonMenuItem fileTexture = new JRadioButtonMenuItem("File texture");
		JRadioButtonMenuItem processTexture1 = new JRadioButtonMenuItem("Process texture - chess board");
		JRadioButtonMenuItem processTexture2 = new JRadioButtonMenuItem("Process texture - simple ray");

		noTexture.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				actionManager.render.setTextureFunction(null);
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
				actionManager.render.setTextureFunction(texture);
				if (texture == null)
					noTexture.setSelected(true);
			}
		});
		processTexture1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				actionManager.render.setTextureFunction(new ProcessTexture1());
			}
		});
		processTexture2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				actionManager.render.setTextureFunction(new ProcessTexture2());
			}
		});

		ButtonGroup bgT = new ButtonGroup();
		bgT.add(noTexture);
		bgT.add(fileTexture);
		bgT.add(processTexture1);
		bgT.add(processTexture2);
		if (actionManager.render.getTextureFunction() == null)
			noTexture.setSelected(true);
		else if (actionManager.render.getTextureFunction() instanceof FileTexture)
			fileTexture.setSelected(true);
		else if (actionManager.render.getTextureFunction() instanceof ProcessTexture1)
			processTexture1.setSelected(true);
		else if (actionManager.render.getTextureFunction() instanceof ProcessTexture2)
			processTexture2.setSelected(true);
		else
			throw new Exception("Render default texture function error");

		editTexture.add(noTexture);
		editTexture.add(fileTexture);
		editTexture.add(processTexture1);
		editTexture.add(processTexture2);
		edit.add(editTexture);

		menuBar.add(edit);

		// MenuItem Run.
		JMenu run = new JMenu("run");
		runRender = new JMenuItem("run render");
		run.add(runRender);
		runAnimation = new JMenuItem("run animation");
		run.add(runAnimation);
		menuBar.add(run);

		// Type in panel
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
}
