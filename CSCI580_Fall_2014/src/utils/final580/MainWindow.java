package utils.final580;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import utils.ActionManager;

public class MainWindow extends JFrame
{
	private static final long serialVersionUID = 2189432609940965380L;
	private JLabel teamName;
	private JLabel modelLabel;
	private JLabel resultLabel;
	public GraphCanvas canvas;
	private JButton start;
	private JButton pause;
	@SuppressWarnings("rawtypes")
	private JComboBox mode;
	public ActionManager actionManager;
	public RunAnimation runAnimation;

	public MainWindow() throws Exception
	{
		super();
		actionManager = new ActionManager();

		setSize(950, 650);
		getContentPane().setLayout(null);// 设置布局控制器

		add(getTeamName(), null);// 添加标签
		add(getModelLabel(), null);
		add(getResultLabel(), null);
		add(getCanvas(), null);
		add(getMode(), null);// 添加下拉列表框
		add(getStart(), null);
		add(getPause(), null);
		setTitle("Muse");// 设置窗口标题

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private JButton getStart()
	{
		if (start == null)
		{
			start = new JButton();
			start.setBounds(220, 400, 80, 27);
			start.setText("Start");
			start.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					runAnimation.setPaused(false);
				}
			});
		}
		return start;
	}

	private JButton getPause()
	{
		if (pause == null)
		{
			pause = new JButton();
			pause.setBounds(120, 400, 80, 27);
			pause.setText("Pause");
			pause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					runAnimation.setPaused(true);
				}
			});
		}
		return pause;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private JComboBox getMode()
	{
		if (mode == null)
		{
			mode = new JComboBox();
			mode.setBounds(190, 152, 120, 27);
			mode.addItem("Toon Shading");
			mode.addItem("Stippling Shading");
			mode.addItem("Stippling Shading-Color");
			mode.addItem("Gooch Shading");
			mode.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0)
				{
					JComboBox o = (JComboBox) arg0.getSource();
					canvas.setType(o.getSelectedIndex());
					if(runAnimation.getPaused())
					{
						canvas.go = false;
						canvas.repaint();
					}
				}
			});

		}
		return mode;
	}

	private JLabel getTeamName()
	{
		if (teamName == null)
		{
			teamName = new JLabel();
			teamName.setBounds(30, 30, 330, 30);
			teamName.setText("Muse - CS580 team project");
			teamName.setFont(new Font("symbo", Font.ITALIC, 25));
			teamName.setToolTipText("JLabel");
		}
		return teamName;
	}

	private JLabel getModelLabel()
	{
		if (modelLabel == null)
		{
			modelLabel = new JLabel();
			modelLabel.setBounds(60, 150, 130, 30);
			modelLabel.setText("Mode");
			modelLabel.setFont(new Font("symbo", Font.ITALIC, 22));
			modelLabel.setToolTipText("JLabel");
		}
		return modelLabel;
	}

	private JLabel getResultLabel()
	{
		if (resultLabel == null)
		{
			resultLabel = new JLabel();
			resultLabel.setBounds(400, 30, 230, 30);
			resultLabel.setText("Result-Loading");
			resultLabel.setFont(new Font("symbo", Font.ITALIC, 22));
			resultLabel.setToolTipText("JLabel");
		}
		return resultLabel;
	}

	private GraphCanvas getCanvas()
	{
		if (canvas == null)
		{
			canvas = new GraphCanvas();
			canvas.setBounds(380, 70, 512, 512);
		}
		return canvas;
	}
	
	public void changeLoading()
	{
		resultLabel.setText("Result");
	}
}
