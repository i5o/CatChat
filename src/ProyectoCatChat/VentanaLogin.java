package ProyectoCatChat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class VentanaLogin extends JFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public JLabel lblNewLabel, datosIncorrectos, EmailEnUso, UsuarioEnUso;
    public JButton Entrar, Registrarse;
    public JTextField EntrarUsuario, EntrarPassword, nuevoEmail, nuevoUsuario, nuevoPassword;

    private JLabel AlertaEntry1, AlertaEntry2, AlertaEntry3, AlertaEntry4, AlertaEntry5;
    public String iconoAlertaEntry_path;
    ImageIcon iconoAlertaEntry;

    DocumentListener chequearTexto;

    Color colorLabel = Color.decode("#EF6161");

    public VentanaLogin() {
        Font fuente_entry = new Font("Josefin Sans", Font.PLAIN, 25);
        Font fuente_titulo_20 = new Font("Raleway", Font.PLAIN, 20);
        Font fuente_titulo_30 = new Font("Raleway", Font.PLAIN, 30);
        Font fuente_titulo_55 = new Font("Raleway", Font.PLAIN, 55);

        getContentPane().setBackground(SystemColor.activeCaption);
        setIconImage(Toolkit.getDefaultToolkit().getImage(utils.posicion_archivos + "Saludo.png"));
        setTitle("CatChat");
        setBounds(100, 100, 1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setResizable(false);
        setLocationRelativeTo(null);

        lblNewLabel = new JLabel("");
        lblNewLabel.setBounds(138, 211, 313, 415);
        lblNewLabel.setIcon(new ImageIcon(utils.posicion_archivos + "Saludo.png"));
        getContentPane().add(lblNewLabel);

        iconoAlertaEntry_path = utils.posicion_archivos + "AlertaEntry.png";
        iconoAlertaEntry = utils.CrearIcono(iconoAlertaEntry_path, 20, 20, true);

        UIManager.put("ToolTip.background", Color.decode("#EF6161"));
        ToolTipManager.sharedInstance().setInitialDelay(0);

        AlertaEntry1 = utils.CrearAlertaEntry(25, 2);
        AlertaEntry2 = utils.CrearAlertaEntry(25, 2);
        AlertaEntry3 = utils.CrearAlertaEntry(-50, 30);
        AlertaEntry4 = utils.CrearAlertaEntry(-50, 30);
        AlertaEntry5 = utils.CrearAlertaEntry(-100, 30);

        AlertaEntry1.setToolTipText("<html><p><font size='4' face='Raleway'>Ingrese un usuario</font></p></html>");
        AlertaEntry2.setToolTipText("<html><p><font size='4' face='Raleway'>Ingrese una contraseña</font></p></html>");
        AlertaEntry3.setToolTipText("<html><p><font size='4' face='Raleway'>Ingrese un usuario</font></p></html>");
        AlertaEntry4.setToolTipText("<html><p><font size='4' face='Raleway'>Ingrese una contraseña</font></p></html>");
        AlertaEntry5.setToolTipText("<html><p><font size='4' face='Raleway'>Ingrese un correo electrónico</font></p></html>");

        Timer llamarAtencionAlertaEntry = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (iconoAlertaEntry_path.endsWith("AlertaEntry.png")) {
                    iconoAlertaEntry_path = utils.posicion_archivos + "AlertaEntry_.png";
                }
                else {
                    iconoAlertaEntry_path = utils.posicion_archivos + "AlertaEntry.png";
                }
                iconoAlertaEntry = utils.CrearIcono(iconoAlertaEntry_path, 20, 20, true);
                AlertaEntry1.setIcon(iconoAlertaEntry);
                AlertaEntry2.setIcon(iconoAlertaEntry);
                AlertaEntry3.setIcon(iconoAlertaEntry);
                AlertaEntry4.setIcon(iconoAlertaEntry);
                AlertaEntry5.setIcon(iconoAlertaEntry);
            }
        });

        Timer llamarAtencionInfo = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rojo = colorLabel.getRed();
                if (rojo == 239) {
                    colorLabel = Color.BLACK;
                }
                else {
                    colorLabel = Color.decode("#EF6161");
                }
                datosIncorrectos.setForeground(colorLabel);
                EmailEnUso.setForeground(colorLabel);
                UsuarioEnUso.setForeground(colorLabel);
            }
        });

        chequearTexto = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                ocultarIcono(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                ocultarIcono(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                ocultarIcono(e);
            }

            public void ocultarIcono(DocumentEvent e) {
                JTextField padre = (JTextField) e.getDocument().getProperty("padre");
                JLabel alerta = (JLabel) e.getDocument().getProperty("alerta");
                String x = padre.getText();
                if (x.isEmpty() || x == null || x.trim().isEmpty()) {
                    alerta.setVisible(true);
                }
                else {
                    alerta.setVisible(false);
                }
                if (alerta == AlertaEntry1 || alerta == AlertaEntry2) {
                    datosIncorrectos.setVisible(false);
                }

                if (alerta == AlertaEntry3) {
                    UsuarioEnUso.setVisible(false);
                }

                if (alerta == AlertaEntry5) {
                    EmailEnUso.setVisible(false);
                }
            }
        };

        // Etiqueta para nombre de usuarios
        EntrarUsuario = new JTextField();
        EntrarUsuario.setBounds(835, 100, 200, 30);
        EntrarUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        EntrarUsuario.setFont(fuente_entry);
        getContentPane().add(EntrarUsuario);

        EntrarUsuario.setDocument(new LimiteTexto(30));
        EntrarUsuario.getDocument().addDocumentListener(chequearTexto);
        EntrarUsuario.getDocument().putProperty("alerta", AlertaEntry1);
        EntrarUsuario.getDocument().putProperty("padre", EntrarUsuario);

        Placeholder txtEntrarUsuario_pl = new Placeholder("Usuario", EntrarUsuario, true);
        EntrarUsuario.add(AlertaEntry1, BorderLayout.EAST);
        txtEntrarUsuario_pl.changeAlpha(0.4f);
        EntrarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Entrar.doClick();
            }
        });

        // Etiqueta para contraseña de usuarios
        EntrarPassword = new JPasswordField();
        EntrarPassword.setBounds(835, 165, 200, 30);
        EntrarPassword.setHorizontalAlignment(SwingConstants.CENTER);
        EntrarPassword.setFont(fuente_entry);
        getContentPane().add(EntrarPassword);
        EntrarPassword.setDocument(new LimiteTexto(45));
        EntrarPassword.getDocument().addDocumentListener(chequearTexto);
        EntrarPassword.getDocument().putProperty("alerta", AlertaEntry2);
        EntrarPassword.getDocument().putProperty("padre", EntrarPassword);
        Placeholder pwdContrasea_pl = new Placeholder("Contraseña", EntrarPassword, true);
        EntrarPassword.add(AlertaEntry2, BorderLayout.EAST);
        pwdContrasea_pl.changeAlpha(0.4f);
        EntrarPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Entrar.doClick();
            }
        });

        // Botón para entrar
        Entrar = new JButton("Entrar") {
            private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(-30, 45);
            }
        };

        Entrar.setBounds(1095, 130, 110, 40);
        Entrar.setBackground(new Color(230, 230, 250));
        Entrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        Entrar.setFont(fuente_entry);
        getContentPane().add(Entrar);

        // Etiqueta bienvenida
        JLabel lblCatchat = new JLabel("¡Bienvenido a CatChat!");
        lblCatchat.setBounds(20, 50, 605, 95);
        lblCatchat.setFont(fuente_titulo_55);
        lblCatchat.setForeground(new Color(230, 230, 250));
        getContentPane().add(lblCatchat);

        // Etiqueta registrarse
        JLabel lblanNoTienes = new JLabel("¿Aún no tienes una cuenta? ¡Regístrate!");
        lblanNoTienes.setBounds(675, 275, 690, 40);
        lblanNoTienes.setFont(fuente_titulo_30);
        lblanNoTienes.setForeground(new Color(255, 255, 255));
        getContentPane().add(lblanNoTienes);

        // Etiqueta usuario
        nuevoUsuario = new JTextField();
        nuevoUsuario.setBounds(700, 345, 200, 30);
        nuevoUsuario.setFont(fuente_entry);
        getContentPane().add(nuevoUsuario);
        nuevoUsuario.setColumns(10);

        nuevoUsuario.setDocument(new LimiteTexto(30));
        nuevoUsuario.getDocument().addDocumentListener(chequearTexto);
        nuevoUsuario.getDocument().putProperty("alerta", AlertaEntry3);
        nuevoUsuario.getDocument().putProperty("padre", nuevoUsuario);

        Placeholder txtNuevoUsuario_pl = new Placeholder("Nuevo usuario", nuevoUsuario, false);
        nuevoUsuario.add(AlertaEntry3, BorderLayout.EAST);
        txtNuevoUsuario_pl.changeAlpha(0.4f);
        nuevoUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrarse.doClick();
            }
        });

        // Etiqueta contraseña
        nuevoPassword = new JPasswordField();
        nuevoPassword.setBounds(955, 345, 200, 30);
        nuevoPassword.setFont(fuente_entry);
        getContentPane().add(nuevoPassword);

        nuevoPassword.setDocument(new LimiteTexto(45));
        nuevoPassword.getDocument().addDocumentListener(chequearTexto);
        nuevoPassword.getDocument().putProperty("alerta", AlertaEntry4);
        nuevoPassword.getDocument().putProperty("padre", nuevoPassword);

        Placeholder pwdNuevaContrasea_pl = new Placeholder("Contraseña", nuevoPassword, false);
        nuevoPassword.add(AlertaEntry4, BorderLayout.EAST);
        pwdNuevaContrasea_pl.changeAlpha(0.4f);
        nuevoPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrarse.doClick();
            }
        });

        // Etiqueta correo electrÃ³nico
        nuevoEmail = new JTextField();
        nuevoEmail.setBounds(700, 410, 455, 30);
        nuevoEmail.setFont(fuente_entry);
        getContentPane().add(nuevoEmail);
        nuevoEmail.setColumns(10);

        nuevoEmail.setDocument(new LimiteTexto(200));
        nuevoEmail.getDocument().addDocumentListener(chequearTexto);
        nuevoEmail.getDocument().putProperty("alerta", AlertaEntry5);
        nuevoEmail.getDocument().putProperty("padre", nuevoEmail);

        Placeholder txtCorreoElectrnico_pl = new Placeholder("Correo electrónico", nuevoEmail, false);
        nuevoEmail.add(AlertaEntry5, BorderLayout.EAST);
        txtCorreoElectrnico_pl.changeAlpha(0.4f);
        nuevoEmail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registrarse.doClick();
            }
        });

        // Botón registrarse
        Registrarse = new JButton("Registrarse") {
            private static final long serialVersionUID = 1L;

            @Override
            public Point getToolTipLocation(MouseEvent e) {
                return new Point(-15, 50);
            }
        };
        Registrarse.setBounds(850, 482, 160, 47);
        Registrarse.setFont(fuente_entry);
        Registrarse.setBackground(new Color(230, 230, 250));
        getContentPane().add(Registrarse);

        JLabel lblyaEstsRegistrado = new JLabel("¿Ya estás registrado? ¡Ingresa!");
        lblyaEstsRegistrado.setForeground(Color.WHITE);
        lblyaEstsRegistrado.setFont(fuente_titulo_30);
        lblyaEstsRegistrado.setBounds(664, 11, 422, 40);
        getContentPane().add(lblyaEstsRegistrado);

        JLabel lblNewLabel_1 = new JLabel();
        lblNewLabel_1.setIcon(new ImageIcon(utils.posicion_archivos + "Sir.png"));
        lblNewLabel_1.setBounds(622, 62, 171, 184);
        getContentPane().add(lblNewLabel_1);

        JLabel lblNewLabel_2 = new JLabel();
        lblNewLabel_2.setIcon(new ImageIcon(utils.posicion_archivos + "Pintando.png"));
        lblNewLabel_2.setBounds(956, 449, 318, 259);
        getContentPane().add(lblNewLabel_2);

        datosIncorrectos = new JLabel("Datos incorrectos");
        datosIncorrectos.setForeground(Color.decode("#EF6161"));
        datosIncorrectos.setFont(fuente_entry);
        datosIncorrectos.setHorizontalAlignment(SwingConstants.CENTER);
        datosIncorrectos.setBounds(835, 130, 200, 35);
        datosIncorrectos.setVisible(false);
        getContentPane().add(datosIncorrectos);

        UsuarioEnUso = new JLabel("El usuario ya existe");
        UsuarioEnUso.setForeground(Color.decode("#EF6161"));
        UsuarioEnUso.setFont(fuente_entry);
        UsuarioEnUso.setBounds(701, 375, 200, 35);
        UsuarioEnUso.setVisible(false);
        getContentPane().add(UsuarioEnUso);

        EmailEnUso = new JLabel("Ya existe una cuenta con ese correo electrónico");
        EmailEnUso.setForeground(new Color(239, 97, 97));
        EmailEnUso.setFont(fuente_titulo_20);
        EmailEnUso.setBounds(700, 437, 455, 35);
        EmailEnUso.setVisible(false);
        getContentPane().add(EmailEnUso);

        JLabel lblNewLabel_3 = new JLabel();
        lblNewLabel_3.setIcon(new ImageIcon(utils.posicion_archivos + "Fondo.jpg"));
        lblNewLabel_3.setBounds(0, 0, 1280, 720);
        getContentPane().add(lblNewLabel_3);

        Timer datosEntradaLlenos = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean completo = false;
                completo = !AlertaEntry1.isVisible() && !AlertaEntry2.isVisible();
                Entrar.setEnabled(completo);
                if (!completo) {
                    Entrar.setToolTipText("<html><center style='font-family: Raleway; font-size: 11px; color: black;'>Ingrese los datos requeridos<br>para iniciar sesión</center></html>");
                }
                else {
                    Entrar.setToolTipText(null);
                }
            }
        });

        Timer datosRegistroLlenos = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean completo = false;
                completo = !AlertaEntry3.isVisible() && !AlertaEntry4.isVisible() && !AlertaEntry5.isVisible();
                Registrarse.setEnabled(completo);
                if (!completo) {
                    Registrarse.setToolTipText("<html><center style='font-family: Raleway; font-size: 11px; color: black;'>Ingrese los datos requeridos<br>para registrarse</center></html>");
                }
                else {
                    Registrarse.setToolTipText(null);
                }
            }
        });

        llamarAtencionAlertaEntry.start();
        datosEntradaLlenos.start();
        datosRegistroLlenos.start();
        llamarAtencionInfo.start();

    }
}
