package ProyectoCatChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.Icon;

public class VentanaDatos extends JFrame {

	private static final long serialVersionUID = 1L;

	private JLabel labelGatito,
	lblNombre,
	lblCiudad,
	lblNewLabel_3,
	lblEdad;

	private static JLabel FotoDePerfil;

	private JLabel labelGatito1;

	private JLabel lblFondo;

	private JLabel Foto;

	private JLabel lblNewLabel_1;

	private JLabel lblCompleteEstosDatos;

	private JLabel lblSexo;

	public JTextField CambioNombre,
	CambioApellido,
	CambioCiudad,
	CambioEdad;

	public JButton Continuar,
	CambiarFoto,
	Siguiente,
	fotoDefecto;

	public JComboBox < String > Sexo;

	private String posicion_archivos = new File("archivos/").getAbsolutePath() + "/".replace("\\", "/");
	private Font fuente_30 = new Font("Nyala", Font.PLAIN, 30);
	private Font fuente_25 = new Font("Nyala", Font.PLAIN, 25);

	static String idUsuario = null;

	public String pathFotoDefecto = posicion_archivos + "/fotoDefecto.png";
	public String pathFoto = pathFotoDefecto;
	public String pathFotoDefectoHombre = posicion_archivos + "/Hombre.png";
	public String pathFotoDefectoMujer = posicion_archivos + "/Mujer.png";
	static Connection conexion = null;
	static Statement stmt = null;

	Timer ponerDefecto;


	private static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	private static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

	private static FileOutputStream fos;
	private JLabel AlertaEntry1, AlertaEntry2, AlertaEntry3, AlertaEntry4, AlertaEntry5;
	private static ImageIcon iconoAlertaEntry;
	String iconoAlertaEntry_path;
	private boolean obteniendoFoto = false;
	private boolean cargandoDatos = false;
	private boolean ManejandoDatos = false;
	private boolean guardandoDatos = false;

	File chooserPath = new File(System.getProperty("user.home"));

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VentanaDatos window = new VentanaDatos(null, null, null, false);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VentanaDatos(String idUsuario_, Connection conexion_, Statement stmt_, boolean Editando_) {

		getContentPane().setBackground(new Color(30, 144, 255));
		idUsuario = idUsuario_;
		conexion = conexion_;
		stmt = stmt_;
		initialize();
	}

