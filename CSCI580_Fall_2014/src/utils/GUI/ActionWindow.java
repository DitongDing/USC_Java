package utils.GUI;

import hw3.Run;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import myGL.Camera;
import myGL.Coord;

public class ActionWindow extends JFrame
{
	private static final long serialVersionUID = 6391527580602385909L;

	private int GUIheight = 200;
	private int GUIwidth = 800;
	private JTable table;

	public static class AWActionListener implements ActionListener
	{
		private MainWindow parent;

		public AWActionListener(MainWindow parent)
		{
			this.parent = parent;
		}

		public void actionPerformed(ActionEvent arg0)
		{
			new ActionWindow(parent);
		}
	}

	private MainWindow parent;

	public ActionWindow(MainWindow p)
	{
		super("Action Window");

		this.parent = p;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(GUIwidth, GUIheight);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		int height = this.getContentPane().getSize().height;
		int width = this.getContentPane().getSize().width;

		JPanel mainPanel = new JPanel();
		mainPanel.setLocation(5, 0);
		mainPanel.setSize(width - 10, height);
		mainPanel.setLayout(null);
		add(mainPanel);

		JPanel listPanel = new JPanel();
		listPanel.setLocation(0, 0);
		listPanel.setSize((int) (mainPanel.getSize().width * 0.65), mainPanel.getSize().height);
		listPanel.setBorder(new TitledBorder("list panel"));
		mainPanel.add(listPanel);

		String[] columnNames = { "#", "type", "descreption", "period" };
		table = new JTable(new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		});
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		for (String columnName : columnNames)
			tableModel.addColumn(columnName);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(listPanel.getSize().width - 16, listPanel.getSize().height - 35));
		listPanel.add(scrollPane);

		// Set width
		int[] colswidth = { 20, 50, scrollPane.getPreferredSize().width - 120, 50 };
		for (int i = 0; i < columnNames.length; i++)
		{
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setPreferredWidth(colswidth[i]);
			column.setMaxWidth(colswidth[i]);
			column.setMinWidth(colswidth[i]);
		}

		refreshTable();

		JPanel controlPanel = new JPanel();
		controlPanel.setLocation(listPanel.getSize().width, 0);
		controlPanel.setSize((int) (mainPanel.getSize().width * 0.35), mainPanel.getSize().height);
		controlPanel.setBorder(new TitledBorder("control panel"));
		controlPanel.setLayout(new GridLayout(3, 2));
		mainPanel.add(controlPanel);

		JButton AddRotation = new JButton("Add Rotation");
		JButton AddTranslation = new JButton("Add Translation");
		JButton AddScale = new JButton("Add Scale");
		JButton AddCamera = new JButton("Add Camera");
		JButton EditPeriod = new JButton("Edit Period");
		JButton Delete = new JButton("Delete");

		AddRotation.addActionListener(new ActionListener() {
			JFrame frame;
			JTextField value;
			JRadioButton X;
			JRadioButton Y;
			JRadioButton Z;

			public void actionPerformed(ActionEvent arg0)
			{
				frame = new JFrame("Add Rotation");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(450, 150);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);

				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new GridLayout(3, 1));
				frame.add(mainPanel);

				JPanel panel1 = new JPanel();
				mainPanel.add(panel1);
				JPanel panel2 = new JPanel();
				panel2.setLayout(new GridLayout(1, 3));
				mainPanel.add(panel2);
				JPanel panel3 = new JPanel();
				panel3.setLayout(new GridLayout(1, 2));
				mainPanel.add(panel3);

				value = new JTextField(30);
				value.setText("0");
				panel1.add(value);

				X = new JRadioButton("X");
				X.setSelected(true);
				Y = new JRadioButton("Y");
				Z = new JRadioButton("Z");
				ButtonGroup bg = new ButtonGroup();
				bg.add(X);
				bg.add(Y);
				bg.add(Z);
				panel2.add(X);
				panel2.add(Y);
				panel2.add(Z);

				JButton Enter = new JButton("Enter");
				Enter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						try
						{
							float fvalue = Float.parseFloat(value.getText());
							short type;
							Coord rotation;
							if (X.isSelected())
							{
								type = UIInput.ROTATION_X;
								rotation = new Coord(fvalue, 0, 0, 0);
							}
							else if (Y.isSelected())
							{
								type = UIInput.ROTATION_Y;
								rotation = new Coord(0, fvalue, 0, 0);
							}
							else
							{
								type = UIInput.ROTATION_Z;
								rotation = new Coord(0, 0, fvalue, 0);
							}
							UIInput input = new UIInput(type, UIInput.OBJECT, rotation);
							parent.addAction(Run.method, input);
							frame.dispose();
							refreshTable();
						}
						catch (Exception e)
						{
							System.out.println("Error in GUI->Rotate->Enter");
							JOptionPane.showMessageDialog(frame, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				JButton Cancel = new JButton("Cancel");
				Cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						frame.dispose();
					}
				});
				panel3.add(Enter);
				panel3.add(Cancel);
			}
		});
		AddTranslation.addActionListener(new ActionListener() {
			JFrame frame;
			JTextField valueX;
			JTextField valueY;
			JTextField valueZ;

			public void actionPerformed(ActionEvent arg0)
			{
				frame = new JFrame("Add Translation");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(450, 150);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);

				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new GridLayout(3, 1));
				frame.add(mainPanel);

				JPanel panel1 = new JPanel();
				mainPanel.add(panel1);
				panel1.setLayout(new GridLayout(1, 3));
				JPanel panel2 = new JPanel();
				panel2.setLayout(new GridLayout(1, 3));
				mainPanel.add(panel2);
				JPanel panel3 = new JPanel();
				panel3.setLayout(new GridLayout(1, 2));
				mainPanel.add(panel3);

				valueX = new JTextField(10);
				valueX.setText("0");
				panel1.add(valueX);
				valueY = new JTextField(10);
				valueY.setText("0");
				panel1.add(valueY);
				valueZ = new JTextField(10);
				valueZ.setText("0");
				panel1.add(valueZ);

				panel2.add(new JLabel("X"));
				panel2.add(new JLabel("Y"));
				panel2.add(new JLabel("Z"));

				JButton Enter = new JButton("Enter");
				Enter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						try
						{
							float fvalueX = Float.parseFloat(valueX.getText());
							float fvalueY = Float.parseFloat(valueY.getText());
							float fvalueZ = Float.parseFloat(valueZ.getText());
							short type = UIInput.TRANSLATION;
							Coord translation = new Coord(fvalueX, fvalueY, fvalueZ, 0);
							UIInput input = new UIInput(type, UIInput.OBJECT, translation);
							parent.addAction(Run.method, input);
							frame.dispose();
							refreshTable();
						}
						catch (Exception e)
						{
							System.out.println("Error in GUI->AddTranslation->Enter");
							JOptionPane.showMessageDialog(frame, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				JButton Cancel = new JButton("Cancel");
				Cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						frame.dispose();
					}
				});
				panel3.add(Enter);
				panel3.add(Cancel);
			}
		});
		AddScale.addActionListener(new ActionListener() {
			JFrame frame;
			JTextField valueX;
			JTextField valueY;
			JTextField valueZ;

			public void actionPerformed(ActionEvent arg0)
			{
				frame = new JFrame("Add Scale");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(450, 150);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);

				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new GridLayout(3, 1));
				frame.add(mainPanel);

				JPanel panel1 = new JPanel();
				mainPanel.add(panel1);
				panel1.setLayout(new GridLayout(1, 3));
				JPanel panel2 = new JPanel();
				panel2.setLayout(new GridLayout(1, 3));
				mainPanel.add(panel2);
				JPanel panel3 = new JPanel();
				panel3.setLayout(new GridLayout(1, 2));
				mainPanel.add(panel3);

				valueX = new JTextField(10);
				valueX.setText("1");
				panel1.add(valueX);
				valueY = new JTextField(10);
				valueY.setText("1");
				panel1.add(valueY);
				valueZ = new JTextField(10);
				valueZ.setText("1");
				panel1.add(valueZ);

				panel2.add(new JLabel("X"));
				panel2.add(new JLabel("Y"));
				panel2.add(new JLabel("Z"));

				JButton Enter = new JButton("Enter");
				Enter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						try
						{
							float fvalueX = Float.parseFloat(valueX.getText());
							float fvalueY = Float.parseFloat(valueY.getText());
							float fvalueZ = Float.parseFloat(valueZ.getText());
							short type = UIInput.SCALE;
							Coord scale = new Coord(fvalueX, fvalueY, fvalueZ, 0);
							UIInput input = new UIInput(type, UIInput.OBJECT, scale);
							parent.addAction(Run.method, input);
							frame.dispose();
							refreshTable();
						}
						catch (Exception e)
						{
							System.out.println("Error in GUI->AddScale->Enter");
							JOptionPane.showMessageDialog(frame, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				JButton Cancel = new JButton("Cancel");
				Cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						frame.dispose();
					}
				});
				panel3.add(Enter);
				panel3.add(Cancel);
			}
		});
		AddCamera.addActionListener(new ActionListener() {
			JFrame frame;
			JTextField[] Pvalue = new JTextField[3];
			JTextField[] Lvalue = new JTextField[3];
			JTextField[] Wvalue = new JTextField[3];
			JTextField FOVvalue = new JTextField();

			public void actionPerformed(ActionEvent arg0)
			{
				frame = new JFrame("Add Scale");
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setSize(450, 250);
				frame.setLocationRelativeTo(null);
				frame.setResizable(false);
				frame.setVisible(true);

				JPanel mainPanel = new JPanel();
				mainPanel.setLayout(new GridLayout(5, 1));
				frame.add(mainPanel);

				JPanel panel1 = new JPanel();
				mainPanel.add(panel1);
				panel1.setLayout(new GridLayout(1, 4));
				panel1.add(new JLabel("position"));
				JPanel panel2 = new JPanel();
				panel2.setLayout(new GridLayout(1, 4));
				mainPanel.add(panel2);
				panel2.add(new JLabel("lookat"));
				JPanel panel3 = new JPanel();
				panel3.setLayout(new GridLayout(1, 4));
				mainPanel.add(panel3);
				panel3.add(new JLabel("worldup"));
				JPanel panel4 = new JPanel();
				panel4.setLayout(new GridLayout(1, 2));
				mainPanel.add(panel4);
				panel4.add(new JLabel("FOV"));
				JPanel panel5 = new JPanel();
				panel5.setLayout(new GridLayout(1, 2));
				mainPanel.add(panel5);

				Coord position = parent.render.camera.getPosition();
				Coord lookat = parent.render.camera.getLookat();
				Coord worldup = parent.render.camera.getWorldup();
				float FOV = parent.render.camera.getFOV();

				Pvalue[0] = new JTextField(8);
				Pvalue[0].setText(new Float(position.x).toString());
				panel1.add(Pvalue[0]);
				Pvalue[1] = new JTextField(8);
				Pvalue[1].setText(new Float(position.y).toString());
				panel1.add(Pvalue[1]);
				Pvalue[2] = new JTextField(8);
				Pvalue[2].setText(new Float(position.z).toString());
				panel1.add(Pvalue[2]);

				Lvalue[0] = new JTextField(8);
				Lvalue[0].setText(new Float(lookat.x).toString());
				panel2.add(Lvalue[0]);
				Lvalue[1] = new JTextField(8);
				Lvalue[1].setText(new Float(lookat.y).toString());
				panel2.add(Lvalue[1]);
				Lvalue[2] = new JTextField(8);
				Lvalue[2].setText(new Float(lookat.z).toString());
				panel2.add(Lvalue[2]);

				Wvalue[0] = new JTextField(8);
				Wvalue[0].setText(new Float(worldup.x).toString());
				panel3.add(Wvalue[0]);
				Wvalue[1] = new JTextField(8);
				Wvalue[1].setText(new Float(worldup.y).toString());
				panel3.add(Wvalue[1]);
				Wvalue[2] = new JTextField(8);
				Wvalue[2].setText(new Float(worldup.z).toString());
				panel3.add(Wvalue[2]);

				FOVvalue = new JTextField(8);
				FOVvalue.setText(new Float(FOV).toString());
				panel4.add(FOVvalue);

				JButton Enter = new JButton("Enter");
				Enter.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						try
						{
							Coord position = new Coord(Float.parseFloat(Pvalue[0].getText()), Float.parseFloat(Pvalue[1].getText()), Float.parseFloat(Pvalue[2]
									.getText()), 1);
							Coord lookat = new Coord(Float.parseFloat(Lvalue[0].getText()), Float.parseFloat(Lvalue[1].getText()), Float.parseFloat(Lvalue[2]
									.getText()), 1);
							Coord worldup = new Coord(Float.parseFloat(Wvalue[0].getText()), Float.parseFloat(Wvalue[1].getText()), Float.parseFloat(Wvalue[2]
									.getText()), 1);
							float FOV = Float.parseFloat(FOVvalue.getText());
							short type = UIInput.CAMERA;
							Camera camera = new Camera(position, lookat, worldup, FOV);
							UIInput input = new UIInput(type, UIInput.OBJECT, camera);
							parent.addAction(Run.method, input);
							frame.dispose();
							refreshTable();
						}
						catch (Exception e)
						{
							System.out.println("Error in GUI->AddCamera->Enter");
							JOptionPane.showMessageDialog(frame, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				JButton Cancel = new JButton("Cancel");
				Cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0)
					{
						frame.dispose();
					}
				});
				panel5.add(Enter);
				panel5.add(Cancel);
			}
		});
		EditPeriod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					int index = table.getSelectedRow();
					double time = Double.parseDouble(JOptionPane.showInputDialog("please enter time length (s)"));
					parent.editAction(Run.method, index, time);
					refreshTable();
				}
				catch (Exception e)
				{
					System.out.println("Error in GUI->EditPeriod");
					JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0)
			{
				try
				{
					int index = table.getSelectedRow();
					if (JOptionPane.showConfirmDialog(null, "Really want to delete action?", "Delete action", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
						parent.deleteAction(Run.method, index);
					refreshTable();
				}
				catch (Exception e)
				{
					System.out.println("Error in GUI->Delete");
					JOptionPane.showMessageDialog(null, e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		controlPanel.add(AddRotation);
		controlPanel.add(AddTranslation);
		controlPanel.add(AddScale);
		controlPanel.add(AddCamera);
		controlPanel.add(EditPeriod);
		controlPanel.add(Delete);

		setVisible(true);
	}

	public void refreshTable()
	{
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		tableModel.setRowCount(0);
		int no = 1;
		for (UIInput input : parent.actionList)
		{
			String number = new Integer(no++).toString();
			String type = "";
			String description = "";
			String timePeriod = new Double(input.period).toString();
			if (input.type == UIInput.ROTATION_X)
			{
				type = "rotation";
				description = "rotate by X in " + input.rotation.x + " degree";
			}
			else if (input.type == UIInput.ROTATION_Y)
			{
				type = "rotation";
				description = "rotate by Y in " + input.rotation.y + " degree";
			}
			else if (input.type == UIInput.ROTATION_Z)
			{
				type = "rotation";
				description = "rotate by Z in " + input.rotation.z + " degree";
			}
			else if (input.type == UIInput.TRANSLATION)
			{
				type = "translation";
				description = "translation by (" + input.translation.x + ", " + input.translation.y + ", " + input.translation.z + ")";
			}
			else if (input.type == UIInput.SCALE)
			{
				type = "scale";
				description = "scale X by " + input.scale.x + " times, scale Y by " + input.scale.y + " times, scale Z by " + input.scale.z + " times";
			}
			else if (input.type == UIInput.CAMERA)
			{
				type = "camera";
				description = "";
			}
			else
				continue;

			Object[] data = { number, type, description, timePeriod };
			tableModel.addRow(data);
		}
	}
}