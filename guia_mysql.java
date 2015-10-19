package proyecto-test;
import java.sql.*;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class main {

    static String sentencia_sql = null;
    static Statement stmt = null;

    public static void main(String[] args) throws Exception {
        String nombre_driver = "com.mysql.jdbc.Driver";
        String nombre_usuario = "root";
        String nombre_base = "tienda";
        String contra = "ignacio";
        String servidor = "localhost";

        Class.forName(nombre_driver);

        String url = "jdbc:mysql://" + servidor + "/" + nombre_base + "?user=" + nombre_usuario + "&password=" + contra;
        Connection conexion = DriverManager.getConnection(url);
        stmt = conexion.createStatement();

        // Elimino la tabla fabricantes para empezar
        sentencia_sql = "DROP TABLE ARTICULOS,FABRICANTES";
        try {
            ejecutar(true);
        } catch (MySQLSyntaxErrorException error) {
            // No existe
        }

        sentencia_sql = "CREATE TABLE FABRICANTES (Clave_fabricante INT NOT NULL, Nombre VARCHAR(30), PRIMARY KEY (Clave_fabricante))";

        // Ejecuto la sentencia
        ejecutar(true);

        sentencia_sql = "CREATE TABLE ARTICULOS (Clave_articulo INT NOT NULL, Nombre VARCHAR(30), Precio INT, Clave_fabricante INT, PRIMARY KEY (Clave_articulo), FOREIGN KEY(Clave_fabricante) REFERENCES FABRICANTES (Clave_fabricante))";
        ejecutar(true);

        String datos_fabricantes[] = new String[5];
        datos_fabricantes[0] = "INSERT INTO FABRICANTES VALUES ( 1 , 'Kingston')";
        datos_fabricantes[1] = "INSERT INTO FABRICANTES VALUES ( 2 , 'Adata')";
        datos_fabricantes[2] = "INSERT INTO FABRICANTES VALUES ( 3 , 'Logitech')";
        datos_fabricantes[3] = "INSERT INTO FABRICANTES VALUES ( 4 , 'Lexar')";
        datos_fabricantes[4] = "INSERT INTO FABRICANTES VALUES ( 5 , 'Seagate')";

        for (int pos = 0; pos < 5; pos++) {
            sentencia_sql = datos_fabricantes[pos];
            ejecutar(true);
        }

        String datos_articulos[] = new String[10];
        datos_articulos[0] = "INSERT INTO ARTICULOS VALUES ( 1, 'Teclado',                    100, 3 )";
        datos_articulos[1] = "INSERT INTO ARTICULOS VALUES ( 2, 'Disco duro 300gb',           500, 5 )";
        datos_articulos[2] = "INSERT INTO ARTICULOS VALUES ( 3, 'Mouse',                      80,  3 )";
        datos_articulos[3] = "INSERT INTO ARTICULOS VALUES ( 4, 'Memoria USB',                140, 4 )";
        datos_articulos[4] = "INSERT INTO ARTICULOS VALUES ( 5, 'Memoria RAM',                290, 1 )";
        datos_articulos[5] = "INSERT INTO ARTICULOS VALUES ( 6, 'Disco duro extraíble 250gb', 650, 5 )";
        datos_articulos[6] = "INSERT INTO ARTICULOS VALUES ( 7, 'Memoria USB',                279, 1 )";
        datos_articulos[7] = "INSERT INTO ARTICULOS VALUES ( 8, 'DVD Rom',                    450, 2 )";
        datos_articulos[8] = "INSERT INTO ARTICULOS VALUES ( 9, 'CD Rom',                     200, 2 )";
        datos_articulos[9] = "INSERT INTO ARTICULOS VALUES ( 10, 'Tarjeta de red',            180, 3 )";

        for (int pos = 0; pos < 10; pos++) {
            sentencia_sql = datos_articulos[pos];
            ejecutar(true);
        }

        String consultas[] = new String[25];

        consultas[0] = "SELECT * FROM ARTICULOS";
        consultas[1] = "SELECT Nombre FROM ARTICULOS";
        consultas[2] = "SELECT Nombre,Precio FROM ARTICULOS";
        consultas[3] = "SELECT DISTINCT  Nombre  FROM ARTICULOS";
        consultas[4] = "SELECT  *  FROM ARTICULOS WHERE Clave_articulo=5";
        consultas[5] = "SELECT  *  FROM ARTICULOS WHERE Nombre='Teclado'";
        consultas[6] = "SELECT  *  FROM ARTICULOS WHERE Nombre='Memoria RAM' OR Nombre='Memoria USB' ";
        consultas[7] = "SELECT  *  FROM ARTICULOS WHERE Nombre LIKE 'M%';";
        consultas[8] = "SELECT  Nombre  FROM ARTICULOS WHERE Precio = 100;";
        consultas[9] = "SELECT  Nombre  FROM ARTICULOS WHERE Precio > 200;";
        consultas[10] = "SELECT  *  FROM ARTICULOS WHERE Precio >= 100 AND Precio<=350";
        consultas[11] = "SELECT  *  FROM ARTICULOS WHERE Precio BETWEEN 100 AND 350";
        consultas[12] = "SELECT  AVG(Precio)  FROM ARTICULOS;";
        consultas[13] = "SELECT  AVG(Precio)  FROM ARTICULOS WHERE Clave_fabricante=3";
        consultas[14] = "SELECT  Nombre, Precio  FROM ARTICULOS ORDER BY Nombre";
        consultas[15] = "SELECT  *  FROM ARTICULOS ORDER BY Precio DESC";
        consultas[16] = "SELECT  Nombre, Precio  FROM ARTICULOS WHERE Precio >= 250 ORDER BY Precio DESC, Nombre";
        consultas[17] = "SELECT  *  FROM ARTICULOS, FABRICANTES WHERE ARTICULOS.Clave_fabricante=FABRICANTES.Clave_fabricante";
        consultas[18] = "SELECT  ARTICULOS.Clave_articulo, ARTICULOS.Nombre, FABRICANTES.Nombre FROM ARTICULOS, FABRICANTES WHERE ARTICULOS.Clave_fabricante=FABRICANTES.Clave_fabricante";
        consultas[19] = "SELECT  ARTICULOS.Nombre, ARTICULOS.Precio FROM ARTICULOS, FABRICANTES WHERE FABRICANTES.Nombre='Logitech' AND ARTICULOS.Clave_fabricante=FABRICANTES.Clave_fabricante ORDER BY ARTICULOS.Nombre";
        consultas[20] = "SELECT  ARTICULOS.Nombre, ARTICULOS.Precio, FABRICANTES.Nombre FROM ARTICULOS, FABRICANTES WHERE FABRICANTES.Nombre='Lexar' OR  FABRICANTES.Nombre='Kingston'AND  ARTICULOS.Clave_fabricante=FABRICANTES.Clave_fabricante ORDER BY ARTICULOS.Precio DESC";

        for (int pos = 0; pos < 21; pos++) {
            sentencia_sql = consultas[pos];
            mostrar_datos();
        }

        String ordenes[] = new String[5];
        ordenes[0] = "INSERT INTO ARTICULOS VALUES (11 ,'Altavoces', 120, 2)";
        ordenes[1] = "UPDATE ARTICULOS SET Nombre='Impresora Laser' WHERE Clave_articulo = 8";
        ordenes[2] = "UPDATE ARTICULOS SET Precio=Precio *0.10";
        ordenes[3] = "UPDATE ARTICULOS SET Precio=Precio-10 WHERE   Precio >= 300";
        ordenes[4] = "DELETE FROM ARTICULOS WHERE Clave_articulo= 6";

        for (int pos = 0; pos < 5; pos++) {
            sentencia_sql = ordenes[pos];
            ejecutar(true);
        }


    };

    public static void mostrar_datos() throws SQLException {
        System.out.println("\n ********************************* \n");
        System.out.println("Resultado de: '" + sentencia_sql + "'");
        ResultSet rs = main.stmt.executeQuery(sentencia_sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnas = rsmd.getColumnCount();

        for (int i = 1; i <= columnas; i++) {
            if (i > 1) System.out.print(",  ");
            System.out.print(rsmd.getColumnName(i));
        }
        System.out.println("");

        while (rs.next()) {
            for (int i = 1; i <= columnas; i++) {
                if (i > 1) System.out.print(",  ");
                System.out.print(rs.getString(i));
            }
            System.out.println("");
        }
    }

    public static void ejecutar(boolean actualiza) throws SQLException {
        System.out.println("**** Ejecutando: `" + sentencia_sql + "`");
        if (actualiza) {
            stmt.executeUpdate(sentencia_sql);
        } else {
            stmt.executeQuery(sentencia_sql);
        }
    }
};
