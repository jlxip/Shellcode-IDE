package net.jlxip.shellcodeide;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CopyToClipboard {
	public static void copy(String data) {
		StringSelection stringSelection = new StringSelection(data);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
	}
}