	private void initialize() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(posicion_archivos + "/Saludo.png"));
		setTitle("CatChat");
		setBounds(100, 100, 1280, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);

		labelGatito = new JLabel("");
		labelGatito.setIcon(new ImageIcon(posicion_archivos + "/Timido.png"));
		labelGatito.setBounds(135, 144, 364, 348);
		getContentPane().add(labelGatito);

		lblCompleteEstosDatos = new JLabel("Por favor, completa estos datos");
		lblCompleteEstosDatos.setFont(new Font("Nyala", Font.PLAIN, 45));
		lblCompleteEstosDatos.setForeground(new Color(230, 230, 250));
		lblCompleteEstosDatos.setBounds(60, 548, 537, 48);
		getContentPane().add(lblCompleteEstosDatos);

		lblNewLabel_1 = new JLabel("Pusheen quiere saber m\u00E1s acerca ti");
		lblNewLabel_1.setForeground(new Color(230, 230, 250));
		lblNewLabel_1.setFont(new Font("Nyala", Font.PLAIN, 45));
		lblNewLabel_1.setBounds(28, 45, 621, 51);
		getContentPane().add(lblNewLabel_1);

		Foto = new JLabel("Foto de perfil");
		Foto.setForeground(Color.WHITE);
		Foto.setFont(fuente_30);
		Foto.setBounds(1050, 110, 162, 48);
		getContentPane().add(Foto);

		CambiarFoto = new JButton("Cambiar");
		CambiarFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String preCambiado = pathFoto;
				pathFoto = elegirArchivo();
				if (pathFoto != null) {
					FotoDePerfil.setIcon(CrearIcono(pathFoto, 150, -1, true, true));
					fotoDefecto.setVisible(true);
				} else {
					pathFoto = preCambiado;
				}
			}
		});
		CambiarFoto.setFont(fuente_30);
		CambiarFoto.setBounds(1060, 350, 130, 35);
		getContentPane().add(CambiarFoto);

		lblNombre = new JLabel("Nombre");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(fuente_30);
		lblNombre.setBounds(655, 125, 110, 30);
		getContentPane().add(lblNombre);

		lblNewLabel_3 = new JLabel("Apellido");
		lblNewLabel_3.setFont(fuente_30);
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setBounds(655, 195, 110, 30);
		getContentPane().add(lblNewLabel_3);

		lblEdad = new JLabel("Edad");
		lblEdad.setForeground(Color.WHITE);
		lblEdad.setFont(fuente_30);
		lblEdad.setBounds(655, 265, 110, 30);
		getContentPane().add(lblEdad);

		lblCiudad = new JLabel("Ciudad");
		lblCiudad.setForeground(Color.WHITE);
		lblCiudad.setFont(fuente_30);
		lblCiudad.setBounds(655, 335, 110, 30);
		getContentPane().add(lblCiudad);

		fotoDefecto = new JButton();
		fotoDefecto.setBackground(new Color(0, 0, 0, 0));
		fotoDefecto.setFocusPainted(false);
		fotoDefecto.setFocusable(false);
		fotoDefecto.setVisible(false);
		fotoDefecto.setBorder(BorderFactory.createEmptyBorder());
		fotoDefecto.setBounds(1178, 182, 20, 20);
		getContentPane().add(fotoDefecto);
		fotoDefecto.setIcon(CrearIcono(posicion_archivos + "/ponerDefecto.png", 20, 20, true, false));
		fotoDefecto.setContentAreaFilled(false);

		ponerDefecto = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Sexo.getSelectedItem().toString() == "Femenino") {
					pathFoto = pathFotoDefectoMujer;
				} else if (Sexo.getSelectedItem().toString() == "Masculino") {
					pathFoto = pathFotoDefectoHombre;
				} else {
					pathFoto = pathFotoDefecto;
				}
				fotoDefecto.setIcon(CrearIcono(posicion_archivos + "/ponerDefecto.png", 20, 20, true, false));
				FotoDePerfil.setIcon(CrearIcono(pathFoto, 150, -1, true, true));
				fotoDefecto.setVisible(false);
				ponerDefecto.stop();
			}
		});
		fotoDefecto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fotoDefecto.setIcon(CrearIcono(posicion_archivos + "/ponerDefecto_.png", 20, 20, true, false));
				ponerDefecto.restart();
			}
		});

		FotoDePerfil = new JLabel();
		FotoDePerfil.setBackground(new Color(51, 153, 255));
		FotoDePerfil.setBounds(1050, 180, 150, 150);
		getContentPane().add(FotoDePerfil);
		FotoDePerfil.setIcon(CrearIcono(pathFotoDefecto, 150, -1, true, true));
		FotoDePerfil.setBorder(new LineBorder(SystemColor.inactiveCaptionBorder, 2, true));

		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.textHighlight);
		panel.setBounds(1050, 180, 150, 150);
		getContentPane().add(panel);
		panel.setLayout(null);

		UIManager.put("ToolTip.background", Color.decode("#EF6161"));

		iconoAlertaEntry_path = posicion_archivos + "/AlertaEntry.png";

		iconoAlertaEntry = CrearIcono(iconoAlertaEntry_path, 20, 20, true, false);
		ToolTipManager.sharedInstance().setInitialDelay(0);

		AlertaEntry1 = new JLabel(iconoAlertaEntry) {
			private static final long serialVersionUID = 1L;

			public Point getToolTipLocation(MouseEvent e) {
				return new Point(25, 2);
			}
		};
		AlertaEntry2 = new JLabel(iconoAlertaEntry) {
			private static final long serialVersionUID = 1L;
			public Point getToolTipLocation(MouseEvent e) {
				return new Point(25, 2);
			}
		};
		AlertaEntry3 = new JLabel(iconoAlertaEntry) {
			private static final long serialVersionUID = 1L;
			public Point getToolTipLocation(MouseEvent e) {
				return new Point(25, 2);
			}
		};
		AlertaEntry4 = new JLabel(iconoAlertaEntry) {
			private static final long serialVersionUID = 1L;
			public Point getToolTipLocation(MouseEvent e) {
				return new Point(25, 2);
			}
		};
		Timer llamarAtencionAlertaEntry = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (iconoAlertaEntry_path.endsWith("AlertaEntry.png")) {
					iconoAlertaEntry_path = posicion_archivos + "/AlertaEntry_.png";
				} else {
					iconoAlertaEntry_path = posicion_archivos + "/AlertaEntry.png";
				}

				String iconoFinal = iconoAlertaEntry_path;
				if (cargandoDatos || guardandoDatos) {
					String palabra;
					if (cargandoDatos) {
						palabra = "Cargando";
					}
					else {
						palabra = "Guardando";
					}
					AlertaEntry1.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">" + palabra + " datos, espere por favor</font></p></html>");
					AlertaEntry2.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">" + palabra + " datos, espere por favor</font></p></html>");
					AlertaEntry3.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">" + palabra + " datos, espere por favor</font></p></html>");
					AlertaEntry4.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">" + palabra + " datos, espere por favor</font></p></html>");
					AlertaEntry5.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">" + palabra + " datos, espere por favor</font></p></html>");

					iconoFinal = posicion_archivos + "ajax-loader.gif";
				}
				else {
					AlertaEntry1.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">Ingrese un nombre</font></p></html>");
					AlertaEntry2.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">Ingrese un apellido</font></p></html>");
					AlertaEntry3.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">Ingrese una ciudad</font></p></html>");
					AlertaEntry4.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">Ingrese una edad (entera)</font></p></html>");
				}
				iconoAlertaEntry = CrearIcono(iconoFinal, 20, 20, true, false);
				if (ManejandoDatos) {
					return;
				}
				AlertaEntry1.setIcon(iconoAlertaEntry);
				AlertaEntry2.setIcon(iconoAlertaEntry);
				AlertaEntry3.setIcon(iconoAlertaEntry);
				AlertaEntry4.setIcon(iconoAlertaEntry);
				AlertaEntry5.setIcon(iconoAlertaEntry);
				if (cargandoDatos || guardandoDatos) {
					ManejandoDatos = true;
				}
			}
		});

		CambioNombre = new JTextField();
		CambioNombre.setFont(new Font("Nyala", Font.PLAIN, 25));
		CambioNombre.setBounds(780, 125, 200, 30);
		getContentPane().add(CambioNombre);
		CambioNombre.setColumns(10);
		CambioNombre.setLayout(new BorderLayout());
		CambioNombre.add(AlertaEntry1, BorderLayout.EAST);

		CambioNombre.setDocument(new LimiteTexto(30));

		CambioApellido = new JTextField();
		CambioApellido.setFont(fuente_25);
		CambioApellido.setColumns(10);
		CambioApellido.setBounds(780, 195, 200, 30);
		getContentPane().add(CambioApellido);
		CambioApellido.setLayout(new BorderLayout());
		CambioApellido.add(AlertaEntry2, BorderLayout.EAST);

		CambioApellido.setDocument(new LimiteTexto(30));

		CambioCiudad = new JTextField();
		CambioCiudad.setFont(fuente_25);
		CambioCiudad.setBounds(780, 335, 200, 30);
		getContentPane().add(CambioCiudad);
		CambioCiudad.setLayout(new BorderLayout());
		CambioCiudad.add(AlertaEntry3, BorderLayout.EAST);

		CambioCiudad.setDocument(new LimiteTexto(30));

		lblSexo = new JLabel("Sexo");
		lblSexo.setForeground(Color.WHITE);
		lblSexo.setBounds(655, 390, 100, 40);
		lblSexo.setFont(fuente_30);
		getContentPane().add(lblSexo);

		Sexo = new JComboBox < String > ();
		Sexo.setFocusable(false);

		Sexo.setForeground(Color.BLACK);
		Sexo.setBackground(Color.WHITE);

		Sexo.addItem("Sin especificar");
		Sexo.addItem("Masculino");
		Sexo.addItem("Femenino");

		Sexo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nuevoSexo = Sexo.getSelectedItem().toString();
				boolean imagenCambiada = (pathFoto != pathFotoDefecto && pathFoto != pathFotoDefectoHombre && pathFoto != pathFotoDefectoMujer);
				if (!imagenCambiada) {
					if (nuevoSexo == "Femenino") {
						pathFoto = pathFotoDefectoMujer;
					} else if (nuevoSexo == "Masculino") {
						pathFoto = pathFotoDefectoHombre;
					} else {
						pathFoto = pathFotoDefecto;
					}
					FotoDePerfil.setIcon(CrearIcono(pathFoto, 150, -1, true, false));
				}
			}
		});

		Sexo.setBounds(780, 400, 200, 30);
		getContentPane().add(Sexo);
		
		AlertaEntry5 = new JLabel((Icon) null) {
			private static final long serialVersionUID = 1L;

			public Point getToolTipLocation(MouseEvent e) {
				return new Point(25, 2);
			}
		};
		AlertaEntry5.setToolTipText("<html><p><font size=\"5\" face=\"Nyala\">Cargando datos, espere por favor</font></p></html>");
		AlertaEntry5.setBounds(988, 405, 20, 20);
		getContentPane().add(AlertaEntry5);

		CambioEdad = new JFormattedTextField();
		CambioEdad.setFont(fuente_25);
		CambioEdad.setBounds(780, 265, 200, 30);
		getContentPane().add(CambioEdad);
		CambioEdad.setLayout(new BorderLayout());
		CambioEdad.add(AlertaEntry4, BorderLayout.EAST);
		CambioEdad.setDocument(new SoloEnteros(2));
		CambioEdad.getDocument().putProperty("padre", CambioEdad);
		CambioEdad.getDocument().putProperty("alerta", AlertaEntry4);

		labelGatito1 = new JLabel("");
		labelGatito1.setIcon(new ImageIcon(posicion_archivos + "/Friki.png"));
		labelGatito1.setBounds(702, 351, 436, 331);
		getContentPane().add(labelGatito1);

		Siguiente = new JButton("Siguiente");
		Siguiente.setFont(fuente_25);
		Siguiente.setBounds(1124, 636, 130, 35);
		getContentPane().add(Siguiente);

		lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 1280, 720);
		getContentPane().add(lblFondo);
		lblFondo.setIcon(new ImageIcon(posicion_archivos + "/Fondo.jpg"));

		llamarAtencionAlertaEntry.start();

		Timer chequearAlertas = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (CambioNombre.getText().isEmpty()) {
					AlertaEntry1.setVisible(true);
				}
				else {
					AlertaEntry1.setVisible(false);
				}

				if (CambioApellido.getText().isEmpty()) {
					AlertaEntry2.setVisible(true);
				}
				else {
					AlertaEntry2.setVisible(false);
				}

				if (CambioEdad.getText().isEmpty()) {
					AlertaEntry4.setVisible(true);
				}
				else {
					AlertaEntry4.setVisible(false);
				}

				if (CambioCiudad.getText().isEmpty()) {
					AlertaEntry3.setVisible(true);
				}
				else {
					AlertaEntry3.setVisible(false);
				}
			}
		});
		
		// Obtener datos desde la base de datos.
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				// Mientras cargamos datos, no se toca nada ;)
				Siguiente.setEnabled(false);

				AlertaEntry5.setVisible(true);
				CambiarFoto.setEnabled(false);
				CambioNombre.setEnabled(false);
				CambioCiudad.setEnabled(false);
				CambioApellido.setEnabled(false);
				CambioEdad.setEnabled(false);
				Sexo.setEnabled(false);

				try {
					cargandoDatos = true;
					ObtenerDatos();
				} catch (NullPointerException | SQLException | IOException | BadLocationException e) {
					System.out.println("Sin conexión a db, probando.");
				}

				String antesLoader = pathFoto;
				try {
					obteniendoFoto = true;
					FotoDePerfil.setHorizontalAlignment(SwingConstants.CENTER);
					FotoDePerfil.setIcon(new ImageIcon(posicion_archivos + "ajax-loader.gif"));
					ObtenerFoto();
					antesLoader = pathFoto;
				} catch (NullPointerException | SQLException | IOException e) {
					System.out.println("No hay foto aún.");
				}
				finally {
					obteniendoFoto = false;
					cargandoDatos = false;
				}

				FotoDePerfil.setHorizontalAlignment(SwingConstants.LEADING);
				FotoDePerfil.setIcon(CrearIcono(antesLoader, 150, -1, true, false));

				CambiarFoto.setEnabled(true);
				CambioNombre.setEnabled(true);
				CambioCiudad.setEnabled(true);
				CambioApellido.setEnabled(true);
				CambioEdad.setEnabled(true);
				Sexo.setEnabled(true);

				chequearAlertas.start();
				AlertaEntry5.setVisible(false);
			}
		});
		t1.start();

		Timer todoLleno = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean completo = false;
				completo = (!AlertaEntry1.isVisible() && !AlertaEntry2.isVisible() && !AlertaEntry3.isVisible() && !AlertaEntry4.isVisible() && (!obteniendoFoto || !cargandoDatos));
				Siguiente.setEnabled(completo);
			}
		});
		todoLleno.start();
		
		Siguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				guardandoDatos = true;
				ManejandoDatos = false;
				chequearAlertas.stop();
				Siguiente.setEnabled(false);
				fotoDefecto.setVisible(false);
				AlertaEntry1.setVisible(true);
				AlertaEntry2.setVisible(true);
				AlertaEntry3.setVisible(true);
				AlertaEntry4.setVisible(true);
				AlertaEntry5.setVisible(true);
				CambiarFoto.setEnabled(false);
				CambioNombre.setEnabled(false);
				CambioCiudad.setEnabled(false);
				CambioApellido.setEnabled(false);
				CambioEdad.setEnabled(false);
				Sexo.setEnabled(false);
			}
		});
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

	private String elegirArchivo() {
		JFileChooser chooser = new JFileChooser();
		PanelPrevisualizacion preview = new PanelPrevisualizacion(chooser);
		chooser.setDialogTitle("Seleccione una imagen");

		String[] extensiones = ImageIO.getWriterFormatNames();
		extensiones = (String[]) unique(extensiones);
		FileFilter filtroImagen = new FileNameExtensionFilter(
			"Imágenes", extensiones);

		chooser.setCurrentDirectory(chooserPath);
		chooser.addChoosableFileFilter(filtroImagen);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setAccessory(preview);
		chooser.addPropertyChangeListener(preview);
		this.setCursor(waitCursor);
		int resultado = chooser.showOpenDialog(null);
		this.setCursor(defaultCursor);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			chooserPath = chooser.getCurrentDirectory();
			File archivoSeleccionado = chooser.getSelectedFile();
			return archivoSeleccionado.getAbsolutePath();
		} else {
			chooserPath = chooser.getCurrentDirectory();
			return null;
		}
	}

	public void ObtenerFoto() throws SQLException, IOException, NullPointerException {
		String sentencia = "select foto,extImagen from usuario where usuario='" + idUsuario + "';";
		ResultSet rs = stmt.executeQuery(sentencia);

		rs.next();
		String extImage = rs.getString(2);

		if (extImage.equals("__base_defecto__")) {
			return;
		}

		Blob datosFoto = rs.getBlob(1);

		File temp = File.createTempFile("tempfile", extImage);
		System.out.println(temp.getAbsolutePath());
		InputStream is = datosFoto.getBinaryStream();
		fos = new FileOutputStream(temp);
		int b = 0;
		while ((b = is.read()) != -1) {
			fos.write(b);
		}

		// Las paths por defectos ahora son la foto del usuario.
		pathFoto = temp.getAbsolutePath();
		pathFotoDefecto = temp.getAbsolutePath();
		pathFotoDefectoHombre = temp.getAbsolutePath();
		pathFotoDefectoMujer = temp.getAbsolutePath();
		FotoDePerfil.setIcon(CrearIcono(temp.getAbsolutePath(), 150, -1, true, true));
	}

	public void ObtenerDatos() throws SQLException, IOException, NullPointerException, BadLocationException {
		String sentencia = "select Nombre,Apellido,Edad,Ciudad,Sexo from usuario where usuario='" + idUsuario + "';";
		ResultSet rs = stmt.executeQuery(sentencia);
		rs.next();


		System.out.println("Datos:");
		String nombre = rs.getString(1);
		String apellido = rs.getString(2);
		int edad = rs.getInt(3);
		String ciudad = rs.getString(4);
		String sexo = rs.getString(5);

		if (!nombre.equals("__base_defecto__")) {
			CambioNombre.getDocument().insertString(0, nombre, null);
		}

		if (!apellido.equals("__base_defecto__")) {
			CambioApellido.setText(apellido);
		}

		if (!ciudad.equals("__base_defecto__")) {
			CambioCiudad.setText(ciudad);
		}

		if (!(edad == -1)) {
			CambioEdad.setText(Integer.toString(edad));
		}

		String[] posibles = new String[3];
		posibles[0] = "Sin especificar";
		posibles[1] = "Masculino";
		posibles[2] = "Femenino";
		int pos;
		for (pos = 0; pos < 3; pos++) {
			if (posibles[pos].equals(sexo)) {
				break;
			}
		}
		Sexo.setSelectedIndex(pos);
	}

	public static String[] unique(String[] strings) {
		Set < String > set = new HashSet < String > ();
		for (int i = 0; i < strings.length; i++) {
			String name = strings[i].toLowerCase();
			if (name.contains("gif")) {
				continue;
			}
			set.add(name);
		}
		return (String[]) set.toArray(new String[0]);
	}
}