package ProyectoCatChat;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

public class VentanaMensajes extends JFrame {

	private static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	private static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private static final long serialVersionUID = 1L;
	private static String posicion_archivos = new File("archivos/").getAbsolutePath() + "/".replace("\\", "/");
	private static int cantidadUsuarios = 0;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMensajes window = new VentanaMensajes();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VentanaMensajes() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		getContentPane().setBackground(new Color(30, 144, 255));
		setIconImage(Toolkit.getDefaultToolkit().getImage(posicion_archivos + "/Saludo.png"));
		setTitle("CatChat");
		setBounds(100, 100, 1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setDividerLocation(300);
		splitPane.setBounds(0, 0, 1280, 692);
		splitPane.setDividerSize(0);
		splitPane.setEnabled(false);
		getContentPane().add(splitPane);
		
		JPanel panel_1 = new JPanel();
		splitPane.setRightComponent(panel_1);
		panel_1.setLayout(null);
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setDividerSize(2);
		splitPane_1.setEnabled(false);
		splitPane_1.setDividerLocation(50);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_1);

		JPanel dragPanel = new JPanel( new ScrollLayout() );
		JScrollPane scrollPanel = new JScrollPane(dragPanel);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane_1.setRightComponent(scrollPanel);

		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);		
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		AgregarUsuario(dragPanel);
		JLabel lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 1280, 720);
		getContentPane().add(lblFondo);
		lblFondo.setIcon(new ImageIcon(posicion_archivos + "/Fondo.jpg"));
	}
	
	public void AgregarUsuario(JPanel dragPanel) {

		JPanel usuario = new JPanel();
		usuario.setLayout(null);
		usuario.setPreferredSize(new Dimension(270, 70));
		System.out.println(cantidadUsuarios);
		int y = (cantidadUsuarios * 70);
		if (cantidadUsuarios > 0) {
			y += 6 * cantidadUsuarios;
		}
		usuario.setLocation(0, y);
		dragPanel.add(usuario);
		
		JLabel lblNewLabel2 = new JLabel();
		lblNewLabel2.setAlignmentX(CENTER_ALIGNMENT);
		lblNewLabel2.setAlignmentY(CENTER_ALIGNMENT);
		lblNewLabel2.setIcon(CrearIcono(posicion_archivos + "/Nacho.jpg", 60, 60, true, true));
		lblNewLabel2.setBounds(5, 5, 60, 60);
		usuario.add(lblNewLabel2);
		lblNewLabel2.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(30, 144, 255), 1, true));
		panel.setBackground(new Color(30, 144, 255));
		panel.setBounds(6, 7, 56, 56);
		usuario.add(panel);
		
		JLabel lblNombre = new JLabel("Ignacio Rodríguez");
		lblNombre.setFont(new Font("Josefin Sans", Font.BOLD, 18));
		lblNombre.setBounds(75, 5, 185, 30);
		usuario.add(lblNombre);
		
		JLabel lblUltMSG = new JLabel("Último mensaje");
		lblUltMSG.setFont(new Font("Josefin Sans", Font.PLAIN, 15));
		lblUltMSG.setBounds(75, 37, 185, 23);
		usuario.add(lblUltMSG);
		
		JLabel lblVaciar = new JLabel();
		lblVaciar.setIcon(CrearIcono(posicion_archivos + "/VaciarChat.png", 15, 15, true, true));
		lblVaciar.setBounds(240, 46, 20, 20);
		usuario.add(lblVaciar);

		cantidadUsuarios++;
		
		JSeparator separador = new JSeparator();
		separador.setPreferredSize(new Dimension(300, 4));
		int yy = y;
		if (cantidadUsuarios > 1) {
			yy -= 3 ;
		}
		separador.setLocation(0, yy);
		dragPanel.add(separador);
	}

	public ImageIcon CrearIcono(String path, int l, int a, boolean redimensionar, boolean busy) {
		if (busy) {
			this.setCursor(waitCursor);
		}
		ImageIcon icono = new ImageIcon(path);
		if (redimensionar) {
			int escalado = Image.SCALE_SMOOTH;
			if (path.endsWith(".gif")) {
				escalado = Image.SCALE_FAST;
			}
			Image img = icono.getImage();
			Image newimg = img.getScaledInstance(l, a, escalado);
			ImageIcon new_icono = new ImageIcon(newimg);
			this.setCursor(defaultCursor);
			return new_icono;

		} else {
			this.setCursor(defaultCursor);
			return icono;
		}
	}
}
