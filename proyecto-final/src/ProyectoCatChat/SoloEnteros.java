package ProyectoCatChat;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class SoloEnteros extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int cifras;

	SoloEnteros(int cifras) {
		super();
		this.cifras = cifras;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null) return;
		if (!((getLength() + str.length()) <= cifras)) {
			Toolkit.getDefaultToolkit().beep();
			return;
		}
		try {
			Integer.parseInt(str);
			super.insertString(offset, str, attr);
		} catch (NumberFormatException nfe) {
			Toolkit.getDefaultToolkit().beep();
		}
	}
}