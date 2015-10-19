/*
 * Proyecto final de 2do BG grupo Agustina Martinez, Lucia Hernández, Tamara Lemes, Ignacio Rodríguez
 * Copyright (C) 2015 Agustina Martinez
 * Copyright (C) 2015 Lucia Hernández
 * Copyright (C) 2015 Tamara Lemes
 * Copyright (C) 2015 Ignacio Rodríguez

 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.event.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
public class interfaz {

    static String path_fondo = "/Users/nacho/Desktop/proyecto/fondo2.jpg";
    static String bardo = "/Users/nacho/Desktop/proyecto/Bardo_sinfondo.png";
    static int largo = 1280;
    static int ancho = 720;
    static int posicion = 0;
    static Font fuente = new Font("Futura", Font.PLAIN, 20);

    public static void main(String[] args) {
        JFrame ventana = new JFrame("Proyecto");
        final JLayeredPane panel = ventana.getLayeredPane();

        Inicio(panel);

        ventana.pack();
        ventana.setVisible(true);
        ventana.setSize(largo, ancho);
        ventana.setLocationRelativeTo(null);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
    }

    public static void Inicio(JLayeredPane panel) {
        JLabel fondo = new JLabel(CrearIcono(path_fondo, largo, ancho, true));
        JPanel fondo_panel = CrearPanel(0, -10, largo, ancho, fondo);
        panel.add(fondo_panel, ObtenerPosicion());

        JLabel bardo_label = new JLabel(CrearIcono(bardo, 0, 0, false));
        JPanel bardo_panel = CrearPanel(30, 110, 480, 490, bardo_label);
        panel.add(bardo_panel, ObtenerPosicion());

        JButton btnEntrar = CrearBoton(975, 50, "Entrar", panel);
        JButton btnRegistrar = CrearBoton(900, 600, "Abrir una cuenta", panel);

        JTextField entryUsuario = CrearEntry(600, 50, "Usuario", panel, false, 400, 11);
        JTextField entryContra = CrearEntry(800, 50, "Contraseña", panel, true, 400, 11);

        JTextField entryNombre = CrearEntry(700, 300, "Nombre", panel, false, 400, 11);
        JTextField entryApellido = CrearEntry(900, 300, "Apellido", panel, false, 400, 11);

        JTextField entryEmail = CrearEntry(600, 350, "Correo electrónico", panel, false, 800, 24);

        JTextField entryUsuarioNuevo = CrearEntry(700, 400, "Nuevo usuario", panel, false, 400, 11);
        JTextField entryContraNuevo = CrearEntry(900, 400, "Nueva contraseña", panel, true, 400, 11);

    }

    public static JTextField CrearEntry(int x, int y, String placeholder, final JComponent panel, boolean tapar, int largo, int columnas) {
        JTextField entry = null;

        if (tapar)	{ entry = new JPasswordField(columnas); }
        else { entry = new JTextField(columnas); }

        entry.setFont(fuente);
        TextPrompt entry_pl = new TextPrompt(placeholder, entry);
        entry_pl.changeAlpha(0.2f);
        JPanel boton_panel = CrearPanel(x, y, largo, 80, entry);
        panel.add(boton_panel, ObtenerPosicion());

        entry.getDocument().addDocumentListener(new DocumentListener() {
          public void changedUpdate(DocumentEvent e) {
              RefrescarTodo(panel);
          }
          public void removeUpdate(DocumentEvent e) {
              RefrescarTodo(panel);
          }
          public void insertUpdate(DocumentEvent e) {
              RefrescarTodo(panel);
          }
        });

        return entry;
    }

    public static JButton CrearBoton(int x, int y, String texto, final JLayeredPane panel) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente);
        JPanel boton_panel = CrearPanel(x, y, 400, 80, boton);
        panel.add(boton_panel, ObtenerPosicion());
        boton.repaint();
        boton.setFocusable(false);

        boton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
              RefrescarTodo(panel);
          }
        });

        return boton;

    }

    public static Integer ObtenerPosicion() {
        posicion++;
        Integer pos = new Integer(posicion);
        return pos;
    }

    public static JPanel CrearPanel(int x, int y, int l, int a, JComponent item) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, l, a);
        panel.add(item);
        panel.setBackground(new Color(0,0,0,0));
        return panel;

    }

    public static ImageIcon CrearIcono(String path, int l, int a, boolean redimensionar) {
        ImageIcon icono = new ImageIcon(path);
        if (redimensionar) {
            System.out.println(l);
            Image img = icono.getImage();
            Image newimg = img.getScaledInstance(l, a,  java.awt.Image.SCALE_FAST);
            ImageIcon new_icono = new ImageIcon(newimg);
            return new_icono;

        }
        else {
            return icono;
        }
    }

    public static void RefrescarTodo(JComponent panel) {
        panel.repaint();
    }

    public static void LlamarInicio(JComponent panel) {
        panel.removeAll();
    }
}
