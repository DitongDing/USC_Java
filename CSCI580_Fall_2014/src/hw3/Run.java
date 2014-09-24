package hw3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utils.MainGUI;
import utils.ResultWindow;

public class Run
{
	public static MainGUI gui;
	public static ResultWindow rw;
	public static String defaultInput = "";
	public static String defaultOutput = "";

	public static void main(String[] args)
	{
		gui = new MainGUI("homework3", 3);
		gui.inputPath.setText(defaultInput);
		gui.outputPath.setText(defaultOutput);
		gui.runRender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
			}
		});
	}
}