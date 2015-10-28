package ProyectoCatChat;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
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
    static Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    static Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);

    static String posicion_archivos = new File("archivos/").getAbsolutePath().replace("\\", "/") + "/";
    static String iconoAlertaEntry_path = posicion_archivos + "AlertaEntry.png";
    static Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);

    /*
     * funcion principal, al crear utils.
     * utils(conexion);
     * Toma como parametro la conexion sql.
     */
    public static void main(Connection conexion_) {
        // seteo el valor de dicha conexion
        conexion = conexion_;
    }

    /*
     * Se encarga de "acomodar" la ventana
     * Es decir, le da el título, el icono y la posiciona
     * Toma como parámetro la ventana
     */
    public static void Acomodar(JFrame ventana) {
        ventana.setIconImage(Toolkit.getDefaultToolkit().getImage(posicion_archivos + "icono.png"));
        ventana.setTitle("CatChat");
        ventana.setBounds(100, 100, 1280, 720);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.getContentPane().setLayout(null);
        ventana.setResizable(false);
        ventana.setLocationRelativeTo(null);
    }

    /*
     * Devuelve si el campo 'x' con el valor 'y', está siendo usado.
     */
    public static boolean CampoUsado(String campo, String valor) {
        String sentencia = "select " + campo + " from usuario where " + campo + "='" + valor + "';";
        Statement stmt_datosvacios;
        try {
            stmt_datosvacios = conexion.createStatement();
            ResultSet rs = stmt_datosvacios.executeQuery(sentencia);
            return !(!rs.isBeforeFirst() && rs.getRow() == 0);
        }
        catch (SQLException e) {
            return true;
        }
    }

    /*
     * Crea y devuelve un JLabel que hace de "alerta"
     * Toma como parámetros:
     * - xTooltip, la distancia desde el mouse en el eje X (para el tooltip)
     * - yTooltip, la distancia desde el mouse en el eje Y (para el tooltip)
     */
    public static JLabel CrearAlertaEntry(final int xTooltip, final int yTooltip) {
        ImageIcon iconoAlertaEntry = CrearIcono(iconoAlertaEntry_path, 20, 20, true);

        JLabel Alerta = new JLabel(iconoAlertaEntry) {
            private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(xTooltip, yTooltip);
            }
        };

        return Alerta;
    }

    /*
     * Crea un ImageIcon redimensionado.
     * Es decir, redimensiona una imagen y la devuelve para poder
     * ser usada en botones, labels, etc.
     * Toma como parámetros:
     * - path de la foto
     * - largo de la foto
     * - ancho de la fonto
     * - si se quiere redimensionar, o no
     */
    public static ImageIcon CrearIcono(String path, int largo, int ancho, boolean redimensionar) {
        ImageIcon icono = new ImageIcon(path);
        if (redimensionar) {
            int escalado = Image.SCALE_SMOOTH;
            if (path.endsWith(".gif")) {
                escalado = Image.SCALE_FAST;
            }
            Image img = icono.getImage();
            Image newimg = img.getScaledInstance(largo, ancho, escalado);
            return new ImageIcon(newimg);

        }
        else {
            return icono;
        }
    }

    /*
     * Compruebo que el usuario haya llenado todos sus datos.
     * Utilizado por la ventanaLogin al momento de logueo.
     * Toma como parámetro el usuario a comprobar
     */
    public static boolean DebeLlenarDatos(String Usuario) {
        // por defecto se asume que no debe llenar los datos.
        boolean debe = false;

        // sentencia que obtiene el valor de registroCompleto,
        // donde el usuario es igual al consultado
        String sentencia = "select registroCompleto from perfil where usuario='" + Usuario + "';";

        try {
            // intérprete
            Statement stmt_llenardatos = conexion.createStatement();
            // se ejecuta la sentencia
            ResultSet rs = stmt_llenardatos.executeQuery(sentencia);
            rs.next();
            // Si el resultado es igual a 1:
            if (rs.getString(1).equals("1")) {
                // no debe llenar datos
                debe = false;
            }
            else {
                // de lo contrario, debe de
                debe = true;
            }
        }
        catch (SQLException e) {
            // en caso de que falle, asumimos que debe llenar datos
            debe = true;
        }
        return debe;
    }

    /*
     * Guarda los datos de la ventana de edición de datos.
     * Toma como parámetro la ventanaDatos
     */
    public static void GuardarDatos(VentanaDatos ventana) {
        // sentencia para actualizar los datos.
        String sentencia = "UPDATE `perfil` SET `sexo`=?, `edad`=?, `nombre`=?, `apellido`=?, `ciudad`=?, `foto`=?, `extImagen`=?, `registroCompleto`=? WHERE `usuario`=?";

        // defino la extension, que por defecto es "" (vacia)
        String extension = "";

        // defino imagen, que utiliza el path de la foto seleccionada en la ventanaDatos
        String imagen = ventana.pathFoto;

        // obtengo la extensión de la imagen
        // Se toman los últimos caracteres de el archivo (desde el último punto)
        int i = imagen.lastIndexOf(".");
        if (i >= 0) {
            extension = imagen.substring(i);
        }

        try {
            // interprete con dicha sentencia
            PreparedStatement psmnt = conexion.prepareStatement(sentencia);

            // seteo los datos
            psmnt.setString(1, ventana.Sexo.getSelectedItem().toString());
            psmnt.setInt(2, Integer.parseInt(ventana.CambioEdad.getText()));
            psmnt.setString(3, ventana.CambioNombre.getText());
            psmnt.setString(4, ventana.CambioApellido.getText());
            psmnt.setString(5, ventana.CambioCiudad.getText());
            psmnt.setString(7, extension);
            psmnt.setString(8, "1");
            psmnt.setString(9, ventana.usuario);

            // El tipo de dato blob necesita ser subido con este método
            FileInputStream fin = new FileInputStream(imagen);
            psmnt.setBinaryStream(6, fin, fin.available());

            // Ejecuto la sentencia, (guardo datos)
            psmnt.executeUpdate();
        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Guarda el json en la base de datos, para luego poder ser cargado.
     * Toma como parámetros:
     * - JSONObject mensajes, los mensajes en formato JSON.
     * - participante1
     * - participante2
     */
    public static void GuardarMensajes(JSONObject mensajes, String user1, String user2) {
        // sentencia sql que permite actualizar la informacion
        String sentencia_guardado = "UPDATE `mensajes` SET `texto`=? where ? IN (participante1, participante2) and ? IN (participante1, participante2);";

        try {
            // Creo la consulta
            PreparedStatement psmnt = conexion.prepareStatement(sentencia_guardado);
            // Seteo valores
            psmnt.setString(1, mensajes.toString());
            psmnt.setString(2, user1);
            psmnt.setString(3, user2);
            // Ejecuto consulta
            psmnt.executeUpdate();
        }
        catch (SQLException e) {
        }
    }

    /*
     * Chequea los datos del usuario y devuelve
     * verdadero si los datos son correctos.
     * Toma como parámetro la ventanaLogin.
     */
    public static boolean Login(VentanaLogin ventana) {
        // sentencia sql que se encarga de comprobar que los datos sean correctos
        String sentencia = "select usuario,contraseña from `usuario` where `usuario`=? and `contraseña`=?";

        // usuario y contraseña obtenido desde la ventana de datos.
        String user = ventana.EntrarUsuario.getText();
        String password = ventana.EntrarPassword.getText();

        try {
            // intérprete con dicha sentencia
            PreparedStatement psmnt = conexion.prepareStatement(sentencia);

            // seteo los datos
            psmnt.setString(1, user);
            psmnt.setString(2, password);

            ResultSet resultados = psmnt.executeQuery();

            // Comprobamos que el resultado de la consulta no este vacía
            if (!resultados.isBeforeFirst() && resultados.getRow() == 0) {
                // se muestra la alerta que notifica al usuario que los datos son incorrectos
                // y se devuelve falso.
                ventana.datosIncorrectos.setVisible(true);
                return false;
            }

            // Si los datos son correctos, deberíamos poder acceder a la primer fila
            resultados.next();
            resultados.getString(1);

            // los datos son correctos
            return true;
        }
        catch (SQLException e) {
            // error, asumimos que es falso
            return false;
        }

    }

    /*
     * Comparo el array viejo de los mensajes, y obtengo el nuevo
     * Devuelvo la diferencia de ellos
     * Usado para obtener los mensajes nuevos
     * Toma como parámetros:
     * - El array de mensajes "viejo"
     * - participante1
     * - participante2
     */
    public static String[] MensajesNuevos(JSONArray mensajes_antes, String user1, String user2) {
        // Paso todos los mensajes viejos en formato texto y los guardo en un array
        String[] mensajes_antes_str = new String[mensajes_antes.length()];
        int p = 0;
        for (final Object x : mensajes_antes) {
            mensajes_antes_str[p] = x.toString();
            p++;
        }

        // Obtengo los mensajes nuevos
        String jsonMensajes_ = ObtenerMensajes(user1, user2);
        // Creo un objeto json con dichos mensajes
        JSONObject mensajes = new JSONObject(jsonMensajes_);
        // Obtengo el json específico de los mensajes
        JSONArray mensajes_json = mensajes.getJSONArray("Mensajes");

        // Paso todos los mensajes en formato texto y los guardo en un array
        String[] mensajes_ahora_str = new String[mensajes_json.length()];
        p = 0;
        for (final Object x : mensajes_json) {
            mensajes_ahora_str[p] = x.toString();
            p++;
        }

        // Creo un array para los mensajes nuevos.
        String[] mensajes_json_nuevos = new String[mensajes_json.length()];

        // Variable xy, solo para saber la posición
        int xy = 0;
        for (String x : mensajes_ahora_str) { // for para los items de mensajes nuevos.
            if (!Arrays.asList(mensajes_antes_str).contains(x)) { // Si los mensajes anteriores no contienen este mensaje nuevo
                mensajes_json_nuevos[xy] = "{\"Mensaje\": " + x + "}"; // Creo un String simulando un json con el mensaje nuevo
                xy++;
            }
        }

        // devuelvo los mensajes nuevos
        return mensajes_json_nuevos;
    }

    /*
     * Función que devuelve una matriz con los datos del usuario
     * para la ventana de edición de datos.
     * Toma como parámetro el usuario
     */
    public static Object[] ObtenerDatosEdicion(String usuario) throws SQLException {
        String sentencia = "select nombre,apellido,edad,ciudad,sexo from perfil where usuario='" + usuario + "';";
        Statement stmt_datos = conexion.createStatement();
        ResultSet rs = stmt_datos.executeQuery(sentencia);
        rs.next();

        String nombre = rs.getString(1);
        String apellido = rs.getString(2);
        int edad = rs.getInt(3);
        String ciudad = rs.getString(4);
        String sexo = rs.getString(5);

        Object[] datos = { nombre, apellido, edad, ciudad, sexo };

        return datos;
    }

    /*
     * Función que devuelve el path de la foto
     * Dicha foto se encuentra en la base de dato.
     * Toma como parámetro el usuario
     */
    public static String ObtenerFoto(String usuario) throws SQLException, IOException, NullPointerException {
        // sentencia sql que selecciona la foto y su extensión.
        String sentencia = "select foto,extImagen from perfil where usuario='" + usuario + "';";
        // Se crea el intérprete
        Statement stmt_foto = conexion.createStatement();
        // Se ejecuta la consulta.
        ResultSet rs = stmt_foto.executeQuery(sentencia);

        // utilizo next(), para chequear de que hay un dato, de lo contrario se
        // lanza un error.
        rs.next();

        // obtengo la extensión de la imagen
        String extImage = rs.getString(2);

        // Si la extensión de la imagen es nula, la foto aún no fue puesta
        if (extImage.equals("")) {
            // por lo que se devuelve un path no existente
            return "?";
        }

        // obtenemos los datos Blob (tipo de dato para guardar fotos)
        Blob datosFoto = rs.getBlob(1);

        // Creo un archivo temporal
        File temp = File.createTempFile("tempFotoCatChat", extImage);
        // y obtengo los datos de la foto (desde la base de datos)
        InputStream is = datosFoto.getBinaryStream();
        // Lo usamos para escribir datos
        FileOutputStream fos = new FileOutputStream(temp);

        // Escribo los datos al archivo
        int b = 0;
        while ((b = is.read()) != -1) {
            fos.write(b);
        }

        // Devuelvo el path de la foto
        return temp.getAbsolutePath();
    }

    /*
     * Devuelve el JSON con los mensajes obtenido desde la base de datos
     * Toma como parámetros el participante1, y el participante2.
     */
    public static String ObtenerMensajes(String user1, String user2) {
        // Sentencias que permiten buscar y agregar código JSON
        String sentencia_busqueda = "select texto from mensajes where '" + user1
                + "' IN (participante1, participante2) and '" + user2 + "' IN (participante1, participante2);";
        String sentencia_agregado = "INSERT INTO `mensajes` (`participante1`, `participante2`, `texto`, `id`) VALUES (?, ?, ?, ?);";

        // El String json por defecto ( en caso de que falle. )
        String jsonmensajes = "{ 'Mensajes': [] }";

        try {
            // Intérprete & ejecutar consulta
            Statement stmt_ = conexion.createStatement();
            ResultSet rs = stmt_.executeQuery(sentencia_busqueda);
            rs.next();
            // INTENTO obtener los mensajes.
            jsonmensajes = rs.getString(1);
        }
        catch (SQLException e) {
            // Falló obteniendo mensajes, quiere decir que aún no han chateado.
            String error = e.toString();
            // Se crea un registro nuevo en la base de datos.
            if (error.contains("Illegal operation on empty result set.")) {
                PreparedStatement psmnt;
                try {
                    // Se crea una consulta, y se le agregan los valores.
                    psmnt = conexion.prepareStatement(sentencia_agregado);
                    psmnt.setString(1, user1);
                    psmnt.setString(2, user2);
                    psmnt.setString(3, jsonmensajes);
                    psmnt.setString(4, user1 + "-" + user2);
                    // Se ejecuta dicha consulta.
                    psmnt.executeUpdate();
                }
                catch (SQLException e1) {
                }
            }
        }

        // Se devuelve el json
        return jsonmensajes;
    }

    /*
     * Devuelve el nombre y el apellido (separados por un espacio)
     * de x usuario desde la base de datos.
     * Toma como parámetros el usuario.
     */
    public static String ObtenerNombre(String user) {
        // sentencia sql que selecciona el nombre y el apellido
        String sentencia = "select nombre,apellido from perfil where usuario='" + user + "';";

        // El nombre por defecto es "?", significando que no se pudo cargar el nombre.
        String nombre = "?";
        try {
            // Creo intérprete y ejecuto la consulta.
            Statement stmt_ = conexion.createStatement();
            ResultSet rs = stmt_.executeQuery(sentencia);
            rs.next();
            // Obtengo el nombre.
            nombre = rs.getString(1) + " " + rs.getString(2);
        }
        catch (SQLException e) {
        }

        return nombre;
    }

    /*
     * Obtengo los usuarios que han completado su registro
     * No toma parámetros.
     * Devuelve un ArrayList con los nombres de los usuarios.
     */
    public static ArrayList<String> ObtenerUsuarios() {
        // sentencia sql que me permite conocer los usuarios que han completado el registro
        String sentencia = "select usuario from perfil where registroCompleto=1;";

        ArrayList<String> usuarios = null;

        try {
            // Creo la consulta
            Statement stmt_addusers = conexion.createStatement();
            // Ejecuto la consulta
            ResultSet rs = stmt_addusers.executeQuery(sentencia);
            // redefino el arraylist.
            usuarios = new ArrayList<String>();

            while (rs.next()) { // mientras que haya un usuario siguiente
                // Obtengo el nombre
                String nombreUsuario = rs.getString(1);
                // y lo guardo
                usuarios.add(nombreUsuario);
            }

        }
        catch (SQLException e) {
        }

        // devuelvo el ArrayList con los datos
        return usuarios;
    }

    /*
     * Registra al usuario en la base de datos
     * Toma como parámetro la ventanaLogin.
     */
    public static void Registro(VentanaLogin ventana) {
        // Sentencia para registrar al usuario
        String sentencia = "INSERT INTO `usuario` (`usuario`, `contraseña`, `email`) VALUES (?, ?, ?)";

        // datos del usuario para registrarse
        String nuevoEmail = ventana.nuevoEmail.getText();
        String nuevoPassword = ventana.nuevoPassword.getText();
        String nuevoUsuario = ventana.nuevoUsuario.getText();

        // booleano algoUsado, se vuelve verdadero en caso de que
        // los datos: usuario y/o email estén registrados en el sistema
        // ya que, usuario es llave primaria, y email es unica
        boolean algoUsado = false;

        if (CampoUsado("email", nuevoEmail)) {
            algoUsado = true;
            // se muestra la alerta EmailEnUso en la ventana de login.
            ventana.EmailEnUso.setVisible(true);
        }

        if (CampoUsado("usuario", nuevoUsuario)) {
            algoUsado = true;

            // se muestra la alerta usuarioEnUso en la ventana de login.
            ventana.UsuarioEnUso.setVisible(true);
        }

        // Si algo está usado no se puede continuar.
        if (algoUsado) {
            return;
        }

        try {
            // Se crea el intérprete
            PreparedStatement psmnt = conexion.prepareStatement(sentencia);

            // se setean los datos.
            psmnt.setString(1, nuevoUsuario);
            psmnt.setString(2, nuevoPassword);
            psmnt.setString(3, nuevoEmail);
            psmnt.executeUpdate();

            // Y se crea otro intérprete con una sentencia que deja un valor por defecto
            // en la tabla perfil. Dicho valor es el usuario.
            psmnt = conexion.prepareStatement("INSERT INTO `perfil` (`usuario`) VALUES ('" + nuevoUsuario + "');");
            psmnt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
