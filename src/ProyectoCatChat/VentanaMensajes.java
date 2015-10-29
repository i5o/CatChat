package ProyectoCatChat;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONObject;

public class VentanaMensajes extends JFrame {
    private static final long serialVersionUID = 1L;

    private int cantidadUsuarios = 0;
    private int cantidadMensajes = 0;
    private int y_mensajes = 0;
    private String usuario = null;
    private String seleccionado = null;
    private String pathMiFoto = utils.posicion_archivos + "ajax-loader.gif";
    private Map<String, JLabel> fotos = new HashMap<String, JLabel>();
    private Map<String, String> fotosPath = new HashMap<String, String>();
    private Map<String, String> nombreUsuarios = new HashMap<String, String>();
    private Map<String, JButton> botonesChatUsuarios = new HashMap<String, JButton>();
    private JSONObject mensajes = null;
    private JSONArray mensajes_json = null;

    private JLabel fotodePerfil;
    private JButton seleccionado_btn;
    private JPanel panelUsuarios;
    private JPanel panelMensajes;
    private JSplitPane splitPrincipal;
    private JPanel panelCargandoMensajes;
    private JPanel panelUsoChat;
    private JLabel lblCargandoMensajes;
    private JLabel lblIconoCargandoChat;

    private JScrollPane scrollMensajes;
    private Timer chequearMensajes;

    public JButton btnEditarDatos;

