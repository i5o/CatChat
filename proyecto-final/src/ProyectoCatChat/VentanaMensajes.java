package ProyectoCatChat;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class VentanaMensajes extends JFrame {

	private static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	private static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private static final long serialVersionUID = 1L;
	private static String posicion_archivos = new File("archivos/").getAbsolutePath() + "/".replace("\\", "/");
	private static int cantidadUsuarios;
	private static String usuario;
	static Connection conexion = null;
	
	public JButton btnEditarDatos;
	private JLabel fotodePerfil; 
	private String pathMiFoto = posicion_archivos + "/ajax-loader.gif";
	
	private Map<String, JLabel> fotos = new HashMap<String, JLabel>();
	
	JPanel panelUsuarios;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMensajes window = new VentanaMensajes("ignacio", null);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public VentanaMensajes(String usuario_, Connection conexion_) {
		usuario = usuario_;
		conexion = conexion_;
		initialize();
	}

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
		splitPane_1.setDividerLocation(60);
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setLeftComponent(splitPane_1);

		panelUsuarios = new JPanel( new ScrollLayout() );
		JScrollPane scrollPanel = new JScrollPane(panelUsuarios);
		scrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitPane_1.setRightComponent(scrollPanel);

		JPanel panel = new JPanel();

		splitPane_1.setBackground(new Color(0,0,0,0));
		splitPane.setBackground(new Color(0,0,0,0));
		panel.setBackground(new Color(0,0,0,0));
		panel_1.setBackground(new Color(0,0,0,0));
		panelUsuarios.setBackground(new Color(0,0,0,0));
		scrollPanel.setBackground(new Color(0,0,0,0));
		
		splitPane_1.setLeftComponent(panel);
		panel.setLayout(null);
		
		fotodePerfil = new JLabel();
		fotodePerfil.setBounds(5, 5, 50, 50);
		panel.add(fotodePerfil);
		fotodePerfil.setVerticalAlignment(JLabel.CENTER);
		fotodePerfil.setHorizontalAlignment(JLabel.CENTER);
		fotodePerfil.setIcon(CrearIcono(pathMiFoto, 30, 30, true, true));
		fotodePerfil.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
		
		JPanel fotodePerfilMarco = new JPanel();
		fotodePerfilMarco.setBounds(5, 5, 50, 50);
		panel.add(fotodePerfilMarco);
		fotodePerfilMarco.setBorder(new LineBorder(new Color(30, 144, 255), 1, true));
		fotodePerfilMarco.setBackground(new Color(30, 144, 255));
				
		JLabel lblNombre = new JLabel("?");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setBounds(75, 8, 185, 30);
		lblNombre.setFont(new Font("Josefin Sans", Font.BOLD, 18));
		try {
			lblNombre.setText(ObtenerNombre(usuario));
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		panel.add(lblNombre);

		btnEditarDatos = CrearBotonAnimado("/editarPerfil.png", "/editarPerfil_.png");
		btnEditarDatos.setBounds(240, 18, 20, 20);
		panel.add(btnEditarDatos);
				
		JLabel lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 1280, 720);
		getContentPane().add(lblFondo);
		lblFondo.setIcon(new ImageIcon(posicion_archivos + "/Fondo.jpg"));

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				try {
					AgregarUsuarios();
					try {
						String path = ObtenerFoto(usuario);
						fotodePerfil.setIcon(CrearIcono(path, 48, 48, true, true));
					} catch (NullPointerException | IOException e1) {
					}
					for (final String user_ : fotos.keySet()) {
						Thread cargarFoto = new Thread(new Runnable() {
							public void run() {
								String path;
								try {
									path = ObtenerFoto(user_);
									JLabel miLabelFoto = fotos.get(user_);
									miLabelFoto.setIcon(CrearIcono(path, 55, 55, true, true));
								} catch (NullPointerException | SQLException | IOException e) {
									e.printStackTrace();
								}
							}
						});
						cargarFoto.start();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		
	}
	
	public void AgregarUsuario(final String usuario_) {
		JPanel usuario = new JPanel();
		usuario.setBackground(new Color(0,0,0,0));
		usuario.setLayout(null);
		usuario.setPreferredSize(new Dimension(270, 70));

		int y = (cantidadUsuarios * 70);
		if (cantidadUsuarios > 0) {
			y += 6 * cantidadUsuarios;
		}
		usuario.setLocation(0, y);
		panelUsuarios.add(usuario);
		panelUsuarios.repaint();
		
		JLabel labelFoto = new JLabel();
		labelFoto.setVerticalAlignment(JLabel.CENTER);
		labelFoto.setHorizontalAlignment(JLabel.CENTER);
		fotos.put(usuario_, labelFoto);

		labelFoto.setIcon(CrearIcono(posicion_archivos + "/ajax-loader.gif", 30, 30, true, true));
		labelFoto.setBounds(7, 5, 58, 58);
		labelFoto.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
		usuario.add(labelFoto);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
		panel.setBackground(new Color(30, 144, 255));
		panel.setBounds(7, 5, 58, 58);
		usuario.add(panel);
		
		JLabel lblUltMSG = new JLabel("Último mensaje");
		lblUltMSG.setForeground(Color.WHITE);
		lblUltMSG.setFont(new Font("Josefin Sans", Font.PLAIN, 15));
		lblUltMSG.setBounds(75, 37, 185, 23);
		usuario.add(lblUltMSG);
		String nombre = "?";
		try {
			nombre = ObtenerNombre(usuario_);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		JLabel label = new JLabel(nombre);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Josefin Sans", Font.BOLD, 18));
		label.setBounds(75, 5, 185, 30);
		usuario.add(label);
		
		JButton btnVaciar = CrearBotonAnimado("/VaciarChat.png", "/VaciarChat_.png");
		btnVaciar.setBounds(240, 46, 20, 20);
		usuario.add(btnVaciar);
				
		btnVaciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(handCursor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
            	setCursor(defaultCursor);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
		
		cantidadUsuarios++;
		
		JSeparator separador = new JSeparator();
		separador.setPreferredSize(new Dimension(300, 4));
		int yy = y;
		if (cantidadUsuarios > 1) {
			yy -= 3 ;
		}
		separador.setLocation(0, yy);
		panelUsuarios.add(separador);
		panelUsuarios.revalidate();
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
	
	public JButton CrearBotonAnimado(String fotoA, String fotoB) {
		JButton botonAnimado = new JButton();
		botonAnimado.setIcon(CrearIcono(posicion_archivos + fotoA, 20, 20, true, false));
		botonAnimado.setBackground(new Color(0, 0, 0, 0));
		botonAnimado.setFocusPainted(false);
		botonAnimado.setFocusable(false);
		botonAnimado.setContentAreaFilled(false);
		botonAnimado.setBorder(BorderFactory.createEmptyBorder());

		Timer iconoPresionado = new Timer(150, null);
		iconoPresionado.addActionListener(new ActionListener() 
		{@Override
			public void actionPerformed(ActionEvent e) {
				botonAnimado.setIcon(CrearIcono(posicion_archivos + fotoA, 20, 20, true, false));
				iconoPresionado.stop();
			}
		});
		botonAnimado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				botonAnimado.setIcon(CrearIcono(posicion_archivos + fotoB, 20, 20, true, false));
				iconoPresionado.restart();
			}
		});		
		
		return botonAnimado;
	}

	public String ObtenerFoto(String user) throws SQLException, IOException, NullPointerException {
		String sentencia = "select foto,extImagen from usuario where usuario='" + user + "';";
		Statement stmt_ = conexion.createStatement();
		ResultSet rs = stmt_.executeQuery(sentencia);

		rs.next();
		
		Blob datosFoto = rs.getBlob(1);
		String extImage = rs.getString(2);

		File temp = File.createTempFile("tempFoto_" + user + "-CatChat-", extImage);
		InputStream is = datosFoto.getBinaryStream();
		FileOutputStream fos = new FileOutputStream(temp);

		int b = 0;
		while ((b = is.read()) != -1) {
			fos.write(b);
		}

		return temp.getAbsolutePath();
	}
	
	public String ObtenerNombre(String user) throws SQLException {
		String sentencia = "select nombre,apellido from usuario where usuario='" + user + "';";
		Statement stmt_ = conexion.createStatement();
		ResultSet rs = stmt_.executeQuery(sentencia);

		rs.next();
		
		return rs.getString(1) + " " + rs.getString(2);
	}
	
	public void AgregarUsuarios() throws SQLException {
		String sentencia = "select usuario from usuario where registroCompleto=1;";
		Statement stmt_addusers = conexion.createStatement();
		ResultSet rs = stmt_addusers.executeQuery(sentencia);
		while (rs.next()) {
			final String nombreUsuario = rs.getString(1);
			if (nombreUsuario.equals(usuario)) {
				continue;
			}
			AgregarUsuario(nombreUsuario);
			panelUsuarios.revalidate();
			panelUsuarios.repaint();
		}
		
	}
}
