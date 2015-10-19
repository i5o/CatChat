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

	static String sentencia = null;
	static Statement stmt = null;
	static String posicion_archivos = new File("archivos/").getAbsolutePath() + "\\";
	static Connection conexion = null;

	static boolean Desarrollo;

	static VentanaLogin ventanaInicial;
	static VentanaDatos ventanaSecundaria;
	static String Usuario;
	static Color colorLabel = Color.decode("#EF6161");
	static Timer llamarAtencion = null;
	static String usuario;

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
					int resultado_registro = Registro();
					System.out.println(resultado_registro);
					if (resultado_registro > 0) {
						ventanaInicial.setVisible(false);
						VentanaDatos ventanaSecundaria = new VentanaDatos(usuario, conexion, stmt, true);
						ventanaSecundaria.setVisible(true);
						ventanaSecundaria.Siguiente.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								try {
									GuardarDatos(ventanaSecundaria);
								} catch (SQLException | IOException e) {
									e.printStackTrace();
								}
							}});
					}

				} catch (SQLException | IOException e) {
					String error = e.toString();
					System.out.println(error);
					if (error.contains("Duplicate entry")) {
						System.out.println("Ya existe el usuario");
					}
				}
			}
		});

		llamarAtencion = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int rojo = colorLabel.getRed();
				if (rojo == 239) {
					colorLabel = Color.BLACK;
				} else {
					colorLabel = Color.decode("#EF6161");
				}
				ventanaInicial.datosIncorrectos.setForeground(colorLabel);
			}
		});


		ventanaInicial.Entrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Login();
					ventanaInicial.datosIncorrectos.setVisible(false);
					VentanaDatos ventanaSecundaria = new VentanaDatos(usuario, conexion, stmt, true);
					ventanaSecundaria.setVisible(true);
					ventanaSecundaria.Siguiente.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							try {
								GuardarDatos(ventanaSecundaria);
							} catch (SQLException | IOException e) {
								e.printStackTrace();
							}
						}});
				} catch (SQLException e) {
					String error = e.toString();
					System.out.println(error);
					if (error.contains("empty")) {
						llamarAtencion.restart();
						ventanaInicial.datosIncorrectos.setVisible(true);
					}
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

	public static int Registro() throws SQLException, IOException {
		sentencia = "INSERT INTO `usuario` (`usuario`, `contraseña`, `email`) VALUES (?, ?, ?);";
		PreparedStatement psmnt = null;

		psmnt = conexion.prepareStatement(sentencia);
		Usuario = ventanaInicial.nuevoUsuario.getText();
		psmnt.setString(1, ventanaInicial.nuevoUsuario.getText());
		psmnt.setString(2, ventanaInicial.nuevoPassword.getText());
		psmnt.setString(3, ventanaInicial.nuevoEmail.getText());

		int resultado = psmnt.executeUpdate();
		return resultado;
	}

	public static void Login() throws SQLException {
		sentencia = "select usuario,contraseña from `usuario` where `usuario`=? and `contraseña`=?";
		String user = ventanaInicial.EntrarUsuario.getText();
		String password = ventanaInicial.EntrarPassword.getText();
		PreparedStatement psmnt = null;

		psmnt = conexion.prepareStatement(sentencia);
		psmnt.setString(1, user);
		psmnt.setString(2, password);

		usuario = user;
		ResultSet resultados = psmnt.executeQuery();
		resultados.next();
		resultados.getString(1);
	}
	
	public static void GuardarDatos(VentanaDatos ventana) throws SQLException, IOException {
		String sentencia;
		sentencia = "UPDATE `usuario` SET `sexo`=?, `edad`=?, `Nombre`=?, `Apellido`=?, `Ciudad`=?, `foto`=?, `extImagen`=? WHERE `usuario`=?";
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
		psmnt.setString(8, usuario);
				
		FileInputStream fin = new FileInputStream(imagen);
		psmnt.setBinaryStream(6, fin, fin.available());

		int s = psmnt.executeUpdate();
	}
}