    public VentanaMensajes(String usuario_) {
        usuario = usuario_;

        utils.Acomodar(this);

        splitPrincipal = new JSplitPane();
        splitPrincipal.setDividerLocation(300);
        splitPrincipal.setBounds(0, 0, 1280, 692);
        splitPrincipal.setDividerSize(0);
        splitPrincipal.setEnabled(false);
        splitPrincipal.setBackground(new Color(0, 0, 0, 0));
        getContentPane().add(splitPrincipal);

        JSplitPane splitUsuarios = new JSplitPane();
        splitUsuarios.setDividerSize(2);
        splitUsuarios.setEnabled(false);
        splitUsuarios.setDividerLocation(60);
        splitUsuarios.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitUsuarios.setBackground(new Color(0, 0, 0, 0));

        splitPrincipal.setLeftComponent(splitUsuarios);

        panelUsuarios = new JPanel(new ScrollLayout());
        panelUsuarios.setBackground(new Color(0, 0, 0, 0));

        JScrollPane scrollUsuarios = new JScrollPane(panelUsuarios);
        scrollUsuarios.getVerticalScrollBar().setUnitIncrement(20);
        scrollUsuarios.setBackground(new Color(0, 0, 0, 0));
        scrollUsuarios.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        splitUsuarios.setRightComponent(scrollUsuarios);

        JPanel panelDatosUsuario = new JPanel();
        panelDatosUsuario.setBackground(new Color(0, 0, 0, 0));
        splitUsuarios.setLeftComponent(panelDatosUsuario);
        panelDatosUsuario.setLayout(null);

        fotodePerfil = new JLabel();
        fotodePerfil.setBounds(5, 4, 50, 50);
        panelDatosUsuario.add(fotodePerfil);
        fotodePerfil.setVerticalAlignment(SwingConstants.CENTER);
        fotodePerfil.setHorizontalAlignment(SwingConstants.CENTER);
        fotodePerfil.setIcon(utils.CrearIcono(pathMiFoto, 30, 30, true));
        fotodePerfil.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));

        JPanel fotodePerfilMarco = new JPanel();
        fotodePerfilMarco.setBounds(5, 4, 50, 50);
        panelDatosUsuario.add(fotodePerfilMarco);
        fotodePerfilMarco.setBorder(new LineBorder(new Color(30, 144, 255), 1, true));
        fotodePerfilMarco.setBackground(new Color(30, 144, 255));

        btnEditarDatos = CrearBotonAnimado("editarPerfil.png", "editarPerfil_.png");
        btnEditarDatos.setToolTipText("Haz click aquí para editar tu perfil.");
        btnEditarDatos.setBounds(240, 18, 20, 20);
        panelDatosUsuario.add(btnEditarDatos);

        final JLabel lblNombre = new JLabel("?");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Josefin Sans", Font.BOLD, 18));
        lblNombre.setBounds(65, 14, 185, 30);
        panelDatosUsuario.add(lblNombre);

        // Cómo escribirle a alguien.
        panelUsoChat = new JPanel();
        panelUsoChat.setBackground(new Color(0, 0, 0, 0));
        panelUsoChat.setLayout(null);
        JLabel lblUsoChat =
            new JLabel(
                "<html><center>Para comenzar a chatear, seleccione un usuario clickeando el icono (el icono es el que está actualmente en el fondo) que aparece al costado de su información.<br>\r\n</center>");
        lblUsoChat.setForeground(new Color(0, 0, 255));
        lblUsoChat.setFont(new Font("Raleway", Font.BOLD, 27));
        lblUsoChat.setVerticalAlignment(SwingConstants.TOP);
        lblUsoChat.setHorizontalAlignment(SwingConstants.CENTER);
        lblUsoChat.setBounds(220, 223, 538, 243);
        panelUsoChat.add(lblUsoChat);

        JLabel lblIconoUsoChat = new JLabel();
        lblIconoUsoChat.setBounds(165, 11, 649, 668);
        lblIconoUsoChat.setIcon(new ImageIcon(utils.posicion_archivos + "seleccion.png"));
        panelUsoChat.add(lblIconoUsoChat);

        // Cargando mensajes / usuarios
        panelCargandoMensajes = new JPanel();
        panelCargandoMensajes.setBackground(new Color(0, 0, 0, 0));
        splitPrincipal.setRightComponent(panelCargandoMensajes);
        panelCargandoMensajes.setLayout(null);
        lblCargandoMensajes = new JLabel("Cargando usuarios");
        lblCargandoMensajes.setForeground(new Color(0, 0, 255));
        lblCargandoMensajes.setFont(new Font("Raleway", Font.BOLD, 30));
        lblCargandoMensajes.setVerticalAlignment(SwingConstants.TOP);
        lblCargandoMensajes.setHorizontalAlignment(SwingConstants.CENTER);
        lblCargandoMensajes.setBounds(220, 280, 538, 243);
        panelCargandoMensajes.add(lblCargandoMensajes);

        lblIconoCargandoChat = new JLabel();
        lblIconoCargandoChat.setBounds(165, 11, 649, 668);
        lblIconoCargandoChat.setIcon(new ImageIcon(utils.posicion_archivos + "CargandoMensajes.gif"));
        panelCargandoMensajes.add(lblIconoCargandoChat);

        final JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 1280, 720);
        getContentPane().add(lblFondo);
        lblFondo.setIcon(new ImageIcon(utils.posicion_archivos + "Fondo.jpg"));

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String nombre = ObtenerNombre(usuario);
                    lblNombre.setText(nombre);
                    lblNombre.repaint();
                    lblFondo.repaint();
                    AgregarUsuarios();

                    String path = ObtenerFoto(usuario);
                    fotosPath.put(usuario, path);
                    fotodePerfil.setIcon(utils.CrearIcono(path, 48, 48, true));

                    for (final String user_ : fotos.keySet()) {
                        Thread cargarFoto = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String path = ObtenerFoto(user_);
                                JLabel miLabelFoto = fotos.get(user_);
                                miLabelFoto.setIcon(utils.CrearIcono(path, 55, 55, true));
                                fotosPath.put(user_, path);
                                JButton boton = botonesChatUsuarios.get(user_);
                                boton.setVisible(true);
                                splitPrincipal.setRightComponent(panelUsoChat);
                            }
                        });
                        cargarFoto.start();
                    }
                }
                catch (SQLException e) {
                }
            }
        });
        t1.start();

        // Redibujar el fondo para evitar problemas...
        Timer redibujarFondo = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lblFondo.repaint();
            }
        });
        redibujarFondo.start();

        // Chequear mensajes nuevos
        chequearMensajes = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] nuevosMensajes = utils.MensajesNuevos(mensajes_json, usuario, seleccionado);
                for (String mensaje_json : nuevosMensajes) {
                    if (mensaje_json == null) {
                        continue;
                    }
                    JSONArray mensaje = new JSONObject(mensaje_json).getJSONArray("Mensaje");
                    CrearWidgetMensaje(mensaje.getString(0), mensaje.getString(1), mensaje.getString(2));
                }
                // Actualizar el json
                mensajes = new JSONObject(utils.ObtenerMensajes(usuario, seleccionado));
                mensajes_json = mensajes.getJSONArray("Mensajes");
            }
        });
    }

    public void AgregarUsuario(final String usuario_) {
        JPanel usuario = new JPanel();
        usuario.setBackground(new Color(0, 0, 0, 0));
        usuario.setLayout(null);
        usuario.setPreferredSize(new Dimension(270, 70));

        int y = cantidadUsuarios * 70;
        if (cantidadUsuarios > 0) {
            y += 6 * cantidadUsuarios;
        }
        usuario.setLocation(0, y);
        panelUsuarios.add(usuario);
        panelUsuarios.repaint();

        JLabel labelFoto = new JLabel();
        labelFoto.setVerticalAlignment(SwingConstants.CENTER);
        labelFoto.setHorizontalAlignment(SwingConstants.CENTER);
        fotos.put(usuario_, labelFoto);

        labelFoto.setIcon(utils.CrearIcono(utils.posicion_archivos + "/ajax-loader.gif", 30, 30, true));
        labelFoto.setBounds(7, 5, 58, 58);
        labelFoto.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
        usuario.add(labelFoto);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
        panel.setBackground(new Color(30, 144, 255));
        panel.setBounds(7, 5, 58, 58);
        usuario.add(panel);

        String nombre = "?";
        try {
            nombre = ObtenerNombre(usuario_);
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }

        JLabel label = new JLabel(nombre);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Josefin Sans", Font.BOLD, 18));
        label.setBounds(75, 5, 185, 30);
        usuario.add(label);

        JButton btnChatear = CrearBotonChat(usuario_);
        btnChatear.setToolTipText("Haz click aquí para chatear con ésta persona.");
        btnChatear.setBounds(230, 35, 35, 35);
        usuario.add(btnChatear);

        cantidadUsuarios++;
        botonesChatUsuarios.put(usuario_, btnChatear);

        btnChatear.setVisible(false);

        JSeparator separador = new JSeparator();
        separador.setPreferredSize(new Dimension(300, 4));
        int yy = y;
        if (cantidadUsuarios > 1) {
            yy -= 3;
        }
        separador.setLocation(0, yy);
        panelUsuarios.add(separador);
        panelUsuarios.revalidate();
    }

    public JButton CrearBotonAnimado(final String fotoA, final String fotoB) {
        final JButton botonAnimado = new JButton();
        botonAnimado.setIcon(utils.CrearIcono(utils.posicion_archivos + fotoA, 20, 20, true));
        botonAnimado.setBackground(new Color(0, 0, 0, 0));
        botonAnimado.setFocusPainted(false);
        botonAnimado.setFocusable(false);
        botonAnimado.setContentAreaFilled(false);
        botonAnimado.setBorder(BorderFactory.createEmptyBorder());

        final Timer iconoPresionado = new Timer(150, null);
        iconoPresionado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                botonAnimado.setIcon(utils.CrearIcono(utils.posicion_archivos + fotoA, 20, 20, true));
                iconoPresionado.stop();
            }
        });
        botonAnimado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                botonAnimado.setIcon(utils.CrearIcono(utils.posicion_archivos + fotoB, 20, 20, true));
                iconoPresionado.restart();
            }
        });

        botonAnimado.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(utils.handCursor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(utils.defaultCursor);
            }
        });
        return botonAnimado;
    }

    public JButton CrearBotonChat(final String usuario) {
        final JButton botonSeleccion = new JButton();
        botonSeleccion.setIcon(utils.CrearIcono(utils.posicion_archivos + "seleccion.png", 35, 35, true));
        botonSeleccion.setBackground(new Color(0, 0, 0, 0));
        botonSeleccion.setFocusPainted(false);
        botonSeleccion.setFocusable(false);
        botonSeleccion.setContentAreaFilled(false);
        botonSeleccion.setBorder(BorderFactory.createEmptyBorder());
        botonSeleccion.setToolTipText("Haz click aquí para empezar a hablar con ésta persona.");

        botonSeleccion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (seleccionado_btn != null) {
                    seleccionado_btn.setIcon(utils.CrearIcono(utils.posicion_archivos + "seleccion.png", 35, 35, true));
                }
                botonSeleccion.setIcon(utils.CrearIcono(utils.posicion_archivos + "seleccion_.png", 35, 35, true));
                seleccionado = usuario;
                seleccionado_btn = botonSeleccion;
                splitPrincipal.setRightComponent(null);
                splitPrincipal.revalidate();
                splitPrincipal.repaint();
                splitPrincipal.setRightComponent(panelCargandoMensajes);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        CrearListaMensajes(usuario);
                    }
                });
            }
        });

        botonSeleccion.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(utils.handCursor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(utils.defaultCursor);
            }
        });
        return botonSeleccion;
    }

    public String ObtenerFoto(String user) {
        if (fotosPath.containsKey(user)) {
            return fotosPath.get(user);
        }

        String path = "?";
        try {
            path = utils.ObtenerFoto(user);
        }
        catch (NullPointerException | SQLException | IOException e) {
        }

        return path;
    }

    public String ObtenerNombre(String user) {
        if (nombreUsuarios.containsKey(user)) {
            return nombreUsuarios.get(user);
        }

        String nombre = utils.ObtenerNombre(user);
        nombreUsuarios.put(user, nombre);
        return nombre;
    }

    public void AgregarUsuarios() throws SQLException {
        ArrayList<String> usuarios = utils.ObtenerUsuarios();
        int cantidadUsuarios;
        for (cantidadUsuarios = 0; cantidadUsuarios < usuarios.size(); cantidadUsuarios++) {
            final String nombreUsuario = usuarios.get(cantidadUsuarios);
            if (nombreUsuario.equals(usuario)) {
                continue;
            }
            AgregarUsuario(nombreUsuario);
            panelUsuarios.revalidate();
            panelUsuarios.repaint();
        }

        if (cantidadUsuarios == 1) {
            lblCargandoMensajes.setText("");
            lblIconoCargandoChat.setIcon(new ImageIcon(utils.posicion_archivos + "soloTu.png"));
        }
    }

    public void CrearWidgetMensaje(String user_, String mensaje_, String fecha_) {
        Color color;
        int x = 20;
        if (!user_.equals(usuario)) {
            x = 410;
            color = new Color(72, 209, 204);
        }
        else {

            color = new Color(30, 144, 255);
        }

        y_mensajes = 20;
        if (cantidadMensajes > 0) {
            y_mensajes += 120 * cantidadMensajes;
        }

        String nombreUser_ = ObtenerNombre(user_);

        cantidadMensajes++;
        JPanel Mensaje = new JPanel();
        Mensaje.setPreferredSize(new Dimension(535, 100));
        Mensaje.setBackground(new Color(0, 0, 0, 0));
        Mensaje.setBorder(new LineBorder(color, 3));
        Mensaje.setLocation(x, y_mensajes);
        Mensaje.setLayout(null);

        JLabel fotoBorde1 = new JLabel("");
        fotoBorde1.setHorizontalAlignment(SwingConstants.CENTER);
        fotoBorde1.setBorder(new LineBorder(color, 2));
        fotoBorde1.setBounds(13, 26, 60, 60);
        fotoBorde1.setIcon(utils.CrearIcono(ObtenerFoto(user_), 60, 60, true));
        Mensaje.add(fotoBorde1);

        JPanel bordeFalso = new JPanel();
        bordeFalso.setBounds(0, 0, 535, 19);
        Mensaje.add(bordeFalso);
        bordeFalso.setBackground(color);
        bordeFalso.setLayout(null);

        JLabel usuario = new JLabel(nombreUser_ + " dice:");
        usuario.setBounds(10, 0, 510, 19);
        usuario.setHorizontalAlignment(SwingConstants.LEFT);
        usuario.setForeground(Color.WHITE);
        usuario.setFont(new Font("Raleway", Font.BOLD, 15));
        bordeFalso.add(usuario);

        JLabel fecha = new JLabel(fecha_);
        fecha.setForeground(Color.WHITE);
        fecha.setFont(new Font("Raleway", Font.BOLD, 13));
        fecha.setBounds(350, 2, 350, 14);
        bordeFalso.add(fecha);

        JScrollPane scroll = new JScrollPane() {
            private static final long serialVersionUID = 1L;

            @Override
            public void setBorder(Border border) {
            }
        };

        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBounds(81, 21, 446, 72);
        Mensaje.add(scroll);

        JTextArea mensaje = new JTextArea(mensaje_);
        mensaje.setSize(50, 70);
        mensaje.setLocation(21, 0);
        mensaje.setBackground(Color.WHITE);
        mensaje.setFont(new Font("Josefin Sans", Font.PLAIN, 18));
        mensaje.setEditable(false);
        mensaje.setLineWrap(true);

        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBackground(Color.WHITE);
        scroll.setViewportView(mensaje);
        panelMensajes.add(Mensaje);
        panelMensajes.revalidate();
        panelMensajes.repaint();
        irAbajo(scrollMensajes);
    }

    public void CrearListaMensajes(String user_) {
        cantidadMensajes = 0;

        final JSplitPane splitMensajes = new JSplitPane();
        splitMensajes.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitMensajes.setDividerSize(2);
        splitMensajes.setEnabled(false);
        splitMensajes.setDividerLocation(600);
        splitMensajes.setBackground(new Color(0, 0, 0, 0));

        JSplitPane splitInternalMensajes = new JSplitPane();
        splitInternalMensajes.setBackground(new Color(0, 0, 0, 0));
        splitInternalMensajes.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitMensajes.setLeftComponent(splitInternalMensajes);
        splitInternalMensajes.setDividerSize(2);
        splitInternalMensajes.setEnabled(false);
        splitInternalMensajes.setDividerLocation(60);

        JPanel panelDatosOtro = new JPanel();
        panelDatosOtro.setBackground(new Color(0, 0, 0, 0));
        splitInternalMensajes.setLeftComponent(panelDatosOtro);
        panelDatosOtro.setLayout(null);

        JLabel label = new JLabel();
        label.setIcon(utils.CrearIcono(ObtenerFoto(seleccionado), 50, 50, true));
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(new LineBorder(new Color(255, 165, 0), 2, true));
        label.setBounds(882, 4, 50, 50);
        panelDatosOtro.add(label);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(new Color(30, 144, 255), 1, true));
        panel.setBackground(new Color(30, 144, 255));
        panel.setBounds(882, 4, 50, 50);
        panelDatosOtro.add(panel);

        JLabel lblNombre1 = new JLabel("Hablando con: " + ObtenerNombre(seleccionado));
        lblNombre1.setBounds(59, 14, 600, 30);
        panelDatosOtro.add(lblNombre1);
        lblNombre1.setForeground(Color.WHITE);
        lblNombre1.setFont(new Font("Josefin Sans", Font.BOLD, 18));

        panelMensajes = new JPanel(new ScrollLayout());
        panelMensajes.setBackground(new Color(0, 0, 0, 0));
        scrollMensajes = new JScrollPane(panelMensajes);
        scrollMensajes.getVerticalScrollBar().setUnitIncrement(20);
        scrollMensajes.setBackground(new Color(0, 0, 0, 0));
        scrollMensajes.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        splitInternalMensajes.setRightComponent(scrollMensajes);

        JPanel panelEnvio = new JPanel();
        panelEnvio.setBackground(new Color(0, 0, 0, 0));
        splitMensajes.setRightComponent(panelEnvio);
        panelEnvio.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBackground(new Color(0, 0, 0, 0));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 913, 87);
        panelEnvio.add(scrollPane);

        final JTextArea Mensaje = new JTextArea();
        Mensaje.setFont(new Font("Josefin Sans", Font.BOLD, 15));
        Placeholder Mensaje_pl = new Placeholder("Escriba el mensaje a enviar", Mensaje, true);
        Mensaje_pl.changeAlpha(0.4f);
        Mensaje.setLineWrap(true);
        Mensaje.setLocation(487, 0);
        scrollPane.setViewportView(Mensaje);
        ToolTipManager.sharedInstance().setInitialDelay(0);
        JButton btnEnviar = CrearBotonAnimado("/Enviar.png", "/Enviar_.png");
        btnEnviar.setToolTipText("Click para enviar.");
        btnEnviar.setBounds(917, 18, 50, 50);
        panelEnvio.add(btnEnviar);

        lblCargandoMensajes.setText("Cargando mensajes");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ObtenerMensajes(seleccionado, usuario);
                splitPrincipal.setRightComponent(splitMensajes);
            }
        });
        t1.start();

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void actionPerformed(ActionEvent arg0) {
                Date fechaActual = new Date();
                String x = Mensaje.getText();
                if (x.isEmpty() || x == null || x.trim().isEmpty()) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }
                EnviarMensaje(x, fechaActual.toLocaleString());
                Mensaje.setText("");
            }
        });
    }

    public void ObtenerMensajes(String user1, String user2) {
        String jsonmensajes = utils.ObtenerMensajes(user1, user2);
        mensajes = new JSONObject(jsonmensajes);
        mensajes_json = mensajes.getJSONArray("Mensajes");
        cantidadMensajes = 0;
        for (int m = 0; m < mensajes_json.length(); m++) {
            JSONArray d = mensajes_json.getJSONArray(m);
            CrearWidgetMensaje(d.getString(0), d.getString(1), d.getString(2));
        }
        chequearMensajes.start();
    }

    public void EnviarMensaje(String mensaje, String fecha) {
        chequearMensajes.stop();
        long numeroAleatorio = new Random().nextLong();
        String[] a = { usuario, mensaje, fecha, String.valueOf(numeroAleatorio) };
        mensajes_json.put(a);
        mensajes_json = new JSONObject(mensajes.toString()).getJSONArray("Mensajes");

        CrearWidgetMensaje(usuario, mensaje, fecha);

        utils.GuardarMensajes(mensajes, usuario, seleccionado);
        chequearMensajes.start();
    }

    private void irAbajo(JScrollPane scrollPane) {
        final JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        AdjustmentListener downScroller = new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                verticalBar.removeAdjustmentListener(this);
            }
        };
        verticalBar.addAdjustmentListener(downScroller);
    }
}