package ProyectoCatChat;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class PanelPrevisualizacion extends JPanel implements PropertyChangeListener {
    private static final long serialVersionUID = -6974646137771314674L;
    private int width, height;
    private ImageIcon icon;
    private Image image;
    private static final int ACCSIZE = 155;
    private Color bg;
    private Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
    private Cursor defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
    private JFileChooser ventana;
    private String[] formatNames;

    public PanelPrevisualizacion(JFileChooser ventanad) {
        ventana = ventanad;
        setPreferredSize(new Dimension(ACCSIZE, -1));
        bg = getBackground();
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        if (propertyName.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {

            File selection = (File) e.getNewValue();
            String name;

            if (selection == null) {
                return;
            }
            else {
                name = selection.getAbsolutePath();
            }

            formatNames = ImageIO.getWriterFormatNames();
            formatNames = unique(formatNames);
            boolean esVisible = false;
            for (String formato : formatNames) {
                esVisible = name.toLowerCase().endsWith(formato);
                if (esVisible) {
                    break;
                }
            }

            if (name != null && esVisible) {
                ventana.setCursor(waitCursor);
                icon = new ImageIcon(name);
                image = icon.getImage();
                scaleImage();
                repaint();
                ventana.setCursor(defaultCursor);
            }
            else {
                ventana.setCursor(waitCursor);
                image = null;
                repaint();
                ventana.setCursor(defaultCursor);
            }
        }
        else {
            ventana.setCursor(waitCursor);
            image = null;
            repaint();
            ventana.setCursor(defaultCursor);
        }
    }

    private void scaleImage() {
        width = image.getWidth(this);
        height = image.getHeight(this);
        double ratio = 1.0;
        if (width >= height) {
            ratio = (double) (ACCSIZE - 5) / width;
            width = ACCSIZE - 5;
            height = (int) (height * ratio);
        }
        else {
            if (getHeight() > 150) {
                ratio = (double) (ACCSIZE - 5) / height;
                height = ACCSIZE - 5;
                width = (int) (width * ratio);
            }
            else {
                ratio = (double) getHeight() / height;
                height = getHeight();
                width = (int) (width * ratio);
            }
        }
        image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(bg);
        g.fillRect(0, 0, ACCSIZE, getHeight());
        if (image != null) {
            g.drawImage(image, getWidth() / 2 - width / 2 + 5, getHeight() / 2 - height / 2, this);
        }
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