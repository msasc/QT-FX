/*
 * Copyright (C) 2017 Miquel Sas
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.qtfx.lib.gui;

import java.io.OutputStream;
import java.io.PrintStream;

import com.qtfx.lib.util.Strings;

import javafx.application.Platform;
import javafx.scene.control.Control;
import javafx.scene.control.TextArea;

/**
 * A console mapped to a text area.
 *
 * @author Miquel Sas
 */
public class Console {
	
	/**
	 * Output stream implementor.
	 */
	class OS extends OutputStream {

		/**
		 * Write a byte.
		 */
		@Override
		public void write(int b) {
			append(new String(new byte[] { (byte) b }));
		}

		/**
		 * Write bytes from a source byte array.
		 */
		@Override
		public void write(byte src[], int off, int len) {
			if (src == null) {
				throw new NullPointerException();
			} else if ((off < 0)
				|| (off > src.length)
				|| (len < 0)
				|| ((off + len) > src.length)
				|| ((off + len) < 0)) {
				throw new IndexOutOfBoundsException();
			} else if (len == 0) {
				return;
			}
			byte[] dest = new byte[len];
			System.arraycopy(src, off, dest, 0, len);
			append(new String(dest));
		}
	}

	/** Text area. */
	private TextArea textArea;
	/** Maximum number of line, zero no limit, default is 1000. */
	private int maxLines = 1000;
	/** This console print stream. */
	private PrintStream printStream;

	/**
	 * Constructor.
	 */
	public Console() {
		super();
		textArea = new TextArea();
		FX.setConsole(textArea, this);
	}
	
	/**
	 * Returns this console print stream.
	 * 
	 * @return This console print stream.
	 */
	public PrintStream getPrintStream() {
		if (printStream == null) {
			printStream = new PrintStream(new OS());
		}
		return printStream;
	}

	/**
	 * Append the string to the console.
	 * 
	 * @param str The string to print.
	 */
	public void append(String str) {
		Platform.runLater(() -> {
			textArea.appendText(str);
			if (maxLines > 0) {
				String text = textArea.getText();
				int lineCount = Strings.countLines(text);
				if (lineCount > (maxLines + (maxLines / 2))) {
					int startLine = 0;
					int endLine = lineCount - maxLines;
					int startOffset = Strings.offsetStartLine(text, startLine);
					int endOffset = Strings.offsetEndLine(text, endLine);
					textArea.deleteText(startOffset, endOffset);
				}
			}
			textArea.end();
		});
	}

	/**
	 * Return the control.
	 * 
	 * @return The control.
	 */
	public Control getControl() {
		return textArea;
	}
}
