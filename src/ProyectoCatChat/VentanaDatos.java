package ProyectoCatChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
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
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;

public class VentanaDatos extends JFrame {

    private static final long serialVersionUID = 1L;
    private String pathFotoDefecto = utils.posicion_archivos + "fotoDefecto.png";
    private String pathFotoDefectoMujer = utils.posicion_archivos + "fotoDefectoMujer.png";
    private String pathFotoDefectoHombre = utils.posicion_archivos + "fotoDefectoHombre.png";
    private JButton fotoDefecto;
    private JButton CambiarFoto;
    private Timer ponerDefecto;
    private JLabel FotoDePerfil;
    private JLabel AlertaEntry1, AlertaEntry2, AlertaEntry3, AlertaEntry4, AlertaEntry5;
    private boolean guardandoDatos;
    private boolean obteniendoFoto;
    private boolean cargandoDatos;
    private boolean manejandoDatos;

    /* Estos datos/widgets son accesibles desde cualquier archivo. */
    public JButton Siguiente;
    public String pathFoto = pathFotoDefecto;
    public JTextField CambioNombre, CambioApellido, CambioCiudad, CambioEdad;
    public JComboBox<String> Sexo;
    public String usuario = null;

    File chooserPath = new File(System.getProperty("user.home"));

    public VentanaDatos(String usuario_) {
        usuario = usuario_;

        // Tipografías
        Font fuente_entry = new Font("Josefin Sans", Font.PLAIN, 20);
        Font fuente_titulo_20 = new Font("Raleway", Font.PLAIN, 20);
        Font fuente_titulo_25 = new Font("Raleway", Font.PLAIN, 25);
        Font fuente_titulo_35 = new Font("Raleway", Font.PLAIN, 35);

        utils.Acomodar(this);

        JLabel labelGatito = new JLabel("");
        labelGatito.setIcon(new ImageIcon(utils.posicion_archivos + "Timido.png"));
        labelGatito.setBounds(135, 144, 364, 348);
        getContentPane().add(labelGatito);

        JLabel lblCompleteEstosDatos = new JLabel("Por favor, completa estos datos");
        lblCompleteEstosDatos.setFont(fuente_titulo_35);
        lblCompleteEstosDatos.setForeground(new Color(230, 230, 250));
        lblCompleteEstosDatos.setBounds(60, 548, 537, 48);
        getContentPane().add(lblCompleteEstosDatos);

        JLabel lblNewLabel_1 = new JLabel("Pusheen quiere saber más acerca ti");
        lblNewLabel_1.setForeground(new Color(230, 230, 250));
        lblNewLabel_1.setFont(fuente_titulo_35);
        lblNewLabel_1.setBounds(28, 45, 621, 51);
        getContentPane().add(lblNewLabel_1);

        JLabel Foto = new JLabel("Foto de perfil");
        Foto.setForeground(Color.WHITE);
        Foto.setFont(fuente_titulo_25);
        Foto.setBounds(1050, 110, 162, 48);
        getContentPane().add(Foto);

        CambiarFoto = new JButton("Cambiar");
        CambiarFoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String preCambiado = pathFoto;
                pathFoto = elegirArchivo();
                if (pathFoto != null) {
                    FotoDePerfil.setIcon(utils.CrearIcono(pathFoto, 150, -1, true));
                    fotoDefecto.setVisible(true);
                }
                else {
                    pathFoto = preCambiado;
                }
            }
        });
        CambiarFoto.setFont(fuente_titulo_25);
        CambiarFoto.setBounds(1050, 350, 150, 35);
        getContentPane().add(CambiarFoto);

        JLabel lblNombre = new JLabel("Nombre");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(fuente_titulo_25);
        lblNombre.setBounds(655, 125, 110, 30);
        getContentPane().add(lblNombre);

        JLabel lblNewLabel_3 = new JLabel("Apellido");
        lblNewLabel_3.setFont(fuente_titulo_25);
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setBounds(655, 195, 110, 30);
        getContentPane().add(lblNewLabel_3);

        JLabel lblEdad = new JLabel("Edad");
        lblEdad.setForeground(Color.WHITE);
        lblEdad.setFont(fuente_titulo_25);
        lblEdad.setBounds(655, 265, 110, 30);
        getContentPane().add(lblEdad);

        JLabel lblCiudad = new JLabel("Ciudad");
        lblCiudad.setForeground(Color.WHITE);
        lblCiudad.setFont(fuente_titulo_25);
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
        fotoDefecto.setIcon(utils.CrearIcono(utils.posicion_archivos + "ponerDefecto.png", 20, 20, true));
        fotoDefecto.setContentAreaFilled(false);

        ponerDefecto = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Sexo.getSelectedItem().toString() == "Femenino") {
                    pathFoto = pathFotoDefectoMujer;
                }
                else if (Sexo.getSelectedItem().toString() == "Masculino") {
                    pathFoto = pathFotoDefectoHombre;
                }
                else {
                    pathFoto = pathFotoDefecto;
                }
                fotoDefecto.setIcon(utils.CrearIcono(utils.posicion_archivos + "ponerDefecto.png", 20, 20, true));
                FotoDePerfil.setIcon(utils.CrearIcono(pathFoto, 150, -1, true));
                fotoDefecto.setVisible(false);
                ponerDefecto.stop();
            }
        });
        fotoDefecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                fotoDefecto.setIcon(utils.CrearIcono(utils.posicion_archivos + "ponerDefecto_.png", 20, 20, true));
                ponerDefecto.restart();
            }
        });

        FotoDePerfil = new JLabel();
        FotoDePerfil.setBackground(new Color(51, 153, 255));
        FotoDePerfil.setBounds(1050, 180, 150, 150);
        getContentPane().add(FotoDePerfil);
        FotoDePerfil.setIcon(utils.CrearIcono(pathFotoDefecto, 150, -1, true));
        FotoDePerfil.setBorder(new LineBorder(Color.WHITE, 2, true));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 144, 255));
        panel.setBounds(1050, 180, 150, 150);
        getContentPane().add(panel);
        panel.setLayout(null);

        UIManager.put("ToolTip.background", Color.decode("#EF6161"));
        ToolTipManager.sharedInstance().setInitialDelay(0);

        AlertaEntry1 = utils.CrearAlertaEntry(25, 2);
        AlertaEntry2 = utils.CrearAlertaEntry(25, 2);
        AlertaEntry3 = utils.CrearAlertaEntry(25, 2);
        AlertaEntry4 = utils.CrearAlertaEntry(25, 2);
        AlertaEntry5 = utils.CrearAlertaEntry(25, 2);

        final JLabel[] alertas = { AlertaEntry1, AlertaEntry2, AlertaEntry3, AlertaEntry4, AlertaEntry5 };

        Timer llamarAtencionAlertaEntry = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (utils.iconoAlertaEntry_path.endsWith("AlertaEntry.png")) {
                    utils.iconoAlertaEntry_path = utils.posicion_archivos + "AlertaEntry_.png";
                }
                else {
                    utils.iconoAlertaEntry_path = utils.posicion_archivos + "AlertaEntry.png";
                }

                String iconoFinal = utils.iconoAlertaEntry_path;
                if (cargandoDatos || guardandoDatos) {
                    String palabra;
                    if (cargandoDatos) {
                        palabra = "Cargando";
                    }
                    else {
                        palabra = "Guardando";
                    }

                    for (JLabel alerta : alertas) {
                        alerta.setToolTipText("<html><p><font size='4' face='Raleway'>" + palabra
                                + " datos, espere por favor</font></p></html>");
                    }

                    iconoFinal = utils.posicion_archivos + "ajax-loader.gif";

                }
                else {
                    AlertaEntry1.setToolTipText(
                            "<html><p><font size='4' face='Raleway'>Ingrese un nombre</font></p></html>");
                    AlertaEntry2.setToolTipText(
                            "<html><p><font size='4' face='Raleway'>Ingrese un apellido</font></p></html>");
                    AlertaEntry3.setToolTipText(
                            "<html><p><font size='4' face='Raleway'>Ingrese una ciudad</font></p></html>");
                    AlertaEntry4.setToolTipText(
                            "<html><p><font size='4' face='Raleway'>Ingrese una edad (entera)</font></p></html>");
                }

                ImageIcon iconoAlertaEntry = utils.CrearIcono(iconoFinal, 20, 20, true);
                if (manejandoDatos) {
                    return;
                }
                for (JLabel alerta : alertas) {
                    alerta.setIcon(iconoAlertaEntry);
                }
                if (cargandoDatos || guardandoDatos) {
                    manejandoDatos = true;
                }
            }
        });

        CambioNombre = new JTextField();
        CambioNombre.setFont(fuente_entry);
        CambioNombre.setBounds(780, 125, 200, 30);
        getContentPane().add(CambioNombre);
        CambioNombre.setColumns(10);
        CambioNombre.setLayout(new BorderLayout());
        CambioNombre.add(AlertaEntry1, BorderLayout.EAST);
        CambioNombre.setDocument(new LimiteTexto(15));

        CambioApellido = new JTextField();
        CambioApellido.setFont(fuente_entry);
        CambioApellido.setColumns(10);
        CambioApellido.setBounds(780, 195, 200, 30);
        getContentPane().add(CambioApellido);
        CambioApellido.setLayout(new BorderLayout());
        CambioApellido.add(AlertaEntry2, BorderLayout.EAST);

        CambioApellido.setDocument(new LimiteTexto(15));

        CambioCiudad = new JTextField();
        CambioCiudad.setFont(fuente_entry);
        CambioCiudad.setBounds(780, 335, 200, 30);
        getContentPane().add(CambioCiudad);
        CambioCiudad.setLayout(new BorderLayout());
        CambioCiudad.add(AlertaEntry3, BorderLayout.EAST);
        CambioCiudad.setDocument(new LimiteTexto(20));

        JLabel lblSexo = new JLabel("Sexo");
        lblSexo.setForeground(Color.WHITE);
        lblSexo.setBounds(655, 390, 100, 40);
        lblSexo.setFont(fuente_titulo_25);
        getContentPane().add(lblSexo);

        Sexo = new JComboBox<String>();
        Sexo.setFocusable(false);
        Sexo.setFont(fuente_entry);
        Sexo.setForeground(Color.BLACK);
        Sexo.setBackground(Color.WHITE);
        Sexo.addItem("Sin especificar");
        Sexo.addItem("Masculino");
        Sexo.addItem("Femenino");
        Sexo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nuevoSexo = Sexo.getSelectedItem().toString();
                boolean imagenCambiada = pathFoto != pathFotoDefecto && pathFoto != pathFotoDefectoHombre
                        && pathFoto != pathFotoDefectoMujer;
                if (!imagenCambiada) {
                    if (nuevoSexo == "Femenino") {
                        pathFoto = pathFotoDefectoMujer;
                    }
                    else if (nuevoSexo == "Masculino") {
                        pathFoto = pathFotoDefectoHombre;
                    }
                    else {
                        pathFoto = pathFotoDefecto;
                    }
                    FotoDePerfil.setIcon(utils.CrearIcono(pathFoto, 150, -1, true));
                }
            }
        });

        Sexo.setBounds(780, 400, 200, 30);
        getContentPane().add(Sexo);

        AlertaEntry5.setToolTipText(
                "<html><p><font size='4' face='Raleway'>Cargando datos, espere por favor</font></p></html>");
        AlertaEntry5.setBounds(988, 405, 20, 20);
        getContentPane().add(AlertaEntry5);

        CambioEdad = new JFormattedTextField();
        CambioEdad.setFont(fuente_entry);
        CambioEdad.setBounds(780, 265, 200, 30);
        getContentPane().add(CambioEdad);
        CambioEdad.setLayout(new BorderLayout());
        CambioEdad.add(AlertaEntry4, BorderLayout.EAST);
        CambioEdad.setDocument(new SoloEnteros(2));

        JLabel labelGatito1 = new JLabel("");
        labelGatito1.setIcon(new ImageIcon(utils.posicion_archivos + "Friki.png"));
        labelGatito1.setBounds(702, 351, 436, 331);
        getContentPane().add(labelGatito1);

        Siguiente = new JButton("Siguiente");
        Siguiente.setFont(fuente_titulo_20);
        Siguiente.setBounds(1124, 636, 130, 35);
        getContentPane().add(Siguiente);

        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 1280, 720);
        getContentPane().add(lblFondo);
        lblFondo.setIcon(new ImageIcon(utils.posicion_archivos + "Fondo.jpg"));

        llamarAtencionAlertaEntry.start();

        final Timer chequearAlertas = new Timer(200, new ActionListener() {
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

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
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
                }
                catch (NullPointerException | SQLException e) {
                }

                String antesLoader = pathFoto;
                try {
                    obteniendoFoto = true;
                    FotoDePerfil.setHorizontalAlignment(SwingConstants.CENTER);
                    FotoDePerfil.setIcon(new ImageIcon(utils.posicion_archivos + "ajax-loader.gif"));
                    ObtenerFoto();
                    antesLoader = pathFoto;
                }
                catch (Exception e) {
                }
                finally {
                    obteniendoFoto = false;
                    cargandoDatos = false;
                }

                FotoDePerfil.setHorizontalAlignment(SwingConstants.LEADING);
                FotoDePerfil.setIcon(utils.CrearIcono(antesLoader, 150, -1, true));

                CambiarFoto.setEnabled(true);
                CambioNombre.setEnabled(true);
                CambioCiudad.setEnabled(true);
                CambioApellido.setEnabled(true);
                CambioEdad.setEnabled(true);
                Sexo.setEnabled(true);

                manejandoDatos = false;
                chequearAlertas.start();
                AlertaEntry5.setVisible(false);

            }
        });
        t1.start();

        Timer todoLleno = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean completo = false;
                completo = !AlertaEntry1.isVisible() && !AlertaEntry2.isVisible() && !AlertaEntry3.isVisible()
                        && !AlertaEntry4.isVisible() && (!obteniendoFoto || !cargandoDatos);
                Siguiente.setEnabled(completo);
            }
        });
        todoLleno.start();

        Siguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                guardandoDatos = true;
                manejandoDatos = false;
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

    private String elegirArchivo() {
        JFileChooser chooser = new JFileChooser();
        PanelPrevisualizacion preview = new PanelPrevisualizacion(chooser);
        chooser.setDialogTitle("Seleccione una imagen");

        String[] extensiones = ImageIO.getWriterFormatNames();
        extensiones = unique(extensiones);
        FileFilter filtroImagen = new FileNameExtensionFilter("Imágenes", extensiones);

        chooser.setCurrentDirectory(chooserPath);
        chooser.addChoosableFileFilter(filtroImagen);
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setAccessory(preview);
        chooser.addPropertyChangeListener(preview);
        this.setCursor(utils.waitCursor);
        int resultado = chooser.showOpenDialog(null);
        this.setCursor(utils.defaultCursor);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            chooserPath = chooser.getCurrentDirectory();
            File archivoSeleccionado = chooser.getSelectedFile();
            return archivoSeleccionado.getAbsolutePath();
        }
        else {
            chooserPath = chooser.getCurrentDirectory();
            return null;
        }
    }

    public void ObtenerFoto() {
        String path = "?";
        try {
            path = utils.ObtenerFoto(usuario);
        }
        catch (NullPointerException | SQLException | IOException e) {
        }

        if (path.equals("?")) {
            return;
        }

        pathFoto = path;
        pathFotoDefecto = path;
        pathFotoDefectoHombre = path;
        pathFotoDefectoMujer = path;
        FotoDePerfil.setIcon(utils.CrearIcono(path, 150, -1, true));
    }

    public void ObtenerDatos() throws SQLException {
        Object[] datos = utils.ObtenerDatosEdicion(usuario);

        String nombre = (String) datos[0];
        String apellido = (String) datos[1];
        int edad = (int) datos[2];
        String ciudad = (String) datos[3];
        String sexo = (String) datos[4];

        if (!nombre.equals("")) {
            try {
                CambioNombre.getDocument().insertString(0, nombre, null);
            }
            catch (BadLocationException e) {
            }
        }

        if (!apellido.equals("")) {
            CambioApellido.setText(apellido);
        }

        if (!ciudad.equals("")) {
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
        Set<String> set = new HashSet<String>();
        for (String string : strings) {
            String name = string.toLowerCase();
            if (name.contains("gif")) {
                continue;
            }
            set.add(name);
        }
        return set.toArray(new String[0]);
    }
}
