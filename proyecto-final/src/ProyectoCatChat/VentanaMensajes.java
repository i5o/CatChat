package ProyectoCatChat;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
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
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.SwingConstants;

public class VentanaMensajes extends JFrame {

	private static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	private static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private static final long serialVersionUID = 1L;
	private static String posicion_archivos = new File("archivos/").getAbsolutePath().replace("\\", "/") + "/";
	private static int cantidadUsuarios;
	private static String usuario;
	static Connection conexion = null;
	
	public JButton btnEditarDatos;
	private JLabel fotodePerfil; 
	private String pathMiFoto = posicion_archivos + "/ajax-loader.gif";
	
	private Map<String, JLabel> fotos = new HashMap<String, JLabel>();
	
	private JPanel panelUsuarios, panelMensajes;

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
		
		JSplitPane splitPrincipal = new JSplitPane();
		splitPrincipal.setDividerLocation(300);
		splitPrincipal.setBounds(0, 0, 1280, 692);
		splitPrincipal.setDividerSize(0);
		splitPrincipal.setEnabled(false);
		getContentPane().add(splitPrincipal);

		JSplitPane splitUsuarios = new JSplitPane();
		splitUsuarios.setDividerSize(2);
		splitUsuarios.setEnabled(false);
		splitUsuarios.setDividerLocation(60);
		splitUsuarios.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPrincipal.setLeftComponent(splitUsuarios);
		
		panelUsuarios = new JPanel(new ScrollLayout());
		JScrollPane scrollUsuarios = new JScrollPane(panelUsuarios);
		scrollUsuarios.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitUsuarios.setRightComponent(scrollUsuarios);

		JPanel panelDatosUsuario = new JPanel();

		splitUsuarios.setLeftComponent(panelDatosUsuario);
		panelDatosUsuario.setLayout(null);
		
		fotodePerfil = new JLabel();
		fotodePerfil.setBounds(5, 4, 50, 50);
		panelDatosUsuario.add(fotodePerfil);
		fotodePerfil.setVerticalAlignment(JLabel.CENTER);
		fotodePerfil.setHorizontalAlignment(JLabel.CENTER);
		fotodePerfil.setIcon(CrearIcono(pathMiFoto, 30, 30, true, true));
		fotodePerfil.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
		
		JPanel fotodePerfilMarco = new JPanel();
		fotodePerfilMarco.setBounds(5, 4, 50, 50);
		panelDatosUsuario.add(fotodePerfilMarco);
		fotodePerfilMarco.setBorder(new LineBorder(new Color(30, 144, 255), 1, true));
		fotodePerfilMarco.setBackground(new Color(30, 144, 255));

		btnEditarDatos = CrearBotonAnimado("/editarPerfil.png", "/editarPerfil_.png");
		btnEditarDatos.setBounds(240, 18, 20, 20);
		panelDatosUsuario.add(btnEditarDatos);
		
		JSplitPane splitMensajes = new JSplitPane();
		splitMensajes.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPrincipal.setRightComponent(splitMensajes);
		splitMensajes.setDividerSize(2);
		splitMensajes.setEnabled(false);
		splitMensajes.setDividerLocation(600);
		
		JPanel panelEnvio = new JPanel();
		splitMensajes.setRightComponent(panelEnvio);
		panelEnvio.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 913, 87);
		panelEnvio.add(scrollPane);
		
		JTextArea Mensaje = new JTextArea();
		Mensaje.setLineWrap(true);
		Mensaje.setLocation(487, 0);
		scrollPane.setViewportView(Mensaje);
		
		JButton btnEnviar = CrearBotonAnimado("/Enviar.png", "/Enviar_.png");
		btnEnviar.setBounds(917, 18, 50, 50);
		panelEnvio.add(btnEnviar);
 
		JSplitPane splitInternalMensajes = new JSplitPane();
		splitInternalMensajes.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitMensajes.setLeftComponent(splitInternalMensajes);
		splitInternalMensajes.setDividerSize(2);
		splitInternalMensajes.setEnabled(false);
		splitInternalMensajes.setDividerLocation(60);
			
		JPanel panelDatosOtro = new JPanel();
		splitInternalMensajes.setLeftComponent(panelDatosOtro);
		panelDatosOtro.setLayout(null);
		
		JLabel label = new JLabel();
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
		label.setBounds(882, 4, 50, 50);
		panelDatosOtro.add(label);
		
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(30, 144, 255), 1, true));
		panel.setBackground(new Color(30, 144, 255));
		panel.setBounds(882, 4, 50, 50);
		panelDatosOtro.add(panel);
		
		JLabel lblNombre1 = new JLabel("?");
		lblNombre1.setBounds(59, 14, 185, 30);
		panelDatosOtro.add(lblNombre1);
		lblNombre1.setForeground(Color.WHITE);
		lblNombre1.setFont(new Font("Josefin Sans", Font.BOLD, 18));
		
		JLabel lblNombre = new JLabel("?");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Josefin Sans", Font.BOLD, 18));
		lblNombre.setBounds(65, 14, 185, 30);
		panelDatosUsuario.add(lblNombre);

		JLabel lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 1280, 720);
		getContentPane().add(lblFondo);
		lblFondo.setIcon(new ImageIcon(posicion_archivos + "/Fondo.jpg"));

		panelMensajes = new JPanel(new ScrollLayout());
		JScrollPane scrollMensajes = new JScrollPane(panelMensajes);
		scrollMensajes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitInternalMensajes.setRightComponent(scrollMensajes);
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				if (conexion == null) {
					return;
				}
				try {
					try {
						String nombre = ObtenerNombre(usuario);
						lblNombre.setText(nombre);
						lblNombre.repaint();
						lblFondo.repaint();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
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


		Color transparente = new Color(0,0,0,0);
		splitMensajes.setBackground(transparente);
		splitUsuarios.setBackground(transparente);
		splitPrincipal.setBackground(transparente);
		splitInternalMensajes.setBackground(transparente);

		panelUsuarios.setBackground(transparente);
		scrollUsuarios.setBackground(transparente);		
		panelEnvio.setBackground(transparente);
		panelDatosUsuario.setBackground(transparente);
		panelDatosOtro.setBackground(transparente);
		panelMensajes.setBackground(transparente);
		scrollMensajes.setBackground(transparente);

		scrollMensajes.getVerticalScrollBar().addAdjustmentListener(new ScrollCambiado(lblFondo));
		scrollUsuarios.getVerticalScrollBar().addAdjustmentListener(new ScrollCambiado(lblFondo));

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
		} catch (Exception e1) {
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
		cantidadUsuarios = 0;
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

class ScrollCambiado implements AdjustmentListener {
	
	JLabel fondo;

	public ScrollCambiado(JLabel lblFondo) {
		fondo = lblFondo;
	}

	@Override
	  public void adjustmentValueChanged(AdjustmentEvent evt) {
	    fondo.repaint();
	  }

}
