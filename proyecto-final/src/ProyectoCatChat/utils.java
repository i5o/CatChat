package ProyectoCatChat;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.List;
import java.awt.Point;
import java.awt.Toolkit;
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
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.json.JSONArray;
import org.json.JSONObject;


public class utils {
	static Connection conexion = null;
	static String posicion_archivos = new File("archivos/").getAbsolutePath().replace("\\", "/") + "/";
	static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
	static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
	static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
	static String iconoAlertaEntry_path = posicion_archivos  + "AlertaEntry.png";
	
	public static void main(Connection conexion_) {
		conexion = conexion_;
	}

	public static String ObtenerFoto(String usuario) throws SQLException, IOException, NullPointerException {
		String sentencia = "select foto,extImagen from usuario where usuario='" + usuario + "';";
		Statement stmt_foto = conexion.createStatement();
		ResultSet rs = stmt_foto.executeQuery(sentencia);
	
		rs.next();
		String extImage = rs.getString(2);
	
		if (extImage.equals("__base_defecto__")) {
			return "?";
		}
	
		Blob datosFoto = rs.getBlob(1);
	
		File temp = File.createTempFile("tempFotoCatChat", extImage);
		System.out.println(temp.getAbsolutePath());
		InputStream is = datosFoto.getBinaryStream();
		FileOutputStream fos = new FileOutputStream(temp);
		int b = 0;
		while ((b = is.read()) != -1) {
			fos.write(b);
		}
	
		return temp.getAbsolutePath();
	}

	public static Object[] ObtenerDatosEdicion(String usuario) throws SQLException {
		String sentencia = "select Nombre,Apellido,Edad,Ciudad,Sexo from usuario where usuario='" + usuario + "';";
		Statement stmt_datos = conexion.createStatement();
		ResultSet rs = stmt_datos.executeQuery(sentencia);
		rs.next();

		String nombre = rs.getString(1);
		String apellido = rs.getString(2);
		int edad = rs.getInt(3);
		String ciudad = rs.getString(4);
		String sexo = rs.getString(5);
		
		Object[] datos = new Object[5];
		datos[0] = nombre;
		datos[1] = apellido;
		datos[2] = edad;
		datos[3] = ciudad;
		datos[4] = sexo;
		
		return datos;
	}

	public static void Acomodar(JFrame ventana) {
		ventana.setIconImage(Toolkit.getDefaultToolkit().getImage(posicion_archivos + "Saludo.png"));
		ventana.setTitle("CatChat");
		ventana.setBounds(100, 100, 1280, 720);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.getContentPane().setLayout(null);
		ventana.setResizable(false);
		ventana.setLocationRelativeTo(null);
	}

	public static ImageIcon CrearIcono(String path, int largo, int ancho, boolean redimensionar) {
		ImageIcon icono = new ImageIcon(path);
		if (redimensionar) {
			int escalado = Image.SCALE_SMOOTH;
			if (path.endsWith(".gif")) {
				escalado = Image.SCALE_FAST;
			}
			Image img = icono.getImage();
			Image newimg = img.getScaledInstance(largo, ancho, escalado);
			ImageIcon new_icono = new ImageIcon(newimg);
			return new_icono;

		} else {
			return icono;
		}
	}
	
	public static JLabel CrearAlertaEntry(final int xTooltip, final int yTooltip) {
		ImageIcon iconoAlertaEntry = CrearIcono(iconoAlertaEntry_path, 20, 20, true);

		JLabel Alerta = new JLabel(iconoAlertaEntry) {
			private static final long serialVersionUID = 1L;

			public Point getToolTipLocation(MouseEvent e) {
				return new Point(xTooltip, yTooltip);
			}
		};
		
		return Alerta;
	}

	public static String ObtenerNombre(String user) {
		String sentencia = "select nombre,apellido from usuario where usuario='" + user + "';";
		Statement stmt_;
		String nombre = "?";
		try {
			stmt_ = conexion.createStatement();
			ResultSet rs = stmt_.executeQuery(sentencia);
			rs.next();
			nombre = rs.getString(1) + " " + rs.getString(2);
		} catch (SQLException e) {}

		return nombre;
	}

	public static String ObtenerMensajes(String user1, String user2) {
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

		return jsonmensajes;
	}
	
	public static void GuardarMensajes(JSONObject mensajes, String user1, String user2)  {
		String sentencia_guardado = "UPDATE `Mensajes` SET `texto`=? where ? IN (participante1, participante2) and ? IN (participante1, participante2);";
		PreparedStatement psmnt = null;
		try {
			psmnt = conexion.prepareStatement(sentencia_guardado);
			psmnt.setString(1, mensajes.toString());
			psmnt.setString(2, user1);
			psmnt.setString(3, user2);
			psmnt.executeUpdate();
		} catch (SQLException e) {}
	}

	public static ArrayList<String> ObtenerUsuarios() {
		String sentencia = "select usuario from usuario where registroCompleto=1;";
		Statement stmt_addusers;
		ArrayList<String> usuarios= null;
		try {
			stmt_addusers = conexion.createStatement();
			ResultSet rs = stmt_addusers.executeQuery(sentencia);
			usuarios = new ArrayList<String>();
			while (rs.next()) {
				String nombreUsuario = rs.getString(1);
				usuarios.add(nombreUsuario);
			}

		} catch (SQLException e) {
		}

		return usuarios;
	}

	public static String[] MensajesNuevos(JSONArray mensajes_antes, String user1, String user2) {
		String[] mensajes_antes_str = new String[mensajes_antes.length()];
		int p = 0;
		for (final Object x: mensajes_antes) {
			mensajes_antes_str[p] = x.toString();
			p++;
		}

		String jsonMensajes_ = ObtenerMensajes(user1, user2);
		JSONObject mensajes = new JSONObject(jsonMensajes_);
		JSONArray mensajes_json = mensajes.getJSONArray("Mensajes");

		String[] mensajes_ahora_str = new String[mensajes_json.length()];
		p = 0;
		for (final Object x: mensajes_json) {
			mensajes_ahora_str[p] = x.toString();
			p++;
		}

		String[] mensajes_json_nuevos = new String[mensajes_json.length()];
		
		int xy = 0;
		for (String x: mensajes_ahora_str) {
			if (!Arrays.asList(mensajes_antes_str).contains(x)) {
				mensajes_antes = mensajes_json;
				mensajes_json_nuevos[xy] = "{\"Mensaje\": " + x + "}";
				xy++;
			}
		}

		return mensajes_json_nuevos;
	}
}