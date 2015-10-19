import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class pruebas {
    static Font fuente = new Font("Futura", Font.PLAIN, 20);

    static String sentencia_sql = null;
    static Statement stmt = null;
    public static void main(String[] args) throws Exception {
        JFrame ventana = new JFrame("pruebas");

        String nombre_driver = "com.mysql.jdbc.Driver";
        String nombre_usuario = "proyectoutu";
        String nombre_base = "usuarios";
        String contra = "kuckuckrocks";
        String servidor = "kuckuck.treehouse.su:3306";

        String url = "jdbc:mysql://" + servidor + "/" + nombre_base + "?user=" + nombre_usuario + "&password=" + contra;
        Connection conexion = DriverManager.getConnection(url);
        stmt = conexion.createStatement();

        // crear tablas
        sentencia_sql = "CREATE TABLE `login` ( `usuario` VARCHAR(30) NOT NULL, `contra` VARCHAR(30) NOT NULL, PRIMARY KEY (`usuario`))";
        try {
            stmt.executeUpdate(sentencia_sql);
        } catch (MySQLSyntaxErrorException error) {
            // Ya existe
        }


        ventana.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel labelNuevoUsuario = new JLabel("Nuevo usuario");
        JLabel labelNuevaContra = new JLabel("Nueva contraseña");
        final JTextField entryNuevoUsuario = new JTextField(30);
        final JPasswordField entryNuevaContra = new JPasswordField(30);


        JLabel labelUsuario = new JLabel("Usuario");
        JLabel labelContra = new JLabel("Contraseña");
        final JTextField entryUsuario = new JTextField(30);
        final JPasswordField entryContra = new JPasswordField(30);


        JButton registrar = new JButton("Registrarse");
        JButton entrar = new JButton("Entrar");

        final JLabel resultado = new JLabel("Registro: Sin información aún");
        final JLabel resultado2 = new JLabel("Entrada: Sin información aún");

        registrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = "Sin información aun";
                try {
                    AddUser(entryNuevoUsuario, entryNuevaContra);
                    msg = "Usuario creado";
                } catch (Exception error) {
                    String error_ = error.toString();

                    if (error_.contains("Duplicate")) {
                        msg = "El usuario ya existe";
                    }


                    if (error_.contains("user-passwd")) {
                        msg = "Ingrese un usuario y contraseña.";
                    }

                }
                resultado.setText("Registro: " + msg);
                try {
                    sentencia_sql = "SELECT * FROM login;";
                    System.out.println(obtener_datos(true));
                } catch (Exception error) {
                    System.out.println(error);
                }
            }
        });

        entrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String msg = "Sin información aun";
                try {
                    boolean correcto = CheckUser(entryUsuario, entryContra);
                    if (correcto) {
                        msg = "Los datos ingresados son correctos";
                    } else {
                        msg = "Los datos ingresados son incorrectos";
                    }
                } catch (Exception error) {
                    String error_ = error.toString();
                    System.out.println(error_);
                    if (error_.contains("user-passwd")) {
                        msg = "Ingrese un usuario y contraseña.";
                    }
                }
                resultado2.setText("Entrada: " + msg);
            }
        });

        c.gridx = 0;
        c.gridy = 0;
        ventana.add(labelNuevoUsuario, c);
        c.gridx = 0;
        c.gridy = 1;
        ventana.add(entryNuevoUsuario, c);
        c.gridx = 1;
        c.gridy = 0;
        ventana.add(labelNuevaContra, c);
        c.gridx = 1;
        c.gridy = 1;
        ventana.add(entryNuevaContra, c);
        c.gridx = 0;
        c.gridy = 2;
        ventana.add(labelUsuario, c);
        c.gridx = 0;
        c.gridy = 3;
        ventana.add(entryUsuario, c);
        c.gridx = 1;
        c.gridy = 2;
        ventana.add(labelContra, c);
        c.gridx = 1;
        c.gridy = 3;
        ventana.add(entryContra, c);
        c.gridx = 2;
        c.gridy = 1;
        ventana.add(registrar, c);
        c.gridx = 2;
        c.gridy = 3;
        ventana.add(entrar, c);
        c.gridx = 0;
        c.gridy = 4;
        ventana.add(resultado, c);
        c.gridx = 1;
        c.gridy = 4;
        ventana.add(resultado2, c);

        ventana.pack();
        ventana.setVisible(true);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
    }

    public static void AddUser(JTextField user_, JTextField contra) throws Exception, SQLException {
        String user = user_.getText();
        String passwd = contra.getText();
        // Comprobar si no estaban vacios.

        if (user.isEmpty() || passwd.isEmpty()) {
            throw new Exception("Ingresar user-passwd");
        }
        sentencia_sql = "insert into login values ('" + user + "', '" + passwd + "');";
        stmt.executeUpdate(sentencia_sql);
    }

    public static boolean CheckUser(JTextField user_, JTextField contra) throws Exception, SQLException {
        String user = user_.getText();
        String passwd = contra.getText();

        if (user.isEmpty() || passwd.isEmpty()) {
            throw new Exception("Ingresar user-passwd");
        }

        boolean correcto = false;
        sentencia_sql = "select * from `login` WHERE `contra`='" + passwd + "' and `usuario`='" + user + "';";
        String datos = obtener_datos(false);

        if (datos == "") {
            System.out.println("Datos incorrectos;");
            correcto = false;
        } else {
            System.out.println(datos);
            System.out.println("Los datos son correctos.");
            correcto = true;
        }

        // Comprobar si no estaban vacios.



        return correcto;
    }

    public static String obtener_datos(boolean nom_column) throws SQLException {
        ResultSet rs = stmt.executeQuery(sentencia_sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnas = rsmd.getColumnCount();

        String texto = "";
        if (nom_column) {
            for (int i = 1; i <= columnas; i++) {
                if (i > 1) texto += ",  ";
                texto += rsmd.getColumnName(i);
            }
            texto += "\n";
        }

        while (rs.next()) {
            for (int i = 1; i <= columnas; i++) {
                if (i > 1) texto += ",  ";
                texto += rs.getString(i);
            }
            texto += "\n";
        }

        return texto;
    }
}
