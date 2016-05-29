/**
 * @author Theo den Exter, ARS
 * Date: May 21th 2016
 * Version 1.0
 *
 * Delimeter class for size of input field
 *
 * History
 *
 */
package eu.org.srk.radar;

import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class JTextFieldLimit extends PlainDocument {
	private int limit;

	JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr)
			throws BadLocationException {
		if (str == null)
			return;

		String s = str.trim();
		if ((getLength() + s.length()) <= limit) {
			super.insertString(offset, s, attr);
		}
	}
}