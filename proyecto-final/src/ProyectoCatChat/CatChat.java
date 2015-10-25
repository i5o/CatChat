package ProyectoCatChat;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class CatChat {

	static Statement stmt = null;
	static String posicion_archivos = new File("archivos/").getAbsolutePath() + "\\";
	static Connection conexion = null;

	static VentanaLogin ventanaLogin;
	static VentanaDatos ventanaDatos;
	static VentanaMensajes ventanaMensajes;
	static String Usuario;

	static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

	public static void main(String[] args) {

		try {
			EstablecerConexionMySql();
		} catch (SQLException e) {
			System.out.println("Imposible conectar a la base de datos.\nSi no hay conexión, no hay programa :/");
			System.exit(0);
		}
		ventanaLogin = new VentanaLogin();
		ventanaLogin.setVisible(true);

		ventanaLogin.Registrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Registro();
					if (ventanaLogin.UsuarioEnUso.isVisible() || ventanaLogin.EmailEnUso.isVisible()) {
						return;
					}
					llamarVentanaDatos();

				} catch (SQLException | IOException e) {
				}
			}
		});

		ventanaLogin.Entrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (Login()) {
						if (DebeLlenarDatos()) {
							llamarVentanaDatos();
						} else {
							llamarVentanaMensajes();
						}
					}
				} catch (SQLException e) {
				}
			}
		});
	}

	public static void EstablecerConexionMySql() throws SQLException {
		String url = "jdbc:mysql://kuckuck.treehouse.su:3306/proyecto?user=ignacioutu&password=ProyectoDeUTU2015";
		//String url = "jdbc:mysql://192.168.2.158:3306/test_proyecto?user=estudiante04&password=Estudiante04";

		System.out.println("Intentando conectar.");
		conexion = DriverManager.getConnection(url);
		System.out.println("Conexión establecida");

		Runtime.getRuntime().addShutdownHook(new Thread() {@Override
			public void run() {
				try {
					conexion.close();
					System.out.println("Conexión cerrada");
				} catch (SQLException e) {
					System.out.println("Conexión cerrada");
				}
			}
		});

	}

	public static void Registro() throws SQLException, IOException {
		String sentencia = "INSERT INTO `usuario` (`usuario`, `contraseña`, `email`) VALUES (?, ?, ?);";
		PreparedStatement psmnt = null;
		String nuevoEmail = ventanaLogin.nuevoEmail.getText();
		String nuevoPassword = ventanaLogin.nuevoPassword.getText();
		String nuevoUsuario = ventanaLogin.nuevoUsuario.getText();

		boolean algoUsado = false;
		if (!CampoVacio("email", nuevoEmail)) {
			algoUsado = true;
			ventanaLogin.EmailEnUso.setVisible(true);
		}
		if (!CampoVacio("usuario", nuevoUsuario)) {
			algoUsado = true;
			ventanaLogin.UsuarioEnUso.setVisible(true);
		}

		if (algoUsado) {
			return;
		}

		psmnt = conexion.prepareStatement(sentencia);
		Usuario = nuevoUsuario;
		psmnt.setString(1, Usuario);
		psmnt.setString(2, nuevoPassword);
		psmnt.setString(3, nuevoEmail);

		psmnt.executeUpdate();
	}

	public static boolean Login() throws SQLException {
		String sentencia = "select usuario,contraseña from `usuario` where `usuario`=? and `contraseña`=?";
		String user = ventanaLogin.EntrarUsuario.getText();
		String password = ventanaLogin.EntrarPassword.getText();
		PreparedStatement psmnt = null;

		psmnt = conexion.prepareStatement(sentencia);
		psmnt.setString(1, user);
		psmnt.setString(2, password);

		Usuario = user;
		ResultSet resultados = psmnt.executeQuery();

		if (consultaVacia(resultados)) {
			ventanaLogin.datosIncorrectos.setVisible(true);
			return false;
		}
		resultados.next();
		resultados.getString(1);
		return true;
	}

	public static void GuardarDatos(VentanaDatos ventana) throws SQLException, IOException {
		String sentencia = "UPDATE `usuario` SET `sexo`=?, `edad`=?, `Nombre`=?, `Apellido`=?, `Ciudad`=?, `foto`=?, `extImagen`=?, `registroCompleto`=? WHERE `usuario`=?";
		PreparedStatement psmnt = null;

		String extension = "";
		String imagen = ventana.pathFoto;
		int i = imagen.lastIndexOf(".");
		if (i >= 0) {
			extension = imagen.substring(i);
		}
		psmnt = conexion.prepareStatement(sentencia);

		psmnt.setString(1, ventana.Sexo.getSelectedItem().toString());
		psmnt.setInt(2, Integer.parseInt(ventana.CambioEdad.getText()));
		psmnt.setString(3, ventana.CambioNombre.getText());
		psmnt.setString(4, ventana.CambioApellido.getText());
		psmnt.setString(5, ventana.CambioCiudad.getText());
		psmnt.setString(7, extension);
		psmnt.setString(8, "1");
		psmnt.setString(9, Usuario);

		FileInputStream fin = new FileInputStream(imagen);
		psmnt.setBinaryStream(6, fin, fin.available());

		psmnt.executeUpdate();
	}

	public static boolean CampoVacio(String campo, String valor) throws SQLException {
		String sentencia = "select " + campo + " from usuario where " + campo + "='" + valor + "';";
		Statement stmt_datosvacios = conexion.createStatement();
		ResultSet rs = stmt_datosvacios.executeQuery(sentencia);
		return consultaVacia(rs);
	}

	public static boolean consultaVacia(ResultSet rs) throws SQLException {
		// Devuelve True si la consulta hecha no tiene campos
		return (!rs.isBeforeFirst() && rs.getRow() == 0);
	}

	public static void llamarVentanaDatos() {
		ventanaLogin.llamarAtencionAlertaEntry.stop();
		ventanaLogin.datosEntradaLlenos.stop();
		ventanaLogin.datosRegistroLlenos.stop();
		ventanaLogin.llamarAtencionInfo.stop();
		ventanaLogin.setVisible(false);
		VentanaDatos ventanaDatos = new VentanaDatos(Usuario, conexion);
		ventanaDatos.setVisible(true);

		VentanaDatos.Siguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread t1 = new Thread(new Runnable() {
					public void run() {
						try {
							GuardarDatos(ventanaDatos);
							ventanaDatos.todoLleno.stop();
							ventanaDatos.dispose();
							llamarVentanaMensajes();
						} catch (SQLException | IOException e) {
						}
					}
				});
				t1.start();
			}
		});
	}

	public static void llamarVentanaMensajes() {
		ventanaLogin.setVisible(false);
		ventanaMensajes = new VentanaMensajes(Usuario, conexion);
		ventanaMensajes.setVisible(true);

		ventanaMensajes.btnEditarDatos.addMouseListener(new MouseAdapter() {@Override
			public void mouseEntered(MouseEvent e) {
				ventanaMensajes.setCursor(handCursor);
			}@Override
			public void mouseExited(MouseEvent e) {
				ventanaMensajes.setCursor(defaultCursor);
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				llamarVentanaDatos();
				ventanaMensajes.dispose();
			}
		});
	}

	public static boolean DebeLlenarDatos() throws SQLException {
		boolean debe = false;
		String sentencia = "select registroCompleto from usuario where usuario='" + Usuario + "';";
		Statement stmt_llenardatos = conexion.createStatement();
		try {
			ResultSet rs = stmt_llenardatos.executeQuery(sentencia);
			rs.next();
			if (rs.getString(1).equals("1")) {
				debe = false;
			} else {
				debe = true;
			}
		} catch (SQLException e) {
			debe = true;
		}
		return debe;
	}
}