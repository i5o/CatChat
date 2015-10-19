package ProyectoCatChat;

import java.awt.Toolkit;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LimiteTexto extends PlainDocument {
	private static final long serialVersionUID = 1L;
	private int limit;

	LimiteTexto(int limit) {
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null) return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		} else {
			Toolkit.getDefaultToolkit().beep();
		}
	}
}