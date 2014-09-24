package utils;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class MainGUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private int GUIheight = 150;
	private int GUIwidth = 700;
	
	public JMenuBar menuBar;
	public JTextField inputPath;
	public JTextField outputPath;
	public JMenuItem runRender;
	
	public MainGUI(String title, int hwNumber)
	{
		super(title);
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
		
		if(hwNumber == 3)
		{
			// TODO
		}
		
		runRender = new JMenuItem("run");
		menuBar.add(runRender);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setLocation(5, menuBar.getSize().height);
		mainPanel.setSize(width-10, height - menuBar.getSize().height);
		mainPanel.setLayout(new GridLayout(2,1));
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
