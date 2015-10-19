package prueba2;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.border.CompoundBorder;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import java.awt.Insets;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;
import java.awt.CardLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;

public class prueba {

	private JFrame frmPrueba;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					prueba window = new prueba();
					window.frmPrueba.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public prueba() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmPrueba = new JFrame();
		frmPrueba.setTitle("Prueba");
		frmPrueba.setResizable(false);
		frmPrueba.setBounds(0, 0, 1280, 720);
		frmPrueba.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmPrueba.getContentPane().setLayout(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerSize(0);
		splitPane.setEnabled(false);
		splitPane.setBounds(0, 0, 1280, 720);
		frmPrueba.getContentPane().add(splitPane);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(298, 350));
		splitPane.setLeftComponent(panel);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(0, 0, 300, 50);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblIgnacioRodrguez = new JLabel("Ignacio Rodr\u00EDguez");
		lblIgnacioRodrguez.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		lblIgnacioRodrguez.setBounds(21, 11, 136, 32);
		panel_1.add(lblIgnacioRodrguez);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\ignacio\\Desktop\\proyecto\\proyecto-final\\archivos\\Triste.png"));
		lblNewLabel.setBounds(137, -52, 163, 102);
		panel_1.add(lblNewLabel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(0, 40, 300, 650);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		JInternalFrame internalFrame = new JInternalFrame("New JInternalFrame");
		internalFrame.setBounds(10, 61, 278, 60);
		panel.add(internalFrame);
		
		JButton button = new JButton("Mensaje");
		internalFrame.getContentPane().add(button, BorderLayout.CENTER);
		
		JInternalFrame internalFrame_1 = new JInternalFrame("New JInternalFrame");
		internalFrame_1.setBounds(10, 146, 278, 60);
		panel.add(internalFrame_1);
		
		JButton btnNewButton = new JButton("New button");
		internalFrame_1.getContentPane().add(btnNewButton, BorderLayout.CENTER);
		
		JInternalFrame internalFrame_3 = new JInternalFrame("New JInternalFrame");
		internalFrame_3.setBounds(10, 228, 278, 60);
		panel.add(internalFrame_3);
		
		JButton btnNewButton_2 = new JButton("New button");
		internalFrame_3.getContentPane().add(btnNewButton_2, BorderLayout.CENTER);
		
		JInternalFrame internalFrame_2 = new JInternalFrame("New JInternalFrame");
		internalFrame_2.setBounds(10, 315, 278, 60);
		panel.add(internalFrame_2);
		
		JButton btnNewButton_1 = new JButton("New button");
		internalFrame_2.getContentPane().add(btnNewButton_1, BorderLayout.CENTER);
		internalFrame_2.setVisible(true);
		internalFrame_3.setVisible(true);
		internalFrame_1.setVisible(true);
		internalFrame.setVisible(true);
		
		
		JScrollPane scrollPane_1 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_1);
		scrollPane_1.setPreferredSize(new Dimension(frmPrueba.getContentPane().getWidth()-400, 500));
		scrollPane_1.getViewport().setBackground(new Color(128, 128, 255));
		
	}
}
