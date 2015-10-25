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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONObject;

public class VentanaMensajes extends JFrame {

	private static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	private static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	private static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	private static final long serialVersionUID = 1L;
	private static String posicion_archivos = new File("archivos/").getAbsolutePath().replace("\\", "/") + "/";
	private static int cantidadUsuarios, cantidadMensajes, y_mensajes;
	private static String usuario;
	static Connection conexion = null;

	public JButton btnEditarDatos;
	private JLabel fotodePerfil, lblFondo;
	private String pathMiFoto = posicion_archivos + "/ajax-loader.gif";

	private Map < String, JLabel > fotos = new HashMap < String, JLabel > ();
	private Map < String, String > fotosPath = new HashMap < String, String > ();
	private Map < String, String > nombreUsuarios = new HashMap < String, String > ();
	private Map < String, JButton[] > botonesListaUsuarios = new HashMap < String, JButton[] > ();
	private JSONObject mensajes = null;
	private JSONArray mensajes_json, mensajes_antes;

	private JPanel panelUsuarios, panelMensajes;
	private JScrollPane scrollMensajes;
	private Timer chequearMensajes;

	private JSplitPane splitPrincipal, splitMensajes;

	private String seleccionado;
	private JButton seleccionado_btn;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaMensajes window = new VentanaMensajes("ignacio", null);
					window.setVisible(true);
				} catch (Exception e) {
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

		cantidadMensajes = 0;

		getContentPane().setBackground(new Color(30, 144, 255));
		setIconImage(Toolkit.getDefaultToolkit().getImage(posicion_archivos + "/Saludo.png"));
		setTitle("CatChat");
		setBounds(100, 100, 1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);

		splitPrincipal = new JSplitPane();
		splitPrincipal.setDividerLocation(300);
		splitPrincipal.setBounds(0, 0, 1280, 692);
		splitPrincipal.setDividerSize(0);
		splitPrincipal.setEnabled(false);
		splitPrincipal.setBackground(new Color(0, 0, 0, 0));
		getContentPane().add(splitPrincipal);

		JSplitPane splitUsuarios = new JSplitPane();
		splitUsuarios.setDividerSize(2);
		splitUsuarios.setEnabled(false);
		splitUsuarios.setDividerLocation(60);
		splitUsuarios.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitUsuarios.setBackground(new Color(0, 0, 0, 0));

		splitPrincipal.setLeftComponent(splitUsuarios);

		panelUsuarios = new JPanel(new ScrollLayout());
		panelUsuarios.setBackground(new Color(0, 0, 0, 0));

		JScrollPane scrollUsuarios = new JScrollPane(panelUsuarios);
		scrollUsuarios.getVerticalScrollBar().setUnitIncrement(20);
		scrollUsuarios.setBackground(new Color(0, 0, 0, 0));
		scrollUsuarios.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitUsuarios.setRightComponent(scrollUsuarios);

		JPanel panelDatosUsuario = new JPanel();
		panelDatosUsuario.setBackground(new Color(0, 0, 0, 0));
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

		JLabel lblNombre = new JLabel("?");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Josefin Sans", Font.BOLD, 18));
		lblNombre.setBounds(65, 14, 185, 30);
		panelDatosUsuario.add(lblNombre);

		//
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(0, 0, 0, 0));
		splitPrincipal.setRightComponent(panel_1);
		panel_1.setLayout(null);
		JLabel lblinfo = new JLabel("<html><center>Para comenzar a chatear, seleccione un usuario clickeando el icono (el icono es el que está actualmente en el fondo) que aparece al costado de su información.<br>\r\n</center>");
		lblinfo.setForeground(new Color(0, 0, 255));
		lblinfo.setFont(new Font("Raleway", Font.BOLD, 27));
		lblinfo.setVerticalAlignment(SwingConstants.TOP);
		lblinfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblinfo.setBounds(220, 223, 538, 243);
		panel_1.add(lblinfo);

		JLabel lblFotoInfo = new JLabel();
		lblFotoInfo.setBounds(165, 11, 649, 668);
		lblFotoInfo.setIcon(new ImageIcon(posicion_archivos + "seleccion.png"));
		panel_1.add(lblFotoInfo);

		//

		lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 1280, 720);
		getContentPane().add(lblFondo);
		lblFondo.setIcon(new ImageIcon(posicion_archivos + "/Fondo.jpg"));

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				if (conexion == null) {
					return;
				}
				try {
					String nombre = ObtenerNombre(usuario);
					lblNombre.setText(nombre);
					lblNombre.repaint();
					lblFondo.repaint();
					AgregarUsuarios();

					String path = ObtenerFoto(usuario);
					fotosPath.put(usuario, path);
					fotodePerfil.setIcon(CrearIcono(path, 48, 48, true, true));

					for (final String user_: fotos.keySet()) {
						Thread cargarFoto = new Thread(new Runnable() {
							public void run() {
								String path;
								path = ObtenerFoto(user_);
								JLabel miLabelFoto = fotos.get(user_);
								miLabelFoto.setIcon(CrearIcono(path, 55, 55, true, true));
								fotosPath.put(user_, path);
								JButton[] botones = botonesListaUsuarios.get(user_);
								botones[0].setVisible(true);
								botones[1].setVisible(true);
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

		// Redibujar el fondo para evitar problemas...
		Timer redibujarFondo = new Timer(200, new ActionListener() {@Override
			public void actionPerformed(ActionEvent e) {
				lblFondo.repaint();
			}
		});
		redibujarFondo.start();

		// Chequear mensajes nuevos
		chequearMensajes = new Timer(1000, new ActionListener() {@Override

			public void actionPerformed(ActionEvent e) {

				String sentencia_busqueda = "select texto from Mensajes where '" + usuario + "' IN (participante1, participante2) and '" + seleccionado + "' IN (participante1, participante2);";
				Statement stmt_;

				mensajes_antes = mensajes_json;
				String[] mensajes_antes_str = new String[mensajes_antes.length()];
				int p = 0;
				for (final Object x: mensajes_antes) {
					mensajes_antes_str[p] = x.toString();
					p++;
				}
				try {
					stmt_ = conexion.createStatement();
					ResultSet rs = stmt_.executeQuery(sentencia_busqueda);
					rs.next();
					String jsonMensajes_ = rs.getString(1);
					mensajes = new JSONObject(jsonMensajes_);
					mensajes_json = mensajes.getJSONArray("Mensajes");

					String[] mensajes_ahora_str = new String[mensajes_json.length()];
					p = 0;
					for (final Object x: mensajes_json) {
						mensajes_ahora_str[p] = x.toString();
						p++;
					}

					for (String x: mensajes_ahora_str) {
						if (!Arrays.asList(mensajes_antes_str).contains(x)) {
							mensajes_antes = mensajes_json;
							String mensaje_json = "{\"Mensaje\": " + x + "}";
							JSONArray mensaje = new JSONObject(mensaje_json).getJSONArray("Mensaje");
							CrearWidgetMensaje(mensaje.getString(0), mensaje.getString(1), mensaje.getString(2));
						}
					}

				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public void AgregarUsuario(final String usuario_) {
		JPanel usuario = new JPanel();
		usuario.setBackground(new Color(0, 0, 0, 0));
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
		// usuario.add(lblUltMSG);
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

		JButton btnChatear = CrearBotonChat(usuario_);
		btnChatear.setBounds(240, 45, 20, 20);
		usuario.add(btnChatear);

		JButton btnVaciar = CrearBotonAnimado("/VaciarChat.png", "/VaciarChat_.png");
		btnVaciar.setBounds(240, 46, 20, 20);
		// usuario.add(btnVaciar);

		cantidadUsuarios++;

		JButton botones[] = new JButton[2];
		botones[0] = btnChatear;
		botones[1] = btnVaciar;

		botonesListaUsuarios.put(usuario_, botones);

		btnChatear.setVisible(false);
		btnVaciar.setVisible(false);

		JSeparator separador = new JSeparator();
		separador.setPreferredSize(new Dimension(300, 4));
		int yy = y;
		if (cantidadUsuarios > 1) {
			yy -= 3;
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
		iconoPresionado.addActionListener(new ActionListener() {@Override
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

		botonAnimado.addMouseListener(new MouseAdapter() {@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(handCursor);
			}@Override
			public void mouseExited(MouseEvent e) {
				setCursor(defaultCursor);
			}

			@Override
			public void mouseClicked(MouseEvent e) {}
		});
		return botonAnimado;
	}

	public JButton CrearBotonChat(String usuario) {
		JButton botonSeleccion = new JButton();
		botonSeleccion.setIcon(CrearIcono(posicion_archivos + "seleccion.png", 20, 20, true, false));
		botonSeleccion.setBackground(new Color(0, 0, 0, 0));
		botonSeleccion.setFocusPainted(false);
		botonSeleccion.setFocusable(false);
		botonSeleccion.setContentAreaFilled(false);
		botonSeleccion.setBorder(BorderFactory.createEmptyBorder());

		botonSeleccion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (seleccionado_btn != null) {
					seleccionado_btn.setIcon(CrearIcono(posicion_archivos + "seleccion.png", 20, 20, true, false));
				}
				botonSeleccion.setIcon(CrearIcono(posicion_archivos + "seleccion_.png", 20, 20, true, false));
				seleccionado = usuario;
				seleccionado_btn = botonSeleccion;
				splitPrincipal.setRightComponent(CrearListaMensajes(usuario));
			}
		});

		botonSeleccion.addMouseListener(new MouseAdapter() {@Override
			public void mouseEntered(MouseEvent e) {
				setCursor(handCursor);
			}@Override
			public void mouseExited(MouseEvent e) {
				setCursor(defaultCursor);
			}
		});
		return botonSeleccion;
	}

	public String ObtenerFoto(String user) {
		if (fotosPath.containsKey(user)) {
			return fotosPath.get(user);
		}
		String sentencia = "select foto,extImagen from usuario where usuario='" + user + "';";
		Statement stmt_;
		String path = "?";
		try {
			stmt_ = conexion.createStatement();

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

			path = temp.getAbsolutePath();
		} catch (SQLException | IOException e) {}

		return path;
	}

	public String ObtenerNombre(String user) {
		if (nombreUsuarios.containsKey(user)) {
			return nombreUsuarios.get(user);
		}

		String sentencia = "select nombre,apellido from usuario where usuario='" + user + "';";
		Statement stmt_;
		String nombre = "?";
		try {
			stmt_ = conexion.createStatement();
			ResultSet rs = stmt_.executeQuery(sentencia);
			rs.next();
			nombre = rs.getString(1) + " " + rs.getString(2);
			nombreUsuarios.put(user, nombre);
		} catch (SQLException e) {}

		return nombre;
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

	public void CrearWidgetMensaje(String user_, String mensaje_, String fecha_) {
		Color color;
		int x = 20;
		if (!user_.equals(usuario)) {
			x = 410;
			color = new Color(72, 209, 204);
		} else {

			color = new Color(30, 144, 255);
		}

		y_mensajes = 20;
		if (cantidadMensajes > 0) {
			y_mensajes += 120 * cantidadMensajes;
		}

		String nombreUser_ = ObtenerNombre(user_);

		cantidadMensajes++;
		JPanel Mensaje = new JPanel();
		Mensaje.setPreferredSize(new Dimension(535, 100));
		Mensaje.setBackground(new Color(0, 0, 0, 0));
		Mensaje.setBorder(new LineBorder(color, 3));
		Mensaje.setLocation(x, y_mensajes);
		Mensaje.setLayout(null);

		JLabel fotoBorde1 = new JLabel("");
		fotoBorde1.setHorizontalAlignment(SwingConstants.CENTER);
		fotoBorde1.setBorder(new LineBorder(color, 2));
		fotoBorde1.setBounds(13, 26, 60, 60);
		fotoBorde1.setIcon(CrearIcono(ObtenerFoto(user_), 60, 60, true, false));
		Mensaje.add(fotoBorde1);

		JPanel bordeFalso = new JPanel();
		bordeFalso.setBounds(0, 0, 535, 19);
		Mensaje.add(bordeFalso);
		bordeFalso.setBackground(color);
		bordeFalso.setLayout(null);

		JLabel usuario = new JLabel(nombreUser_ + " dice:");
		usuario.setBounds(10, 0, 510, 19);
		usuario.setHorizontalAlignment(SwingConstants.LEFT);
		usuario.setForeground(Color.WHITE);
		usuario.setFont(new Font("Raleway", Font.BOLD, 15));
		bordeFalso.add(usuario);

		JLabel fecha = new JLabel(fecha_);
		fecha.setForeground(Color.WHITE);
		fecha.setFont(new Font("Raleway", Font.BOLD, 13));
		fecha.setBounds(350, 2, 350, 14);
		bordeFalso.add(fecha);

		JScrollPane scroll = new JScrollPane() {
			private static final long serialVersionUID = 1L;

		@Override public void setBorder(Border border) {}
		};
		
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(81, 21, 446, 72);
		Mensaje.add(scroll);

		JTextArea mensaje = new JTextArea(mensaje_);
		mensaje.setSize(50, 70);
		mensaje.setLocation(21, 0);
		mensaje.setBackground(Color.WHITE);
		mensaje.setFont(new Font("Josefin Sans", Font.PLAIN, 15));
		mensaje.setEditable(false);
		mensaje.setLineWrap(true);

		scroll.getViewport().setBackground(Color.WHITE);
		scroll.setBackground(Color.WHITE);
		scroll.setViewportView(mensaje);
		panelMensajes.add(Mensaje);
		panelMensajes.revalidate();
		panelMensajes.repaint();
		irAbajo(scrollMensajes);
	}

	public JSplitPane CrearListaMensajes(String user_) {
		cantidadMensajes = 0;

		splitMensajes = new JSplitPane();
		splitMensajes.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitMensajes.setDividerSize(2);
		splitMensajes.setEnabled(false);
		splitMensajes.setDividerLocation(600);
		splitMensajes.setBackground(new Color(0, 0, 0, 0));


		JSplitPane splitInternalMensajes = new JSplitPane();
		splitInternalMensajes.setBackground(new Color(0, 0, 0, 0));
		splitInternalMensajes.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitMensajes.setLeftComponent(splitInternalMensajes);
		splitInternalMensajes.setDividerSize(2);
		splitInternalMensajes.setEnabled(false);
		splitInternalMensajes.setDividerLocation(60);

		JPanel panelDatosOtro = new JPanel();
		panelDatosOtro.setBackground(new Color(0, 0, 0, 0));
		splitInternalMensajes.setLeftComponent(panelDatosOtro);
		panelDatosOtro.setLayout(null);

		JLabel label = new JLabel();
		label.setIcon(CrearIcono(ObtenerFoto(seleccionado), 50, 50, true, false));
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

		JLabel lblNombre1 = new JLabel("Hablando con: " + ObtenerNombre(seleccionado));
		lblNombre1.setBounds(59, 14, 600, 30);
		panelDatosOtro.add(lblNombre1);
		lblNombre1.setForeground(Color.WHITE);
		lblNombre1.setFont(new Font("Josefin Sans", Font.BOLD, 18));

		panelMensajes = new JPanel(new ScrollLayout());
		panelMensajes.setBackground(new Color(0, 0, 0, 0));
		scrollMensajes = new JScrollPane(panelMensajes);
		scrollMensajes.getVerticalScrollBar().setUnitIncrement(20);
		scrollMensajes.setBackground(new Color(0, 0, 0, 0));
		scrollMensajes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		splitInternalMensajes.setRightComponent(scrollMensajes);

		JPanel panelEnvio = new JPanel();
		panelEnvio.setBackground(new Color(0, 0, 0, 0));
		splitMensajes.setRightComponent(panelEnvio);
		panelEnvio.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(new Color(0, 0, 0, 0));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 913, 87);
		panelEnvio.add(scrollPane);

		JTextArea Mensaje = new JTextArea();
		Placeholder Mensaje_pl = new Placeholder("Escriba el mensaje a enviar", Mensaje, true);
		Mensaje_pl.changeAlpha(0.4f);
		Mensaje.setLineWrap(true);
		Mensaje.setLocation(487, 0);
		scrollPane.setViewportView(Mensaje);
		ToolTipManager.sharedInstance().setInitialDelay(0);
		JButton btnEnviar = CrearBotonAnimado("/Enviar.png", "/Enviar_.png");
		btnEnviar.setToolTipText("Click para enviar.");
		btnEnviar.setBounds(917, 18, 50, 50);
		panelEnvio.add(btnEnviar);

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				ObtenerMensajes(seleccionado, usuario);
			}
		});
		t1.start();


		btnEnviar.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {
				Date fechaActual = new Date();
				String x = Mensaje.getText();
				if (x.isEmpty() || x == null || x.trim().isEmpty()) {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
				EnviarMensaje(x, fechaActual.toLocaleString());
				Mensaje.setText("");
			}
		});
		return splitMensajes;
	}

	public void ObtenerMensajes(String user1, String user2) {
		String sentencia_busqueda = "select texto from Mensajes where '" + user1 + "' IN (participante1, participante2) and '" + user2 + "' IN (participante1, participante2);";
		String sentencia_agregado = "INSERT INTO `Mensajes` (`participante1`, `participante2`, `texto`, `id`) VALUES (?, ?, ?, ?);";

		Statement stmt_;
		String jsonmensajes = "{ 'Mensajes': [] }";

		try {
			stmt_ = conexion.createStatement();
			ResultSet rs = stmt_.executeQuery(sentencia_busqueda);
			rs.next();
			jsonmensajes = rs.getString(1);
		} catch (SQLException e) {
			String error = e.toString();
			if (error.contains("Illegal operation on empty result set.")) {
				PreparedStatement psmnt;
				try {
					psmnt = conexion.prepareStatement(sentencia_agregado);
					psmnt.setString(1, user1);
					psmnt.setString(2, user2);
					psmnt.setString(3, jsonmensajes);
					psmnt.setString(4, user1 + "-" + user2);
					psmnt.executeUpdate();
				} catch (SQLException e1) {
				}
			}
		}

		mensajes = new JSONObject(jsonmensajes);
		mensajes_json = mensajes.getJSONArray("Mensajes");
		cantidadMensajes = 0;
		for (int m = 0; m < mensajes_json.length(); m++) {
			JSONArray d = mensajes_json.getJSONArray(m);
			CrearWidgetMensaje(d.getString(0), d.getString(1), d.getString(2));
		}

		chequearMensajes.start();
	}

	public void EnviarMensaje(String mensaje, String fecha) {
		chequearMensajes.stop();
		String[] a = new String[3];
		a[0] = usuario;
		a[1] = mensaje;
		a[2] = fecha;
		mensajes_json.put(a);
		mensajes_json = new JSONObject(mensajes.toString()).getJSONArray("Mensajes");

		CrearWidgetMensaje(usuario, mensaje, fecha);
		String sentencia_guardado = "UPDATE `Mensajes` SET `texto`=? where ? IN (participante1, participante2) and ? IN (participante1, participante2);";
		PreparedStatement psmnt = null;
		try {
			psmnt = conexion.prepareStatement(sentencia_guardado);
			psmnt.setString(1, mensajes.toString());
			psmnt.setString(2, usuario);
			psmnt.setString(3, seleccionado);
			psmnt.executeUpdate();
			chequearMensajes.start();
		} catch (SQLException e) {}
	}

	private void irAbajo(JScrollPane scrollPane) {
	    JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
	    AdjustmentListener downScroller = new AdjustmentListener() {
	        @Override
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	            Adjustable adjustable = e.getAdjustable();
	            adjustable.setValue(adjustable.getMaximum());
	            verticalBar.removeAdjustmentListener(this);
	        }
	    };
	    verticalBar.addAdjustmentListener(downScroller);
	}
}