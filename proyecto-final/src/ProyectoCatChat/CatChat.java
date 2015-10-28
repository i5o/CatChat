package ProyectoCatChat;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
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

	static Connection conexion = null;
	static String Usuario;

	public static void main(String[] args) {

		try {
			EstablecerConexionMySql();
		} catch (SQLException e) {
			System.out.println("Imposible conectar a la base de datos.\nSi no hay conexión, no hay programa :/");
			System.exit(0);
		}
		
		utils.conexion = conexion;

		llamarVentanaLogin();

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

	public static void llamarVentanaLogin() {
		final VentanaLogin ventanaLogin = new VentanaLogin();
		ventanaLogin.setVisible(true);

		ventanaLogin.Registrarse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Usuario = ventanaLogin.nuevoUsuario.getText();
				utils.Registro(ventanaLogin);
				llamarVentanaDatos();
			}
		});

		ventanaLogin.Entrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Usuario = ventanaLogin.EntrarUsuario.getText();
				if (utils.Login(ventanaLogin)) {
					ventanaLogin.setVisible(false);
					if (utils.DebeLlenarDatos(Usuario)) {
						llamarVentanaDatos();
					} else {
						llamarVentanaMensajes();
					}
				}
			}
		});
		
	}
	
	public static void llamarVentanaDatos() {
		final VentanaDatos ventanaDatos = new VentanaDatos(Usuario);
		ventanaDatos.setVisible(true);

		ventanaDatos.Siguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread t1 = new Thread(new Runnable() {
					public void run() {
						utils.GuardarDatos(ventanaDatos);
						ventanaDatos.dispose();
						llamarVentanaMensajes();
					}
				});
				t1.start();
			}
		});
	}

	public static void llamarVentanaMensajes() {
		final VentanaMensajes ventanaMensajes = new VentanaMensajes(Usuario);
		ventanaMensajes.setVisible(true);

		ventanaMensajes.btnEditarDatos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				llamarVentanaDatos();
				ventanaMensajes.dispose();
			}
		});
	}
}