package ProyectoCatChat;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Timer;


public class CatChat {

	static Statement stmt = null;
	static String posicion_archivos = new File("archivos/").getAbsolutePath() + "\\";
	static Connection conexion = null;

	static boolean Desarrollo;

	static VentanaLogin ventanaInicial;
	static VentanaDatos ventanaSecundaria;
	static String Usuario;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Desarrollo = true;

		try {
			EstablecerConexionMySql();
		} catch (SQLException e) {
			if (!Desarrollo) {
				e.printStackTrace();
				System.out.println("Imposible conectar a la base de datos.\nSi no hay conexión, no hay programa :/");
				System.exit(0);
			}
		}
		ventanaInicial = new VentanaLogin();
		ventanaInicial.setVisible(true);

		ventanaInicial.Registrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Registro();
					if (ventanaInicial.UsuarioEnUso.isVisible() || ventanaInicial.EmailEnUso.isVisible()) {
						return;
					}
					llamarVentanaSecundaria();

				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		ventanaInicial.Entrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (Login()) {
						if (DebeLlenarDatos()) {
							llamarVentanaSecundaria();
						}
						else {
							System.out.println("Aún no implementado, mensajes 3:");
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void EstablecerConexionMySql() throws SQLException {
		String url = "jdbc:mysql://kuckuck.treehouse.su:3306/proyecto?user=ignacioutu&password=ProyectoDeUTU2015";

		System.out.println("Intentando conectar.");
		conexion = DriverManager.getConnection(url);
		stmt = conexion.createStatement();
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
		String nuevoEmail = ventanaInicial.nuevoEmail.getText();
		String nuevoPassword = ventanaInicial.nuevoPassword.getText();
		String nuevoUsuario = ventanaInicial.nuevoUsuario.getText();

		boolean algoUsado = false;
		if (!CampoVacio("email", nuevoEmail)) {
			algoUsado = true;
			ventanaInicial.EmailEnUso.setVisible(true);
		}
		if (!CampoVacio("usuario", nuevoUsuario)) {
			algoUsado = true;
			ventanaInicial.UsuarioEnUso.setVisible(true);
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
		String user = ventanaInicial.EntrarUsuario.getText();
		String password = ventanaInicial.EntrarPassword.getText();
		PreparedStatement psmnt = null;
		
		psmnt = conexion.prepareStatement(sentencia);
		psmnt.setString(1, user);
		psmnt.setString(2, password);

		Usuario = user;
		ResultSet resultados = psmnt.executeQuery();
		
		if (consultaVacia(resultados)) {
			ventanaInicial.datosIncorrectos.setVisible(true);
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
		ResultSet rs = stmt.executeQuery(sentencia);
		return consultaVacia(rs);
	}

	public static boolean consultaVacia(ResultSet rs) throws SQLException {
		// Devuelve True si la consulta hecha no tiene campos
	    return (!rs.isBeforeFirst() && rs.getRow() == 0);
	}
	
	public static void llamarVentanaSecundaria() {
		ventanaInicial.llamarAtencionAlertaEntry.stop();
		ventanaInicial.datosEntradaLlenos.stop();
		ventanaInicial.datosRegistroLlenos.stop();
		ventanaInicial.llamarAtencionInfo.stop();
		ventanaInicial.setVisible(false);
		VentanaDatos ventanaSecundaria = new VentanaDatos(Usuario, conexion, stmt, true);
		ventanaSecundaria.setVisible(true);

		ventanaSecundaria.Siguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread t1 = new Thread(new Runnable() {
					public void run() {
						try {
							GuardarDatos(ventanaSecundaria);
							ventanaSecundaria.dispose();
						} catch (SQLException | IOException e) {
							e.printStackTrace();
						}
					}
				});
				t1.start();
			}
		});
	}
	
	public static boolean DebeLlenarDatos() {
		boolean debe = false;
		String sentencia = "select registroCompleto from usuario where usuario='" + Usuario + "';";
		try {
			ResultSet rs = stmt.executeQuery(sentencia);
			rs.next();
			if (rs.getString(1).equals("1")) {
				debe = false;
			}
			else {
				debe = true;
			}
		} catch (SQLException e) {
			debe = true;
		}
		return debe;
	}
}