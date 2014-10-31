package utils.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// TODO XwmWindow: finish construction of XwmWindow
public class XwmWindow extends JFrame
{
	private static final long serialVersionUID = 651888265597284508L;

	public static class XWActionListener implements ActionListener
	{
		private MainWindow parent;

		public XWActionListener(MainWindow parent)
		{
			this.parent = parent;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			new XwmWindow(parent);
		}
	}

	@SuppressWarnings("unused")
	private MainWindow parent;

	public XwmWindow(MainWindow parent)
	{
		this.parent = parent;

		JOptionPane.showMessageDialog(parent, "Under Construction", "error", JOptionPane.ERROR_MESSAGE);
	}
}
