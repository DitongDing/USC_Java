package utils.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// TODO LightWindow: finish construction of LightWindow. Do not forget to edit MainWindow to add light log
public class LightWindow extends JFrame
{
	private static final long serialVersionUID = 2965669837557481504L;

	public static class LWActionListener implements ActionListener
	{
		private MainWindow parent;

		public LWActionListener(MainWindow parent)
		{
			this.parent = parent;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			new LightWindow(parent);
		}
	}

	@SuppressWarnings("unused")
	private MainWindow parent;

	public LightWindow(MainWindow parent)
	{
		this.parent = parent;

		JOptionPane.showMessageDialog(parent, "Under Construction", "error", JOptionPane.ERROR_MESSAGE);
	}
}
